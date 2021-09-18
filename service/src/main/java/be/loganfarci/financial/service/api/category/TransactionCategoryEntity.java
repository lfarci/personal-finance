package be.loganfarci.financial.service.api.category;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity(name = "transaction_category")
@Check(constraints = "trim(name) <> '' and name not like parent")
public class TransactionCategoryEntity {

    @Id
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", referencedColumnName = "name")
    private TransactionCategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionCategoryEntity> children;

    public TransactionCategoryEntity(String name, String description, TransactionCategoryEntity parent, List<TransactionCategoryEntity> children) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.children = children;
    }

    public TransactionCategoryEntity() {
        this(null, null, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionCategoryEntity getParent() {
        return parent;
    }

    public void setParent(TransactionCategoryEntity parent) {
        this.parent = parent;
    }

    public List<TransactionCategoryEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TransactionCategoryEntity> children) {
        this.children = children;
    }
}
