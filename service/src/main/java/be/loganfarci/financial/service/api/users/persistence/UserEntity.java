package be.loganfarci.financial.service.api.users.persistence;

import be.loganfarci.financial.service.api.accounts.persistence.BankAccountEntity;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "application_user")
@Check(constraints = "trim(name) <> ''")
public class UserEntity {

    public static final int NAME_COLUMN_LENGTH = 50;

    @Id
    @GeneratedValue(generator = "user_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "user_sequence_generator",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = NAME_COLUMN_LENGTH)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<BankAccountEntity> bankAccounts;

    @CreationTimestamp
    @Column(name = "created", columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(name = "updated", columnDefinition = "TIMESTAMP")
    private LocalDateTime updateDate;

    public UserEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserEntity() {
        this(0L, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
