package com.backend.circuler.service;

import com.backend.circuler.entity.BookInstance;
import com.backend.circuler.entity.CollectionPoint;
import com.backend.circuler.entity.Reservation;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.exception.ForbiddenException;
import com.backend.circuler.exception.NotFoundException;
import com.backend.circuler.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String resolveCurrentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User resolveCurrentUser() {
        String email = resolveCurrentEmail();
        return userRepository.findByEmailAndStatusNot(email, UserStatus.APAGADO)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));
    }

    public boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> roleName.equals(role.getName()));
    }

    public void assertIsRootAdmin() {
        if (!hasRole(resolveCurrentUser(), "ROLE_ROOT_ADMIN")) {
            throw new ForbiddenException("Apenas o administrador raiz pode executar esta ação.");
        }
    }

    public void assertCanManageBookInstance(BookInstance instance) {
        User currentUser = resolveCurrentUser();
        if (hasRole(currentUser, "ROLE_ROOT_ADMIN")) return;
        if (!instance.getCollectionPoint().getUserAdmin().getEmail().equals(currentUser.getEmail())) {
            throw new ForbiddenException("Você não tem permissão para gerenciar exemplares deste ponto de coleta.");
        }
    }

    public void assertCanManageCollectionPoint(CollectionPoint point) {
        User currentUser = resolveCurrentUser();
        if (hasRole(currentUser, "ROLE_ROOT_ADMIN")) return;
        if (!point.getUserAdmin().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Você não é o responsável por este ponto de coleta.");
        }
    }

    public void assertCanReadReservation(Reservation reservation) {
        User currentUser = resolveCurrentUser();
        if (hasRole(currentUser, "ROLE_ROOT_ADMIN")) return;
        if (hasRole(currentUser, "ROLE_ADMIN")) {
            String pointAdminEmail = reservation.getBookInstance().getCollectionPoint().getUserAdmin().getEmail();
            if (currentUser.getEmail().equals(pointAdminEmail)) return;
        }
        if (reservation.getUser().getId().equals(currentUser.getId())) return;
        throw new ForbiddenException("Você não tem permissão para acessar esta reserva.");
    }
}
