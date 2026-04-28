package com.backend.circuler.dto.collectionpoint;

public class CollectionPointCreateDTO {

    private String name;
    private String addressStreet;
    private String addressNeighborhood;
    private Integer capacityLimit;
    private Integer userAdminId;

    public CollectionPointCreateDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddressStreet() { return addressStreet; }
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }

    public String getAddressNeighborhood() { return addressNeighborhood; }
    public void setAddressNeighborhood(String addressNeighborhood) { this.addressNeighborhood = addressNeighborhood; }

    public Integer getCapacityLimit() { return capacityLimit; }
    public void setCapacityLimit(Integer capacityLimit) { this.capacityLimit = capacityLimit; }

    public Integer getUserAdminId() { return userAdminId; }
    public void setUserAdminId(Integer userAdminId) { this.userAdminId = userAdminId; }
}
