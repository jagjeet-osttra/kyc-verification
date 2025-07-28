package com.osttra.alpine.dtos;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskCompletionRequestDto {
    private String taskId;
    private String processId;
    Map<String,Object> formData;
}
