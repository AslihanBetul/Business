package com.usermanagement.mapper;

import com.usermanagement.dto.requestDTOs.UserSaveRequestDTO;
import com.usermanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Mapping(target = "role",ignore = true)
    User userSaveRequestDTOToUser(UserSaveRequestDTO userSaveRequestDTO);

}