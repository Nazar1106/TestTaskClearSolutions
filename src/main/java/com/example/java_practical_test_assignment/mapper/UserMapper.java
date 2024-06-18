package com.example.java_practical_test_assignment.mapper;

import com.example.java_practical_test_assignment.dto.RequestUserPartialUpdateDTO;
import com.example.java_practical_test_assignment.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    RequestUserPartialUpdateDTO userToPartialUser(User user);

    User toUserPartial(RequestUserPartialUpdateDTO user);


}
