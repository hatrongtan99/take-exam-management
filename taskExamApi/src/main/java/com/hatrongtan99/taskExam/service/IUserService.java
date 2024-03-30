package com.hatrongtan99.taskExam.service;

import com.hatrongtan99.taskExam.dto.authDto.ChangeRoleRequestDto;
import com.hatrongtan99.taskExam.dto.authDto.NewUserRequestDto;
import com.hatrongtan99.taskExam.dto.authDto.UpdateUserRequest;
import com.hatrongtan99.taskExam.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IUserService {
    UserEntity findById(String userId);
    UserEntity findByEmailOrThrow(String email);
    Page<UserEntity> getListUser(String roleName, PageRequest pageRequest);

    UserEntity saveNewUser(NewUserRequestDto body);
    UserEntity updateUser(UpdateUserRequest body);
    void changeRoleUser(ChangeRoleRequestDto body);
    void removeUser(String email);
}
