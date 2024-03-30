package com.hatrongtan99.taskExam.api;

import com.hatrongtan99.taskExam.dto.ExamQuestionDto.DetailExamQuestionResponseDto;
import com.hatrongtan99.taskExam.dto.MetadataResponseDto;
import com.hatrongtan99.taskExam.dto.PageableResponseDto;
import com.hatrongtan99.taskExam.dto.ResponseBody;
import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicDetailResponse;
import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicResponseDto;
import com.hatrongtan99.taskExam.dto.examTopicDto.ExamTopicSaveRequestDto;
import com.hatrongtan99.taskExam.entity.ExamTopicEntity;
import com.hatrongtan99.taskExam.service.IExamTopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam-topics")
@RequiredArgsConstructor
@Tag(name = "Topic API")
public class ExamTopicsApi {

    private final IExamTopicService examTopicService;

    @Operation(summary = "Danh sách các bài kiểm tra")
    @GetMapping()
    public ResponseEntity<PageableResponseDto<ExamTopicResponseDto>> getListTopic(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "8") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<ExamTopicEntity> pageResult = this.examTopicService.getList(pageRequest);
        List<ExamTopicResponseDto> records = pageResult.getContent().stream().map(ExamTopicResponseDto::mapToDto).toList();

        return ResponseEntity.ok(
                new PageableResponseDto<>(
                        records,
                        MetadataResponseDto.mapToDto(pageResult)
                )
        );
    }

    @Operation(summary = "Chi tiết bài kiểm tra và có đáp án (Teacher's access)")
    @GetMapping("/{topicId}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ExamTopicDetailResponse> getDetailTopic(
            @PathVariable("topicId") String topicId
    ) {
        ExamTopicEntity examTopic = this.examTopicService.getDetail(topicId);
        return ResponseEntity.ok(this.getDetailResponse(examTopic));
    }


    @Operation(summary = "Tạo mới bài kiểm tra (Teacher's access)")
    @PostMapping()
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ExamTopicDetailResponse> createNewTopic(
            @Valid @RequestBody ExamTopicSaveRequestDto body
    ) {
        ExamTopicEntity examTopic = this.examTopicService.createNew(body);
        return ResponseEntity.ok(this.getDetailResponse(examTopic));
    }

    @Operation(summary = "Chỉnh sửa bài kiểm tra (Teacher's access)")
    @PatchMapping("/{topicId}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ExamTopicDetailResponse> updateExamTopic(
            @PathVariable(name = "topicId") String topicId,
            @Valid @RequestBody ExamTopicSaveRequestDto body
    ) {
        ExamTopicEntity examTopic = this.examTopicService.update(topicId, body);
        return ResponseEntity.ok(this.getDetailResponse(examTopic));
    }

    @Operation(summary = "Xoá bài kiểm tra (Teacher's access)")
    @DeleteMapping("/{topicId}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResponseBody> deleteExamTopic(
            @PathVariable(name = "topicId") String topicId
    ) {
        this.examTopicService.removeById(topicId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseBody(
                        HttpStatus.ACCEPTED,
                        "Delete successfully"
                ));
    }

    private ExamTopicDetailResponse getDetailResponse(ExamTopicEntity entity) {
       return new ExamTopicDetailResponse(
               entity.getId(),
               entity.getCode(),
               entity.getTitle(),
               entity.getQuestions().stream().map(question -> DetailExamQuestionResponseDto.mapToDto(question, true)).toList(),
               entity.getQuestionNumber(),
               entity.getTimeExpires().toMinutes(),
               entity.getIsActive()
       );
    }
}
