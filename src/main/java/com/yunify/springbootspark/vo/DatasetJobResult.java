package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class DatasetJobResult extends Response {
    @Getter
    @JsonProperty("distributedDataset")
    private List<?> distributedDataset;

    public DatasetJobResult() {}

    public DatasetJobResult(int taskStatus, List<?> distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static DatasetJobResult getResult(ErrorCodeEnum errorCodeEnum, List<?> result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new DatasetJobResult(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new DatasetJobResult(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static DatasetJobResult getResult(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, DatasetJobResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return DatasetJobResult.getResult(ErrorCodeEnum.FAILED, null);
        }
    }
}
