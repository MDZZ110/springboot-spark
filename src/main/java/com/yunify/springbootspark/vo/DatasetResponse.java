package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunify.springbootspark.constants.constants;
import com.yunify.springbootspark.util.CommonUtil;

import java.io.IOException;

public class DatasetResponse extends Response {
    @JsonProperty("distributedDataset")
    private String distributedDataset;

    public DatasetResponse() {}


    public DatasetResponse(int taskStatus, String distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static DatasetResponse getResponse(DatasetJobResult jobResult, String fileName) throws IOException {
        String filePath = constants.INPUT_FILE_DIR + "/" + fileName;
        CommonUtil.writeListToFile(jobResult.getDistributedDataset(), filePath);
        return new DatasetResponse(
                Response.TASK_STATUS_SUCCESS,
                fileName,
                ErrorCodeEnum.SUCCESS.getErrorCode(),
                ErrorCodeEnum.SUCCESS.getErrorMsg()
        );
    }

    public static DatasetResponse getResponse(ErrorCodeEnum errorCodeEnum, String result){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new DatasetResponse(
                    Response.TASK_STATUS_SUCCESS,
                    result,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new DatasetResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }


    @Override
    public String toString() {
        return String.format("DatasetResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
