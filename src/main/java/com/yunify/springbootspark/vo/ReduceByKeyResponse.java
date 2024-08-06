package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import scala.Tuple2;

import java.io.IOException;
import java.util.List;

public class ReduceByKeyResponse extends Response {
    @JsonProperty("distributedDataset")
    private List<Tuple2<String, String>> distributedDataset;

    public ReduceByKeyResponse() {}

    public ReduceByKeyResponse(int taskStatus, List<Tuple2<String, String>> distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static ReduceByKeyResponse getResponse(ErrorCodeEnum errorCodeEnum, List<Tuple2<String, String>> distributedDataset){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new ReduceByKeyResponse(
                    Response.TASK_STATUS_SUCCESS,
                    distributedDataset,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new ReduceByKeyResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static ReduceByKeyResponse getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, ReduceByKeyResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return ReduceByKeyResponse.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }

    @Override
    public String toString() {
        return String.format("ReduceByKeyResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
