package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class TakeResponse extends Response {
    @JsonProperty("distributedDataset")
    private List<?> distributedDataset;

    public TakeResponse() {}

    public TakeResponse(int taskStatus, List<?> distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static TakeResponse getResponse(ErrorCodeEnum errorCodeEnum, List<?> result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new TakeResponse(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new TakeResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static TakeResponse getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, TakeResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return TakeResponse.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }

    @Override
    public String toString() {
        return String.format("TakeResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
