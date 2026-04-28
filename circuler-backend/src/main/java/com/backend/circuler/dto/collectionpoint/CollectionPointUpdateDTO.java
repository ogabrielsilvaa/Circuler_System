package com.backend.circuler.dto.collectionpoint;

import com.backend.circuler.enums.CollectionPointStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class CollectionPointUpdateDTO {

    private String name;
    private String addressStreet;
    private String addressNeighborhood;
    private Integer capacityLimit;
    private Integer userAdminId;

    @Schema(
            type = "integer",
            allowableValues = {"0", "1", "2", "3"},
            description = "Status do ponto: 0 - APAGADO, 1 - ATIVO, 2 - LOTADO, 3 - INATIVO",
            example = "1"
    )
    private CollectionPointStatus status;

    public CollectionPointUpdateDTO() {}

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

    public CollectionPointStatus getStatus() { return status; }
    public void setStatus(CollectionPointStatus status) { this.status = status; }
}
