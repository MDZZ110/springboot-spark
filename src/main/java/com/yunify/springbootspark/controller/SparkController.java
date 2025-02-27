package com.yunify.springbootspark.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunify.springbootspark.constants.constants;
import com.yunify.springbootspark.entity.SparkApplicationParam;
import com.yunify.springbootspark.service.ISparkSubmitService;
import com.yunify.springbootspark.util.CommonUtil;
import com.yunify.springbootspark.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author yunify
 **/
@Slf4j
@Controller
public class SparkController {
	@Resource
	private ISparkSubmitService iSparkSubmitService;

	private SparkApplicationParam params;

	@Autowired
	public void setSparkApplicationParam(SparkApplicationParam params) {
		this.params = params;
	}
	/**
	 * 调用service进行远程提交spark任务
	 * @param vo 页面参数
	 * @return 执行结果
	 */
	@ResponseBody
	@PostMapping("/extract/database")
	public Object dbExtractAndLoad2Hdfs(@RequestBody DataBaseExtractorVo vo){
		try {
			return iSparkSubmitService.submitApplication(params,
					vo.getUrl(),
					vo.getTable(),
					vo.getUserName(),
					vo.getPassword(),
					vo.getTargetFileType(),
					vo.getTargetFilePath());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			log.error("执行出错：{}", e.getMessage());
			return Result.err(500, e.getMessage());
		}
	}

	@ResponseBody
	@PostMapping("/operators/actionEntry")
	public ActionEntryResponse actionEntry(@RequestBody ActionEntryVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					"" + vo.getFunction(),
					vo.getParameterList()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return ActionEntryResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return ActionEntryResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/action/collect")
	public CollectResponse collect(@RequestBody CollectVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return CollectResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return CollectResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/action/count")
	public CountResponse count(@RequestBody CountVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return CountResponse.getResponse(ErrorCodeEnum.FAILED, 0);
		}

		return CountResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/action/take")
	public TakeResponse take(@RequestBody TakeVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					"" + vo.getAmount()
			);
			return TakeResponse.getResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
			return TakeResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

	}

	@ResponseBody
	@PostMapping("/action/saveFile")
	public SaveFileResponse saveFile(@RequestBody SaveFileVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getFileType(),
					vo.getFilePath()
			);

			return SaveFileResponse.getResponse(result, vo.getDistributedDataset());
		} catch (Exception e) {
			e.printStackTrace();
			return SaveFileResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/transformationEntry")
	public TransformationEntryResponse transformationEntry(@RequestBody TransformationEntryVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					"" + vo.getFunction(),
					vo.getParameterList()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return TransformationEntryResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return TransformationEntryResponse.getResponse(result);
	}


	@ResponseBody
	@PostMapping("/transformation/map")
	public DatasetResponse map(@RequestBody MapVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getUserDefinedFunction()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());
		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/filter")
	public DatasetResponse filter(@RequestBody FilterVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getUserDefinedFunction()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/sample")
	public DatasetResponse sample(@RequestBody SampleVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getReplace(),
					vo.getPercentage(),
					vo.getRandomSeed()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/union")
	public DatasetResponse union(@RequestBody UnionVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(vo.getDistributedDataset1()),
					vo.getDatasetJson(vo.getDistributedDataset2())
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());
		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

	}

	@ResponseBody
	@PostMapping("/transformation/intersection")
	public DatasetResponse intersection(@RequestBody IntersectionVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(vo.getDistributedDataset1()),
					vo.getDatasetJson(vo.getDistributedDataset2())
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());
		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

	}

	@ResponseBody
	@PostMapping("/transformation/distinct")
	public DatasetResponse distinct(@RequestBody DistinctVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/groupByKey")
	public DatasetResponse groupByKey(@RequestBody GroupByKeyVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

	}

	@ResponseBody
	@PostMapping("/transformation/reduceByKey")
	public DatasetResponse reduceByKey(@RequestBody ReduceByKeyVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getReduceFunction(),
					vo.getKeyField()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/sortByKey")
	public DatasetResponse sortByKey(@RequestBody SortByKeyVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getSort(),
					vo.getKeyField()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/join")
	public DatasetResponse join(@RequestBody JoinVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(vo.getDistributedDataset1()),
					vo.getDatasetJson(vo.getDistributedDataset2()),
					"" + vo.getJoinMethod()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}

	@ResponseBody
	@PostMapping("/transformation/partition")
	public DatasetResponse partition(@RequestBody PartitionVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					"" + vo.getPartitionNumber()
			);

			DatasetJobResult jobResult = DatasetJobResult.getResult(result);
			if (jobResult.getErrorCode() != 0 ) {
				return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
			}

			return DatasetResponse.getResponse(jobResult, vo.getMidFileName());

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}
	}
}
