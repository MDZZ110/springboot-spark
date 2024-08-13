package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class IntersectionJobResult extends Response {
    @Getter
    @JsonProperty("distributedDataset")
    private List<Integer> distributedDataset;

    public IntersectionJobResult() {}

    public IntersectionJobResult(int taskStatus, List<Integer> distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static IntersectionJobResult getResponse(ErrorCodeEnum errorCodeEnum, List<Integer> result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new IntersectionJobResult(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new IntersectionJobResult(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static IntersectionJobResult getResponse(String respJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(respJson, IntersectionJobResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return IntersectionJobResult.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }
}
