package be.loganfarci.financial.service.api.owner;

import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "owner")
@Check(constraints = "trim(name) <> ''")
public class OwnerEntity {

    @Id
    @Column(name = "name", length = 50)
    private String name;

    public OwnerEntity(String name) {
        this.name = name;
    }

    public OwnerEntity() {
        this("Unknown owner");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
