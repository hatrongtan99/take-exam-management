package com.hatrongtan99.taskExam.api;

import com.hatrongtan99.taskExam.dto.ExamQuestionDto.DetailExamQuestionResponseDto;
import com.hatrongtan99.taskExam.dto.ExamQuestionDto.ExamQuestionResponseDto;
import com.hatrongtan99.taskExam.dto.ExamQuestionDto.ExamQuestionSaveDto;
import com.hatrongtan99.taskExam.dto.MetadataResponseDto;
import com.hatrongtan99.taskExam.dto.PageableResponseDto;
import com.hatrongtan99.taskExam.dto.ResponseBody;
import com.hatrongtan99.taskExam.entity.ExamQuestionEntity;
import com.hatrongtan99.taskExam.service.IExamQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam-question")
@RequiredArgsConstructor
@Tag(name = "Questions API")
public class ExamQuestionApi {

    private final IExamQuestionService examQuestionService;

    @Operation(summary = "Danh sách các câu hỏi (Teacher's access)")
    @GetMapping
    public ResponseEntity<PageableResponseDto<ExamQuestionResponseDto>> getListQuestion(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "8") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<ExamQuestionEntity> pageResult = this.examQuestionService.getList(pageRequest);
        List<ExamQuestionResponseDto> records = pageResult.getContent().stream().map(ExamQuestionResponseDto::mapToDto).toList();
        return ResponseEntity.ok(new PageableResponseDto<>(
                records,
                MetadataResponseDto.mapToDto(pageResult)
        ));
    }

    @Operation(summary = "Chi tiết câu hỏi và câu trả lời (Teacher's access)")
    @GetMapping("/{questionId}")
    public ResponseEntity<DetailExamQuestionResponseDto> getDetail(
            @PathVariable(name = "questionId") String questionId
    ) {
        ExamQuestionEntity question = this.examQuestionService.getDetailById(questionId);
        return ResponseEntity.ok(DetailExamQuestionResponseDto.mapToDto(question, true));
    }

    @Operation(summary = "Tạo mới câu hỏi và câu trả lời (Teacher's access)")
    @PostMapping()
    public ResponseEntity<DetailExamQuestionResponseDto> createQuestion(
            @Valid @RequestBody ExamQuestionSaveDto body
    ) {
        ExamQuestionEntity question = this.examQuestionService.createNew(body);
        return ResponseEntity.ok(DetailExamQuestionResponseDto.mapToDto(question, true));
    }

    @Operation(summary = "Chỉnh sửa câu hỏi và câu trả lời (Teacher's access)")
    @PatchMapping("/{questionId}")
    public ResponseEntity<DetailExamQuestionResponseDto> updateQuestion(
            @PathVariable("questionId") String questionId,
            @Valid @RequestBody ExamQuestionSaveDto body
    ) {
        ExamQuestionEntity question = this.examQuestionService.update(questionId, body);
        return ResponseEntity.ok(DetailExamQuestionResponseDto.mapToDto(question, true));
    }

    @Operation(summary = "Xoá câu hỏi và câu trả lời (Teacher's access)")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<ResponseBody> deleteQuestion(
            @PathVariable("questionId") String questionId
    ) {
        this.examQuestionService.deleteById(questionId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseBody(
                        HttpStatus.ACCEPTED,
                        "Delete successfully"
                ));
    }
}
