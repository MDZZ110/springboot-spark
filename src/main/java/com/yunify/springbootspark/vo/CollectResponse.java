package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;


public class CollectResponse extends Response {
    @JsonProperty("result")
    private List<String> result;

    public CollectResponse() {}

    public CollectResponse(int taskStatus, List<String> result, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.result = result;
    }

    public static CollectResponse getResponse(ErrorCodeEnum errorCodeEnum, List<String> result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new CollectResponse(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new CollectResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static CollectResponse getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, CollectResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return CollectResponse.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }

    @Override
    public String toString() {
        return String.format("CollectResponse{taskStatus=%s, result=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), result, this.getErrorCode(), this.getErrorMsg());
    }
}
