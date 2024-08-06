package com.yunify.springbootspark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SampleVo {
    private String distributedDataset;

    private String replace;

    private String percentage;

    private String randomSeed;

    private final String methodName = "sample";
}
