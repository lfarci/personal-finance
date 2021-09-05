package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityIsInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTests {

    @Mock
    OwnerRepository ownerRepository;

    @BeforeEach
    public void setUp() {
        lenient().when(ownerRepository.existsById(anyString())).thenReturn(true);
    }

    @Test
    public void shouldReturnEntityWhenFindingAnExistingEntity() {
        OwnerService service = new OwnerService(ownerRepository);
        Optional<OwnerEntity> entity = Optional.of(new OwnerEntity("name"));
        when(ownerRepository.findById(anyString())).thenReturn(entity);
        assertThat(service.findByName("name")).isSameAs(entity.get());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingNull() {
        assertInstanceException(null, NullPointerException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSavingOwnerWithoutName() {
        assertInstanceException(new OwnerDto(null), NullPointerException.class);
    }

    @Test
    public void shouldThrowOwnerIsInvalidExceptionWhenSavingOwnerWithEmptyName() {
        assertInvalidOwnerIsInvalidFor("");
    }

    @Test
    public void shouldThrowOwnerIsInvalidExceptionWhenSavingOwnerWithBlankName() {
        assertInvalidOwnerIsInvalidFor("        ");
    }

    @Test
    public void shouldThrowOwnerAlreadyExistsExceptionWhenSavingExistingOwner() {
        assertInstanceException(new OwnerDto("Name"), OwnerEntityAlreadyExistsException.class);
    }

    @Test
    public void shouldReturnSavedEntityWhenSavingValidOwner() {
        OwnerService service = new OwnerService(ownerRepository);
        String ownerName = "Owner name";
        lenient().when(ownerRepository.existsById(anyString())).thenReturn(false);
        when(ownerRepository.save(any())).thenReturn(new OwnerEntity(ownerName));
        assertThat(service.save(new OwnerDto(ownerName)).getName()).isEqualTo(ownerName);
    }

    private void assertInvalidOwnerIsInvalidFor(String s) {
        lenient().when(ownerRepository.save(any())).thenReturn(new OwnerEntity(s));
        Throwable thrown = catchThrowable(() -> new OwnerService(ownerRepository).save(new OwnerDto(s)));
        assertThat(thrown).isInstanceOf(OwnerEntityIsInvalidException.class);
    }

    public void assertInstanceException(OwnerDto owner, Class<?> exceptionClass) {
        Throwable thrown = catchThrowable(() -> new OwnerService(ownerRepository).save(owner));
        assertThat(thrown).isInstanceOf(exceptionClass);
    }

}
