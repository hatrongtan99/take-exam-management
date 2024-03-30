package com.hatrongtan99.taskExam.security;

import com.hatrongtan99.taskExam.entity.UserEntity;
import com.hatrongtan99.taskExam.exception.NotFoundException;
import com.hatrongtan99.taskExam.repository.UserRepo;
import com.hatrongtan99.taskExam.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepo.findByEmailAndIsActiveIsTrue(email).orElseThrow(
                () -> new NotFoundException("User: '" + email + "' not found")
        );
        return UserPrincipal.create(userEntity);
    }
}
