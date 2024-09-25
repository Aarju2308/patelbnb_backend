package com.patelbnb.user.mapper;

import com.patelbnb.user.application.dto.ReadUserDTO;
import com.patelbnb.user.domain.Authority;
import com.patelbnb.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO readUserDTOToUser(User user);

    default String mapAuthoritiesToString(Authority authority){
        return authority.getName();
    }

}
