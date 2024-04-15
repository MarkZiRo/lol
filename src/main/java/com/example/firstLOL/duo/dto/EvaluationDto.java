package com.example.firstLOL.duo.dto;

import com.example.firstLOL.duo.entity.Evaluation;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {

    private Long id;

    @Setter
    //평가 하는 사람
    private Long evaluator;
    @Setter
    //평가 받는 사람
    private Long beingEvaluated;
    @Setter
    private String status;
    @Setter
    private Long postId;
    @Setter
    @CreationTimestamp
    private LocalDateTime createdAt;

    public static EvaluationDto fromEntity(Evaluation entity){
        EvaluationDtoBuilder builder = EvaluationDto.builder()
                .id(entity.getId())
                .evaluator(entity.getEvaluator())
                .beingEvaluated(entity.getBeingEvaluated())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .postId(entity.getPostId());
        return builder.build();
    }
}
