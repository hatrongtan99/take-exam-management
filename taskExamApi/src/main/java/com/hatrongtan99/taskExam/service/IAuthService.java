package com.hatrongtan99.taskExam.service;

import com.hatrongtan99.taskExam.dto.authDto.*;
import com.hatrongtan99.taskExam.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public interface IAuthService extends LogoutHandler  {
    LoginResponseDto login(LoginRequestDto body);

}
