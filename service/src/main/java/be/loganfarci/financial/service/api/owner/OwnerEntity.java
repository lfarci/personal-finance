package be.loganfarci.financial.service.api.owner;

import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "owner")
@Check(constraints = "trim(name) <> ''")
public class OwnerEntity {

    public static final int NAME_COLUMN_LENGTH = 50;
    public static final String NAME_COLUMN_NAME = "name";
    private static final String DEFAULT_NAME = "Bank Account Owner";

    @Id
    @Column(name = NAME_COLUMN_NAME, length = NAME_COLUMN_LENGTH)
    private String name;

    public OwnerEntity(String name) {
        this.name = name;
    }

    public OwnerEntity() {
        this(DEFAULT_NAME);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}