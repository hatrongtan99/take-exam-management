package com.hatrongtan99.taskExam.api;

import com.hatrongtan99.taskExam.dto.MetadataResponseDto;
import com.hatrongtan99.taskExam.dto.PageableResponseDto;
import com.hatrongtan99.taskExam.dto.ResponseBody;
import com.hatrongtan99.taskExam.dto.authDto.*;
import com.hatrongtan99.taskExam.entity.UserEntity;
import com.hatrongtan99.taskExam.service.IAuthService;
import com.hatrongtan99.taskExam.service.IUserService;
import com.hatrongtan99.taskExam.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API")
public class AuthApi {

    private final IAuthService authService;
    private final IUserService userService;

    @Value("${auth.user.default-password}")
    private String defaultPassword;

    @Operation(summary = "Đăng nhập")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto body
    ) {
        return ResponseEntity.ok(this.authService.login(body));
    }

    @Operation(summary = "Đăng xuất")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
       this.authService.logout(request, response, authentication);
    }

    @Operation(summary = "Thông tin cá nhân")
    @GetMapping("/me")
    public ResponseEntity<UserDetailResponse> getInfo() {
        String userId = CommonUtils.getCurrentUserId();
        UserEntity user = this.userService.findById(userId);
        return ResponseEntity.ok(UserDetailResponse.mapToDto(user));
    }

    @Operation(summary = "Lấy danh sách người dùng (Teacher's access)")
    @GetMapping("/manage/members")
    public ResponseEntity<PageableResponseDto<UserDetailResponse>> getList(
            @RequestParam(name = "roleName", required = false) String roleName,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "8") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<UserEntity> pageResponse = this.userService.getListUser(roleName, pageRequest);
        List<UserDetailResponse> records = pageResponse.getContent().stream().map(UserDetailResponse::mapToDto).toList();
        return ResponseEntity.ok(
                new PageableResponseDto<>(
                        records,
                        MetadataResponseDto.mapToDto(pageResponse)
                )
        );
    }

    @Operation(summary = "Tạo mới người dùng (Teacher's access)")
    @PostMapping("/manage")
    public ResponseEntity<NewUserResponseDto> createNew(
            @Valid @RequestBody NewUserRequestDto body
    ) {
        UserEntity user = this.userService.saveNewUser(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new NewUserResponseDto(
                        user.getId(),
                        user.getFullname(),
                        user.getEmail(),
                        user.getAuthorities().getName(),
                        defaultPassword,
                        user.getIsActive()
                )
        );
    }

    @Operation(summary = "Thay đổi role (Teacher's access)")
    @PatchMapping("/manage")
    public ResponseEntity<ResponseBody> changeRole(
            @Valid @RequestBody ChangeRoleRequestDto body
    ) {
        this.userService.changeRoleUser(body);
        return ResponseEntity.ok(
                new ResponseBody(
                        HttpStatus.ACCEPTED,
                        "Change role successfully"
                )
        );
    }

    @Operation(summary = "Chỉnh sửa người dùng (Teacher's access)")
    @PutMapping("/manage")
    ResponseEntity<UserDetailResponse> updateUser(
            @Valid @RequestBody UpdateUserRequest body
    ) {
        UserEntity user = this.userService.updateUser(body);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(UserDetailResponse.mapToDto(user));
    }

    @Operation(summary = "Xoá người dùng (Teacher's access)")
    @DeleteMapping("/manage/{email}")
    public ResponseEntity<ResponseBody> deleteUser(
            @PathVariable String email
    ) {
        this.userService.removeUser(email);
        return ResponseEntity.ok(
                new ResponseBody(
                        HttpStatus.ACCEPTED,
                        "Delete user successfully"
                )
        );
    }
}
