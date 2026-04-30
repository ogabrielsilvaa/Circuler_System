package com.backend.circuler.entity;

import com.backend.circuler.enums.BookInstanceStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "book_instances")
public class BookInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_point_id", nullable = false)
    private CollectionPoint collectionPoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_donor_id")
    private User userDonor;

    @Column(name = "status", nullable = false)
    private BookInstanceStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public BookInstance() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public CollectionPoint getCollectionPoint() { return collectionPoint; }
    public void setCollectionPoint(CollectionPoint collectionPoint) { this.collectionPoint = collectionPoint; }

    public User getUserDonor() { return userDonor; }
    public void setUserDonor(User userDonor) { this.userDonor = userDonor; }

    public BookInstanceStatus getStatus() { return status; }
    public void setStatus(BookInstanceStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInstance that = (BookInstance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}