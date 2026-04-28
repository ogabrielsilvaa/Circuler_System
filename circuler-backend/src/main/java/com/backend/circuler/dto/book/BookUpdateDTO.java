package com.backend.circuler.dto.book;

import com.backend.circuler.enums.BookCategory;
import com.backend.circuler.enums.BookStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class BookUpdateDTO {

    private String title;
    private String author;
    private String publisher;
    private String thumbnailUrl;

    @Schema(
            type = "integer",
            allowableValues = {"1", "2", "3", "4"},
            description = "Categoria do livro: 1 - INFANTIL_JUVENIL, 2 - AUTOAJUDA, 3 - DIDATICO, 4 - ESCOLAR",
            example = "4"
    )
    private BookCategory category;

    private String isbn;

    @Schema(
            type = "integer",
            allowableValues = {"0", "1"},
            description = "Status do livro: 0 - APAGADO, 1 - ATIVO",
            example = "1"
    )
    private BookStatus status;

    public BookUpdateDTO() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public BookCategory getCategory() { return category; }
    public void setCategory(BookCategory category) { this.category = category; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
}
