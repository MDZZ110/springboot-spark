package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunify.springbootspark.constants.constants;
import com.yunify.springbootspark.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MapVo {

    private String distributedDataset;

    private String userDefinedFunction;

    private final String methodName = "map";

    public String getDatasetJson() throws JsonProcessingException {
        String filePath = constants.INPUT_FILE_DIR + "/" + distributedDataset;
        List<String> stringList = CommonUtil.readFileAsInput(filePath);
        List<Integer> dataset = new ArrayList<>();
        for(String str: stringList) {
            dataset.add(Integer.parseInt(str));
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dataset);
    }

    public String getMidFileName() {
        return methodName + "-" + distributedDataset;
    }
}
