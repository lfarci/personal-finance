package be.loganfarci.financial.service.api.users.service;

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

    public void deleteById(Long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        } else {
            throw notFound(userId);
        }
    }

    public static class NotFound extends ResourceNotFound {
        public NotFound(String message) {
            super(message);
        }
    }
}
