package be.loganfarci.financial.service.api.owner;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
public class OwnerRepositoryIT {

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    public void shouldThrowDataIntegrityViolationWhenSavingOwnerWithBlankName() {
        assertExceptionInstanceWithOwnerName("", DataIntegrityViolationException.class);
    }

    @Test
    public void shouldThrowDataIntegrityViolationWhenSavingOwnerWithTooLongName() {
        assertExceptionInstanceWithOwnerName("Lorem ipsum dolor sit amet, consectetur massa nunc.", DataIntegrityViolationException.class);
    }

    @Test
    public void shouldThrowJpaSystemWhenSavingOwnerWithoutName() {
        assertExceptionInstanceWithOwnerName(null, JpaSystemException.class);
    }

    @Test
    public void shouldThrowInvalidDataAccessApiUsageWhenSavingNull() {
        assertExceptionInstanceWithOwner(null, InvalidDataAccessApiUsageException.class);
    }

    @Test
    public void shouldReturnExpectedEntityWhenSavingValidOwner() {
        String ownerName = "Owner";
        OwnerEntity entity = ownerRepository.saveAndFlush(new OwnerEntity(ownerName));
        assertThat(entity.getName()).isEqualTo(ownerName);
    }

    private void assertExceptionInstance(ThrowableAssert.ThrowingCallable callable, Class<?> exceptionClass) {
        Throwable thrown = catchThrowable(callable);
        assertThat(thrown).isInstanceOf(exceptionClass);
    }

    private void assertExceptionInstanceWithOwnerName(String name, Class<?> exceptionClass) {
        assertExceptionInstance(() -> saveOwner(name), exceptionClass);
    }

    private void assertExceptionInstanceWithOwner(OwnerEntity entity, Class<?> exceptionClass) {
        assertExceptionInstance(() -> ownerRepository.saveAndFlush(entity), exceptionClass);
    }

    private OwnerEntity saveOwner(String name) {
        return ownerRepository.saveAndFlush(new OwnerEntity(name));
    }

}
