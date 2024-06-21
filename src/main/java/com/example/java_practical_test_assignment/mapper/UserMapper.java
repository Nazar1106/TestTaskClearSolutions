package com.example.java_practical_test_assignment.mapper;

import com.example.java_practical_test_assignment.dto.CreateUserDTO;
import com.example.java_practical_test_assignment.dto.UserPartialUpdateDTO;
import com.example.java_practical_test_assignment.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserPartialUpdateDTO userToPartialUser(User user);

    User toUserPartial(UserPartialUpdateDTO user);

    User createdDtoToUser(CreateUserDTO createUserDTO);


}
