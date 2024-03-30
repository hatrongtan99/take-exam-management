package com.hatrongtan99.taskExam.utils;

import com.hatrongtan99.taskExam.entity.UserEntity;
import com.hatrongtan99.taskExam.exception.RequiredSignInException;
import com.hatrongtan99.taskExam.security.UserPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Random;

public class CommonUtils {
    private static final int LENGTH_CODE = 4;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

    public static String generateCode() {
        return generateCode(LENGTH_CODE);
    }

    public static String generateCode(Integer lengthCode) {
        if (lengthCode < LENGTH_CODE) {
            lengthCode = LENGTH_CODE;
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lengthCode; i++) {
            sb.append(CHARACTERS[random.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new RequiredSignInException("require sign in before process");
        }
        return ((UserPrincipal) authentication.getPrincipal()).getId();
    }

    public static boolean validateTimeIsExpires(Duration timeExpires, Temporal start, Temporal end) {
        return timeExpires.toMinutes() - Duration.between(start, end).toMinutes() <= 0;
    }
}
