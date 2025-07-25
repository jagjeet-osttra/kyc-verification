package com.osttra.alpine.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskCompletionRequestDto {
    private String taskId;
    private String processId;
    Map<String,Object> formData;
}
