package com.git.study.service;

import com.git.study.dto.UserDTO;
import com.git.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserDTO insertUser(UserDTO userDTO) {
        return userRepository.insertUser(userDTO);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public UserDTO getUserByUserId(String userId) {
        return userRepository.getUserByUserId(userId);
    }

    public void updateUserPw(String userId, UserDTO userDTO) {
        userRepository.updateUserPw(userId, userDTO);
    }

    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
    }

}
