package be.loganfarci.financial.service.owner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity(name = "owner")
public class OwnerEntity {

    @Id
    @Size(max = 50)
    @Column(name = "name")
    private String name;

}
