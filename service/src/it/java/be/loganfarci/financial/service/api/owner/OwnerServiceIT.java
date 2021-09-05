package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityIsInvalidException;
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
        OwnerDto owner = new OwnerDto("Name");
        saveOwner(owner);
        Throwable thrown = catchThrowableFor(owner);
        assertThat(thrown).isInstanceOf(OwnerEntityAlreadyExistsException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingNull() {
        OwnerService ownerService = new OwnerService(ownerRepository);
        Throwable thrown = catchThrowable(() -> ownerService.save((OwnerDto) null));
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowOwnerIsInvalidWhenSavingOwnerWithBlankName() {
        Throwable thrown = catchThrowable(() -> saveOwner(new OwnerDto("")));
        assertThat(thrown).isInstanceOf(OwnerEntityIsInvalidException.class);
    }

    @Test
    public void shouldThrowOwnerIsInvalidWhenSavingOwnerWithTooLongName() {
        Throwable thrown = catchThrowable(() -> saveOwner(new OwnerDto("Lorem ipsum dolor sit amet, consectetur massa nunc.")));
        assertThat(thrown).isInstanceOf(OwnerEntityIsInvalidException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingOwnerWithoutName() {
        Throwable thrown = catchThrowable(() -> saveOwner(new OwnerDto(null)));
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    private Throwable catchThrowableFor(OwnerDto owner) {
        return catchThrowable(() -> saveOwner(owner));
    }

    private OwnerEntity saveOwner(OwnerDto owner) {
        OwnerService ownerService = new OwnerService(ownerRepository);
        return ownerService.save(owner);
    }

}
