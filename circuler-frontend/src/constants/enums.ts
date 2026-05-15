export enum UserRole {
  USER = 'ROLE_USER',
  ADMIN = 'ROLE_ADMIN',
  ROOT_ADMIN = 'ROLE_ROOT_ADMIN',
}

export enum BookInstanceStatus {
  APAGADO    = 'APAGADO',
  DISPONIVEL = 'DISPONIVEL',
  RESERVADO  = 'RESERVADO',
  RETIRADO   = 'RETIRADO',
  PENDENTE   = 'PENDENTE',
}

export enum BookCategoryEnum {
  INFANTIL_JUVENIL = 'INFANTIL_JUVENIL',
  AUTOAJUDA        = 'AUTOAJUDA',
  DIDATICO         = 'DIDATICO',
  ESCOLAR          = 'ESCOLAR',
}

export enum ReservationStatus {
  APAGADA   = 'APAGADA',
  ATIVA     = 'ATIVA',
  CONCLUIDA = 'CONCLUIDA',
  CANCELADA = 'CANCELADA',
}
