package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CountResponse extends Response {
    @JsonProperty("totalNum")
    private int totalNum;

    public CountResponse() {}

    public CountResponse(int taskStatus, int totalNum, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.totalNum = totalNum;
    }

    public static CountResponse getResponse(ErrorCodeEnum errorCodeEnum, int totalNum){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new CountResponse(
                    Response.TASK_STATUS_SUCCESS,
                    totalNum,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new CountResponse(
                Response.TASK_STATUS_FAILED,
                0,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static CountResponse getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, CountResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return CountResponse.getResponse(ErrorCodeEnum.FAILED, 0);
        }
    }

    @Override
    public String toString() {
        return String.format("CountResponse{taskStatus=%s, totalNum=%d, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), totalNum, this.getErrorCode(), this.getErrorMsg());
    }
}
