package com.yunify.springbootspark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CountVo {

    private String distributedDataset;

    private final String methodName = "count";
}
