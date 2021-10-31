package be.loganfarci.financial.service.api.users.service;

import be.loganfarci.financial.service.api.errors.exceptions.ResourceConflict;
import be.loganfarci.financial.service.api.errors.exceptions.ResourceNotFound;
import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.persistence.UserEntity;
import be.loganfarci.financial.service.api.users.persistence.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    public static final String NOT_FOUND_MESSAGE_CODE = "user.not_found";
    public static final String CONFLICT_MESSAGE_CODE = "user.conflict";
    public static final String ID_MISMATCH_MESSAGE_CODE = "user.id_mismatch";
    public static final String REQUIRED_ID_MESSAGE_CODE = "user.required_id";

    private final UserRepository repository;
    private final UserMapper mapper;
    private final MessageSourceAccessor messages;

    public UserService(UserRepository repository, UserMapper mapper, MessageSource messages)
    {
        this.repository = repository;
        this.mapper = mapper;
        this.messages = new MessageSourceAccessor(messages);
    }

    private NotFound notFound(Long userId) {
        return new NotFound(messages.getMessage(NOT_FOUND_MESSAGE_CODE, new Long[]{ userId }));
    }

    private Conflict conflict(Long userId) {
        return new Conflict(messages.getMessage(CONFLICT_MESSAGE_CODE, new Long[]{ userId }));
    }

    private InvalidArgument userIdMismatch(Long bodyId, Long URLQueryParamId) {
        return new InvalidArgument(messages.getMessage(ID_MISMATCH_MESSAGE_CODE, new Long[]{ bodyId, URLQueryParamId }));
    }

    private InvalidArgument userIdIsRequired() {
        return new InvalidArgument(messages.getMessage(REQUIRED_ID_MESSAGE_CODE));
    }

    public UserDto findById(Long id) {
        Optional<UserEntity> entity = repository.findById(id);
        if (entity.isPresent()) {
            return mapper.toRest(entity.get());
        } else {
            throw notFound(id);
        }
    }

    public List<UserDto> findAll() {
        return repository.findAll().stream().map(mapper::toRest).collect(Collectors.toList());
    }

    public UserDto save(UserDto user) {
        if (existsById(user)) {
            throw conflict(user.getId());
        } else {
            return mapper.toRest(repository.save(mapper.fromRest(user)));
        }
    }

    private boolean existsById(UserDto user) {
        return user.getId() != null && repository.existsById(user.getId());
    }

    public void deleteById(Long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        } else {
            throw notFound(userId);
        }
    }

    public void updateById(Long userId, UserDto user) {
        validateUpdateArguments(userId, user);
        UserDto updated = findById(userId);
        updated.setName(user.getName());
        repository.save(mapper.fromRest(updated));
    }

    private void validateUpdateArguments(Long userId, UserDto user) {
        if (user.getId() == null) {
            throw userIdIsRequired();
        }
        if (!userId.equals(user.getId())) {
            throw userIdMismatch(user.getId(), userId);
        }
    }

    public static class NotFound extends ResourceNotFound {
        public NotFound(String message) {
            super(message);
        }
    }

    public static class Conflict extends ResourceConflict {
        public Conflict(String message) {
            super(message);
        }
    }

    public static class InvalidArgument extends RuntimeException {
        public InvalidArgument(String message) {
            super(message);
        }
    }

}
