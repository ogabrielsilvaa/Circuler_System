package com.backend.circuler.dto.bookinstance;

import com.backend.circuler.enums.BookCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookInstanceDonateDTO {

    @NotNull(message = "O ponto de coleta é obrigatório")
    private Integer collectionPointId;

    @NotBlank(message = "O título é obrigatório")
    private String bookTitle;

    @NotBlank(message = "O autor é obrigatório")
    private String bookAuthor;

    private String bookPublisher;
    private String bookThumbnailUrl;

    @NotNull(message = "A categoria é obrigatória")
    @Schema(
            type = "integer",
            allowableValues = {"1", "2", "3", "4"},
            description = "Categoria do livro: 1 - INFANTIL_JUVENIL, 2 - AUTOAJUDA, 3 - DIDATICO, 4 - ESCOLAR",
            example = "1"
    )
    private BookCategory bookCategory;

    private String bookIsbn;

    public BookInstanceDonateDTO() {}

    public Integer getCollectionPointId() { return collectionPointId; }
    public void setCollectionPointId(Integer collectionPointId) { this.collectionPointId = collectionPointId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public String getBookPublisher() { return bookPublisher; }
    public void setBookPublisher(String bookPublisher) { this.bookPublisher = bookPublisher; }

    public String getBookThumbnailUrl() { return bookThumbnailUrl; }
    public void setBookThumbnailUrl(String bookThumbnailUrl) { this.bookThumbnailUrl = bookThumbnailUrl; }

    public BookCategory getBookCategory() { return bookCategory; }
    public void setBookCategory(BookCategory bookCategory) { this.bookCategory = bookCategory; }

    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }
}
