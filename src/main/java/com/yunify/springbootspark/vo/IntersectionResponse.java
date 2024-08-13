package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


public class IntersectionResponse extends Response {
    @Getter
    @JsonProperty("distributedDataset")
    private String distributedDataset;

    public IntersectionResponse() {}

    public IntersectionResponse(int taskStatus, String distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static IntersectionResponse getResponse(ErrorCodeEnum errorCodeEnum, String result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new IntersectionResponse(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new IntersectionResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    @Override
    public String toString() {
        return String.format("IntersectionResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
