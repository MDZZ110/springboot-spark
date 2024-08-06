package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ActionEntryResponse extends Response {
    @JsonProperty("distributedDataset")
    private Object distributedDataset;

    public ActionEntryResponse() {}

    public ActionEntryResponse(int taskStatus, Object distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static ActionEntryResponse getResponse(ErrorCodeEnum errorCodeEnum, Object distributedDataset){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new ActionEntryResponse(
                    Response.TASK_STATUS_SUCCESS,
                    distributedDataset,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new ActionEntryResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static ActionEntryResponse getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, ActionEntryResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return ActionEntryResponse.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }

    @Override
    public String toString() {
        return String.format("ActionEntryResponse{taskStatus=%s, result=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
