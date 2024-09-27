package com.allclear.tastytrack.domain.user.service;

import org.springframework.http.HttpHeaders;

import com.allclear.tastytrack.domain.user.dto.LoginRequest;
import com.allclear.tastytrack.domain.user.dto.UserCreateRequest;
import com.allclear.tastytrack.domain.user.dto.UserInfo;
import com.allclear.tastytrack.domain.user.dto.UserUpdateRequest;
import com.allclear.tastytrack.domain.user.entity.User;

public interface UserService {

    void signup(UserCreateRequest userCreateRequest);

    HttpHeaders signin(LoginRequest loginRequest);

    UserInfo updateUserInfo(String username, UserUpdateRequest userUpdateRequest);

    UserInfo getUserInfo(String username);

    User userCheck(String username);

}
