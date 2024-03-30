package com.hatrongtan99.taskExam.api;

import com.hatrongtan99.taskExam.dto.TakeExamDto.TakeExamRequestDto;
import com.hatrongtan99.taskExam.dto.TakeExamDto.TakeExamResponseDto;
import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicDetailResponse;
import com.hatrongtan99.taskExam.entity.TakeExamEntity;
import com.hatrongtan99.taskExam.service.IStudentTakeExamService;
import com.hatrongtan99.taskExam.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/take-exam")
@RequiredArgsConstructor
@Tag(name = "Take Exam API")
public class TakeExamApi {
    private final IStudentTakeExamService studentTakeExamService;

    @Operation(summary = "Bắt đầu làm bài kiểm tra")
    @PostMapping("/{examTopicId}/start")
    public ResponseEntity<TakeExamResponseDto> startTakeExam(
            @PathVariable("examTopicId") String examTopicId
    ) {
        String studentId = CommonUtils.getCurrentUserId();
        TakeExamEntity takeExam = this.studentTakeExamService.startCreateTakeExam(studentId, examTopicId);
        return ResponseEntity.ok(TakeExamResponseDto.mapToDto(takeExam));
    }

    @Operation(summary = "Nộp bài kiểm tra")
    @PostMapping("/submit/{takeExamId}")
    public ResponseEntity<TakeExamResponseDto> submitExam(
            @PathVariable("takeExamId") String examTopicId,
            @Valid @RequestBody TakeExamRequestDto body
    ) {
        String studentId = CommonUtils.getCurrentUserId();
        TakeExamEntity takeExam = this.studentTakeExamService.submitExam(studentId, examTopicId, body);
        return ResponseEntity.ok(TakeExamResponseDto.mapToDto(takeExam));
    }

    @Operation(summary = "Danh sách các câu hỏi trong bài kiểm tra")
    @GetMapping("/question/{takeExamId}")
    public ResponseEntity<ExamTopicDetailResponse> getQuestion(
            @PathVariable("takeExamId") String takeExamId
    ) {
        return ResponseEntity.ok(this.studentTakeExamService.getTakeExamQuestion(takeExamId));
    }
}
