package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunify.springbootspark.constants.constants;
import com.yunify.springbootspark.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ReduceByKeyVo {

    private String distributedDataset;

    private String reduceFunction;

    private String keyField;

    private final String methodName = "reduceByKey";

    public String getDatasetJson() throws JsonProcessingException {
        String filePath = constants.INPUT_FILE_DIR + "/" + distributedDataset;
        List<String> dataset = CommonUtil.readFileAsInput(filePath);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dataset);
    }

    public String getMidFileName() {
        return methodName + "-" + distributedDataset;
    }
}
