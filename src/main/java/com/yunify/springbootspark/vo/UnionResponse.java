package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnionResponse extends Response {
    @JsonProperty("distributedDataset")
    private String distributedDataset;

    public UnionResponse() {}


    public UnionResponse(int taskStatus, String distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static UnionResponse getResponse(ErrorCodeEnum errorCodeEnum, String result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new UnionResponse(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new UnionResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }


    @Override
    public String toString() {
        return String.format("UnionResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
