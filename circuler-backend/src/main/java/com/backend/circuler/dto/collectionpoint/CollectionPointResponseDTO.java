package com.backend.circuler.dto.collectionpoint;

import com.backend.circuler.enums.CollectionPointStatus;

import java.time.LocalDateTime;

public class CollectionPointResponseDTO {

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

    public CollectionPointResponseDTO() {}

    public CollectionPointResponseDTO(Integer id, String name, String addressStreet,
                                      String addressNeighborhood, Integer capacityLimit,
                                      CollectionPointStatus status, Integer userAdminId,
                                      String userAdminName, String userAdminEmail,
                                      LocalDateTime createdAt, LocalDateTime updatedAt) {
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
}
