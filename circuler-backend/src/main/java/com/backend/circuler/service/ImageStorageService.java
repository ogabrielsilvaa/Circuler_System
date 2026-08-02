package com.backend.circuler.service;

import com.backend.circuler.exception.BadRequestException;
import com.backend.circuler.exception.UnprocessableEntityException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageStorageService {

    private static final Logger log = LoggerFactory.getLogger(ImageStorageService.class);

    public static final String USERS_FOLDER = "circuler/users";
    public static final String BOOKS_FOLDER = "circuler/books";
    public static final String COLLECTION_POINTS_FOLDER = "circuler/collection-points";

    private final Cloudinary cloudinary;

    public ImageStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public ImageUploadResult upload(MultipartFile file, String folder) {
        validate(file);

        try {
            Map<?, ?> response = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    )
            );

            String url = (String) response.get("secure_url");
            String publicId = (String) response.get("public_id");

            if (url == null || publicId == null) {
                log.error("Resposta do Cloudinary sem secure_url ou public_id - folder={}", folder);
                throw new UnprocessableEntityException("Resposta inesperada do serviço de imagens.");
            }

            log.info("Imagem enviada - folder={} publicId={} bytes={}", folder, publicId, file.getSize());
            return new ImageUploadResult(url, publicId);
        } catch (IOException e) {
            log.error("Falha de I/O ao enviar imagem para o Cloudinary - folder={}", folder, e);
            throw new UnprocessableEntityException("Não foi possível enviar a imagem no momento.");
        }
    }

    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Imagem removida - publicId={}", publicId);
        } catch (IOException e) {
            log.error("Falha de I/O ao remover imagem do Cloudinary - publicId={}", publicId, e);
            throw new UnprocessableEntityException("Não foi possível remover a imagem anterior.");
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("Upload rejeitado: arquivo ausente ou vazio.");
            throw new BadRequestException("O arquivo de imagem é obrigatório.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.warn("Upload rejeitado: tipo não suportado - contentType={}", contentType);
            throw new BadRequestException("O arquivo enviado deve ser uma imagem.");
        }
    }
}
