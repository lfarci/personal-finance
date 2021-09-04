package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
public class OwnerServiceIT {

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    public void tearDown() {
        ownerRepository.deleteAll();
    }

    @Test
    public void shouldThrowOwnerAlreadyExistsWhenSavingExistingOwner() {
        Owner owner = new Owner("Name");
        saveOwner(owner);
        Throwable thrown = catchThrowableFor(owner);
        assertThat(thrown).isInstanceOf(OwnerEntityAlreadyExistsException.class);
    }

    private Throwable catchThrowableFor(Owner owner) {
        return catchThrowable(() -> saveOwner(owner));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingNull() {
        OwnerService ownerService = new OwnerService(ownerRepository);
        Throwable thrown = catchThrowable(() -> ownerService.save(null));
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowConstraintViolationWhenSavingOwnerWithBlankName() {
        Throwable thrown = catchThrowable(() -> saveOwner(new Owner("")));
        assertThat(thrown).isInstanceOf(OwnerEntityConstraintViolationException.class);
    }

    @Test
    public void shouldThrowConstraintViolationWhenSavingOwnerWithTooLongName() {
        Throwable thrown = catchThrowable(() -> saveOwner(new Owner("Lorem ipsum dolor sit amet, consectetur massa nunc.")));
        assertThat(thrown).isInstanceOf(OwnerEntityConstraintViolationException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingOwnerWithoutName() {
        Throwable thrown = catchThrowable(() -> saveOwner(new Owner(null)));
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    private OwnerEntity saveOwner(Owner owner) {
        OwnerService ownerService = new OwnerService(ownerRepository);
        return ownerService.save(owner);
    }

}
