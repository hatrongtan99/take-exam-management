package com.hatrongtan99.taskExam.api;

import com.hatrongtan99.taskExam.dto.MetadataResponseDto;
import com.hatrongtan99.taskExam.dto.PageableResponseDto;
import com.hatrongtan99.taskExam.dto.TakeExamDto.TakeExamResponseDto;
import com.hatrongtan99.taskExam.entity.TakeExamEntity;
import com.hatrongtan99.taskExam.service.IReadTakeExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Tag(name = "History Take Exam API")
public class ManageHistoryTakeExam {
    private final IReadTakeExamService readTakeExamService;

    @Operation(summary = "Danh sách lịch sử theo Topic (Teacher's access)")
    @GetMapping("/{examTopicId}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<PageableResponseDto<TakeExamResponseDto>> getListByTopicId(
            @PathVariable("examTopicId") String examTopicId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "8") int limit,
            @Parameter(description = "sort (ASC, DESC), range(number-number)")
            @RequestParam() Map<String, String> allRequestPrams
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<TakeExamEntity> pageResponse = this.readTakeExamService.getListByTopic(examTopicId, allRequestPrams, pageable);
        return ResponseEntity.ok(this.responseDto(pageResponse));
    }

    @Operation(summary = "Danh sách lịch sử của học sinh", description = "Role Student chỉ xem được của bản thân, Role TEACHER xem được tất cả")
    @GetMapping("/student/{studentId}")
    @PreAuthorize("(authentication.principal.id == #studentId and hasAuthority('STUDENT')) or hasAuthority('TEACHER')")
    public ResponseEntity<PageableResponseDto<TakeExamResponseDto>> getListOfStudent(
            @PathVariable("studentId") String studentId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "8") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<TakeExamEntity> pageResponse = this.readTakeExamService.getListByUserId(studentId, pageable);
        return ResponseEntity.ok(this.responseDto(pageResponse));
    }

    private PageableResponseDto<TakeExamResponseDto> responseDto(Page<TakeExamEntity> pageResponse) {
        List<TakeExamResponseDto> records = pageResponse.getContent().stream().map(TakeExamResponseDto::mapToDto).toList();
        return new PageableResponseDto<>(records, MetadataResponseDto.mapToDto(pageResponse));
    }
}
