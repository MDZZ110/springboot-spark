package com.yunify.springbootspark.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunify.springbootspark.constants.constants;
import com.yunify.springbootspark.util.CommonUtil;

import java.io.IOException;
import java.util.List;

public class SaveFileResponse extends Response {
    @JsonProperty("distributedDataset")
    private List<?> distributedDataset;

    public SaveFileResponse() {}

    public SaveFileResponse(int taskStatus, List<?> distributedDataset, int errorCode, String errorMsg){
        super(taskStatus, errorCode, errorMsg);
        this.distributedDataset = distributedDataset;
    }

    public static SaveFileResponse getResponse(ErrorCodeEnum errorCodeEnum, List<?> distributedDataset){
        if(errorCodeEnum == ErrorCodeEnum.SUCCESS){
            return new SaveFileResponse(
                    Response.TASK_STATUS_SUCCESS,
                    distributedDataset,
                    ErrorCodeEnum.SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SUCCESS.getErrorMsg()
            );
        }

        return new SaveFileResponse(
                Response.TASK_STATUS_FAILED,
                null,
                errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMsg()
        );
    }

    public static SaveFileResponse getResponse(String respJson, String datasetName){
        try {
            ObjectMapper mapper = new ObjectMapper();
            SaveFileResponse resp = mapper.readValue(respJson, SaveFileResponse.class);
            String filePath = constants.INPUT_FILE_DIR + "/" + datasetName;
            resp.distributedDataset = CommonUtil.readFileAsInput(filePath);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
            return SaveFileResponse.getResponse(ErrorCodeEnum.FAILED, null);
        }
    }

    @Override
    public String toString() {
        return String.format("SaveFileResponse{taskStatus=%s, distributedDataset=%s, errorCode=%s, errorMsg=%s}",
                this.getTaskStatus(), distributedDataset, this.getErrorCode(), this.getErrorMsg());
    }
}
