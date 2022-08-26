package com.flameking.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReportDto {
    @NotBlank
    private Integer to_id;
    @NotBlank
    private String content;
    @NotBlank
    private Integer type;
}
