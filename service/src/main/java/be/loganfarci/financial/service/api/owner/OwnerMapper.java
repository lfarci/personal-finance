package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.csv.model.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {

    public OwnerEntity toEntity(Owner owner) {
        OwnerEntity ownerEntity = null;
        if (owner != null) {
            ownerEntity = new OwnerEntity(owner.getName());
        }
        return ownerEntity;
    }

}
