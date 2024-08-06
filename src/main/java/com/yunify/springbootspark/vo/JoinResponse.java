package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JoinResponse extends Response {
    @JsonProperty("distributedDataset")
    private Object distributedDataset;

    public JoinResponse() {}

    public JoinResponse(int taskStatus, Object distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static JoinResponse getResponse(ErrorCodeEnum errorCodeEnum, Object result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new JoinResponse(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new JoinResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static JoinResponse getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, JoinResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return JoinResponse.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }

    @Override
    public String toString() {
        return String.format("JoinResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
