package com.yunify.springbootspark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SortByKeyVo {

    private String distributedDataset;

    private String sort;

    private String keyField;

    private final String methodName = "sortByKey";

}
