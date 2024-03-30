package com.hatrongtan99.taskExam.service.impl;

import com.hatrongtan99.taskExam.dto.authDto.ChangeRoleRequestDto;
import com.hatrongtan99.taskExam.dto.authDto.NewUserRequestDto;
import com.hatrongtan99.taskExam.dto.authDto.UpdateUserRequest;
import com.hatrongtan99.taskExam.entity.AuthorityEntity;
import com.hatrongtan99.taskExam.entity.UserEntity;
import com.hatrongtan99.taskExam.exception.BadRequestException;
import com.hatrongtan99.taskExam.exception.ConflictException;
import com.hatrongtan99.taskExam.exception.NotFoundException;
import com.hatrongtan99.taskExam.repository.AuthorityRepo;
import com.hatrongtan99.taskExam.repository.UserRepo;
import com.hatrongtan99.taskExam.service.IUserService;
import com.hatrongtan99.taskExam.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final static String DEFAULT_ROLE = "STUDENT";
    private final AuthorityRepo authorityRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @Value("${auth.user.default-password}")
    private String defaultPassword;

    @Override
    public UserEntity findById(String userId) {
        return this.userRepo.findByIdAndIsActiveIsTrue(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }
    @Override
    public UserEntity findByEmailOrThrow(String email) {
        return this.userRepo.findByEmailAndIsActiveIsTrue(email).orElseThrow(
                () -> new NotFoundException("user " + email + " not found")
        );
    }

    @Override
    public Page<UserEntity> getListUser(String roleName, PageRequest pageRequest) {
        return this.userRepo.findAll(roleName, pageRequest);
    }

    @Override
    @Transactional
    public UserEntity saveNewUser(NewUserRequestDto body) {
        UserEntity exitsUser = this.userRepo.findByEmail(body.email()).orElse(null);
        if (exitsUser != null) {
            if (exitsUser.getIsActive()) {
                throw new ConflictException("User: '" + body.email() + "' already exist");
            } else {
                exitsUser.setIsActive(true);
                exitsUser.setFullname(body.fullname());
                exitsUser.setPassword(this.passwordEncoder.encode(defaultPassword));
                return this.userRepo.save(exitsUser);
            }
        }
        AuthorityEntity defaultRole = this.getAuthorityByNameOrThrow(DEFAULT_ROLE);
        UserEntity newUser = UserEntity.builder()
                .fullname(body.fullname())
                .email(body.email())
                .authorities(defaultRole)
                .password(this.passwordEncoder.encode(defaultPassword))
                .isActive(true)
                .build();
        return this.userRepo.save(newUser);
    }

    @Override
    @Transactional
    public UserEntity updateUser(UpdateUserRequest body) {
        UserEntity user = this.findById(body.id());
        user.setEmail(body.email());
        if (body.password() != null) {
            user.setPassword(this.passwordEncoder.encode(body.password()));
        }
        user.setFullname(body.fullname());
        return this.userRepo.save(user);
    }

    @Override
    @Transactional
    public void changeRoleUser(ChangeRoleRequestDto body) {
        UserEntity user = this.findByEmailOrThrow(body.email());
        String currentUserId = CommonUtils.getCurrentUserId();
        if (user.getId().equals(currentUserId)) {
            throw new BadRequestException("Can't change role of yourself");
        }
        AuthorityEntity role = this.getAuthorityByNameOrThrow(body.roleName());
        user.setAuthorities(role);
    }

    @Override
    @Transactional
    public void removeUser(String email) {
        UserEntity user = this.findByEmailOrThrow(email);
        user.setIsActive(false);
    }

    private AuthorityEntity getAuthorityByNameOrThrow(String name) {
        return this.authorityRepo.findByName(name).orElseThrow(
                () -> new NotFoundException("Authority: " + name + " not found")
        );
    }
}
