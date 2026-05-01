package com.backend.circuler.dto.collectionpoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CollectionPointCreateDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O endereço é obrigatório")
    private String addressStreet;

    @NotBlank(message = "O bairro é obrigatório")
    private String addressNeighborhood;

    @NotNull(message = "A capacidade é obrigatória")
    private Integer capacityLimit;

    @NotNull(message = "O ID do administrador responsável é obrigatório")
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
