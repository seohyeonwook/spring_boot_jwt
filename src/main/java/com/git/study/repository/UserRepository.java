package com.git.study.repository;

import com.git.study.dto.UserDTO;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    // c = 그냥 기본으로 한번 만들어보기
    static public ArrayList<UserDTO> users;

    static {
        users = new ArrayList<>();
        users.add(new UserDTO("kang", "test1","1234"));
        users.add(new UserDTO("park", "test2","1234"));
        users.add(new UserDTO("kim", "test3","1234"));
    }

    public UserDTO insertUser(UserDTO userDTO) {
        users.add(userDTO);
        return userDTO;
    }

    // r
    public List<UserDTO> getAllUsers() {
        return users;
    }

    public UserDTO getUserByUserId(String userId) {
        return users.stream()
                .filter(userDTO -> userDTO.getUserId().equals(userId)) // 찾고
                .findAny() // 있으면
                .orElse(new UserDTO("","","")); // 없으면
    }

    // u
    public void updateUserPw(String userId, UserDTO userDTO) {
        users.stream()
                .filter(userDTO1 -> userDTO1.getUserId().equals(userId) )
                .findAny()
                .orElse(new UserDTO("", "", ""))
                .setUserPw(userDTO.getUserPw());
    }

    // d
    public void deleteUser(String userId) {
        users.removeIf(userDTO -> userDTO.getUserId().equals(userId));
    }
}
