package com.yunify.springbootspark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TransformationEntryVo {

    private String distributedDataset;

    private int function;

    private String parameterList;

    private final String methodName = "transformationEntry";

}
