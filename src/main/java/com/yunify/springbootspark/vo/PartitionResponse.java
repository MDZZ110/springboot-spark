package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PartitionResponse extends Response {
    @JsonProperty("distributedDataset")
    private Object distributedDataset;

    public PartitionResponse() {}

    public PartitionResponse(int taskStatus, Object distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static PartitionResponse getResponse(ErrorCodeEnum errorCodeEnum, Object result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new PartitionResponse(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new PartitionResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static PartitionResponse getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, PartitionResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return PartitionResponse.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }

    @Override
    public String toString() {
        return String.format("PartitionResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
