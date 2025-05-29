package com.horvath.usernotesapp.api.controller;

import com.horvath.usernotesapp.api.UserApi;
import com.horvath.usernotesapp.api.dto.UserDto;
import com.horvath.usernotesapp.domain.User;
import com.horvath.usernotesapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        UserDto createdUserDto = mapDtoObject(userService.createUser(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long userId, UserDto userDto) {
        UserDto updatedUserDto = mapDtoObject(userService.updateUser(userId, userDto));
        return ResponseEntity.ok(updatedUserDto);
    }

    @Override
    public ResponseEntity<Void> delete(Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    private UserDto mapDtoObject(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }
}
