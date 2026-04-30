package com.backend.circuler.dto.collectionpoint;

import com.backend.circuler.enums.CollectionPointStatus;

import java.time.LocalDateTime;
import java.util.List;

public class CollectionPointDetailDTO {

    private Integer id;
    private String name;
    private String addressStreet;
    private String addressNeighborhood;
    private Integer capacityLimit;
    private CollectionPointStatus status;
    private Integer userAdminId;
    private String userAdminName;
    private String userAdminEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CollectionPointBookDTO> books;

    public CollectionPointDetailDTO() {}

    public CollectionPointDetailDTO(CollectionPointResponseDTO base, List<CollectionPointBookDTO> books) {
        this.id = base.getId();
        this.name = base.getName();
        this.addressStreet = base.getAddressStreet();
        this.addressNeighborhood = base.getAddressNeighborhood();
        this.capacityLimit = base.getCapacityLimit();
        this.status = base.getStatus();
        this.userAdminId = base.getUserAdminId();
        this.userAdminName = base.getUserAdminName();
        this.userAdminEmail = base.getUserAdminEmail();
        this.createdAt = base.getCreatedAt();
        this.updatedAt = base.getUpdatedAt();
        this.books = books;
    }

    public CollectionPointDetailDTO(Integer id, String name, String addressStreet,
                                    String addressNeighborhood, Integer capacityLimit,
                                    CollectionPointStatus status, Integer userAdminId,
                                    String userAdminName, String userAdminEmail,
                                    LocalDateTime createdAt, LocalDateTime updatedAt,
                                    List<CollectionPointBookDTO> books) {
        this.id = id;
        this.name = name;
        this.addressStreet = addressStreet;
        this.addressNeighborhood = addressNeighborhood;
        this.capacityLimit = capacityLimit;
        this.status = status;
        this.userAdminId = userAdminId;
        this.userAdminName = userAdminName;
        this.userAdminEmail = userAdminEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.books = books;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddressStreet() { return addressStreet; }
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }

    public String getAddressNeighborhood() { return addressNeighborhood; }
    public void setAddressNeighborhood(String addressNeighborhood) { this.addressNeighborhood = addressNeighborhood; }

    public Integer getCapacityLimit() { return capacityLimit; }
    public void setCapacityLimit(Integer capacityLimit) { this.capacityLimit = capacityLimit; }

    public CollectionPointStatus getStatus() { return status; }
    public void setStatus(CollectionPointStatus status) { this.status = status; }

    public Integer getUserAdminId() { return userAdminId; }
    public void setUserAdminId(Integer userAdminId) { this.userAdminId = userAdminId; }

    public String getUserAdminName() { return userAdminName; }
    public void setUserAdminName(String userAdminName) { this.userAdminName = userAdminName; }

    public String getUserAdminEmail() { return userAdminEmail; }
    public void setUserAdminEmail(String userAdminEmail) { this.userAdminEmail = userAdminEmail; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<CollectionPointBookDTO> getBooks() { return books; }
    public void setBooks(List<CollectionPointBookDTO> books) { this.books = books; }
}
