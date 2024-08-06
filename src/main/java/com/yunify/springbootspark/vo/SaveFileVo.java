package com.yunify.springbootspark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SaveFileVo {

    private String distributedDataset;

    private String fileType;

    private String filePath;

    private final String methodName = "saveFile";
}
