package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTests {

    @Mock
    OwnerRepository ownerRepository;

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingNull() {
        assertInstanceException(null, NullPointerException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingOwnerWithoutName() {
        assertInstanceException(new Owner(null), NullPointerException.class);
    }

    @Test
    public void shouldThrowOwnerAlreadyExistsExceptionWhenSavingExistingOwner() {
        when(ownerRepository.existsById("Name")).thenReturn(true);
        assertInstanceException(new Owner("Name"), OwnerEntityAlreadyExistsException.class);
    }

    @Test
    public void shouldThrowOwnerConstraintViolationExceptionWhenDataAccessErrorOccurs() {
        when(ownerRepository.existsById(anyString())).thenReturn(false);
        lenient().when(ownerRepository.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException(""));
        assertInstanceException(new Owner("Name"), OwnerEntityConstraintViolationException.class);
    }

    public void assertInstanceException(Owner owner, Class<?> exceptionClass) {
        Throwable thrown = catchThrowable(() -> new OwnerService(ownerRepository).save(owner));
        assertThat(thrown).isInstanceOf(exceptionClass);
    }

}
