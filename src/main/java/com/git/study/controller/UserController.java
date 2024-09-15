package com.git.study.controller;

import com.git.study.aspect.TokenRequired;
import com.git.study.dto.UserDTO;
import com.git.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    // CRUD
    // C -> POST
    // R -> GET
    // U -> PUT
    // D -> DELETE


    @PostMapping("")
    public UserDTO insertUser(@RequestBody UserDTO userDTO) { // json
        return userService.insertUser(userDTO);
    }

    @GetMapping("") //같은 url로 다른 메소드를 날려서 다양한 서비스 구현 가능
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @TokenRequired
    @GetMapping("/{userId}")
    public UserDTO getUserByUserId(@PathVariable String userId) { // 단건조회 PathVariable
        return userService.getUserByUserId(userId);
    }


    @PutMapping("/{userId}")
    public void updateUserPw(@PathVariable String userId, @RequestBody UserDTO userDTO) { // 이거 잘 생각하자
        userService.updateUserPw(userId, userDTO);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }
}
