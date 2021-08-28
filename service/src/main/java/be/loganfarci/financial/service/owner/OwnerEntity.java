package be.loganfarci.financial.service.owner;

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

}
