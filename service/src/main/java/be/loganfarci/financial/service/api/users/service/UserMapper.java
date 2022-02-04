package be.loganfarci.financial.service.api.users.service;

import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.persistence.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDto toRest(UserEntity entity) {
        return new UserDto(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getCreationDate(), entity.getUpdateDate());
    }

    public UserEntity fromRest(UserDto user) {
        return new UserEntity(user.getId(), user.getFirstName(), user.getLastName());
    }

}
