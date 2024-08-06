package com.yunify.springbootspark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class JoinVo {

    private String distributedDataset1;

    private String distributedDataset2;

    private int joinMethod;

    private final String methodName = "join";
}
