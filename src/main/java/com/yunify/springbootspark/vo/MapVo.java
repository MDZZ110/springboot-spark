package com.yunify.springbootspark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MapVo {

    private String distributedDataset;

    private String userDefinedFunction;

    private final String methodName = "map";
}
