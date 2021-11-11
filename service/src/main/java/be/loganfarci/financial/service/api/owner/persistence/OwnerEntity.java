package be.loganfarci.financial.service.api.owner.persistence;

import be.loganfarci.financial.service.api.accounts.persistence.BankAccountEntity;
import be.loganfarci.financial.service.api.owner.model.dto.OwnerDto;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "owner")
@Check(constraints = "trim(name) <> ''")
public class OwnerEntity {

    public static final int NAME_COLUMN_LENGTH = 50;
    public static final String NAME_COLUMN_NAME = "name";
    private static final String DEFAULT_NAME = "Bank Account Owner";

    @Id
    @Column(name = NAME_COLUMN_NAME, length = NAME_COLUMN_LENGTH)
    private String name;

    @OneToMany(mappedBy = "owner", cascade = ALL, orphanRemoval = true)
    private List<BankAccountEntity> bankAccounts;

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

    public void set(OwnerDto values) {
        name = values.getName();
    }

}