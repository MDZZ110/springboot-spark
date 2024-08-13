package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.util.List;


public class UnionJobResult extends Response {
    @Getter
    @JsonProperty("distributedDataset")
    private List<Integer> distributedDataset;

    public UnionJobResult() {}

    public UnionJobResult(int taskStatus, List<Integer> distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static UnionJobResult getResponse(ErrorCodeEnum errorCodeEnum, List<Integer> result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new UnionJobResult(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new UnionJobResult(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static UnionJobResult getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, UnionJobResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return UnionJobResult.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }
}
