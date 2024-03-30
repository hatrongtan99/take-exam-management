package com.hatrongtan99.taskExam.service.impl;

import com.hatrongtan99.taskExam.dto.authDto.*;
import com.hatrongtan99.taskExam.entity.AuthorityEntity;
import com.hatrongtan99.taskExam.entity.UserEntity;
import com.hatrongtan99.taskExam.exception.BadRequestException;
import com.hatrongtan99.taskExam.exception.ConflictException;
import com.hatrongtan99.taskExam.exception.NotFoundException;
import com.hatrongtan99.taskExam.repository.AuthorityRepo;
import com.hatrongtan99.taskExam.repository.UserRepo;
import com.hatrongtan99.taskExam.security.JwtService;
import com.hatrongtan99.taskExam.security.UserPrincipal;
import com.hatrongtan99.taskExam.service.IAuthService;
import com.hatrongtan99.taskExam.service.IUserService;
import com.hatrongtan99.taskExam.utils.CommonUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final JwtService jwtService;
    private final HttpServletResponse httpServletResponse;

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto login(LoginRequestDto body) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(body.username(), body.password());
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);
        Cookie cookie = new Cookie("Authorization",  token);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return  new LoginResponseDto(
                principal.getId(),
                principal.getFullname(),
                principal.getEmail(),
                token,
                principal.getIsActive()
        );
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.clearContext();
        Cookie authCookie = new Cookie("Authorization", null);
        authCookie.setPath("/");
        authCookie.setMaxAge(0);
        response.addCookie(authCookie);
    }

}
