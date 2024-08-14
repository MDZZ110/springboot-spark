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
	public DatasetResponse take(@RequestBody TakeVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					"" + vo.getAmount()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return DatasetResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/action/saveFile")
	public Response saveFile(@RequestBody SaveFileVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getFileType(),
					vo.getFilePath()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.getResponse(ErrorCodeEnum.FAILED);
		}

		return Response.getResponse(result);
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
	public MapResponse map(@RequestBody MapVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getUserDefinedFunction()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return MapResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return MapResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/transformation/filter")
	public FilterResponse filter(@RequestBody FilterVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getUserDefinedFunction()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return FilterResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return FilterResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/transformation/sample")
	public SampleResponse sample(@RequestBody SampleVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getReplace(),
					vo.getPercentage(),
					vo.getRandomSeed()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return SampleResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return SampleResponse.getResponse(result);
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

			String fileName = "union" + "-" + vo.getDistributedDataset1() + "-" + vo.getDistributedDataset2();
			return DatasetResponse.getResponse(jobResult, fileName);
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

			String fileName = "intersection" + "-" + vo.getDistributedDataset1() + "-" + vo.getDistributedDataset2();
			return DatasetResponse.getResponse(jobResult, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return DatasetResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

	}

	@ResponseBody
	@PostMapping("/transformation/distinct")
	public DistinctResponse distinct(@RequestBody DistinctVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return DistinctResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return DistinctResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/transformation/groupByKey")
	public GroupByKeyResponse groupByKey(@RequestBody GroupByKeyVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return GroupByKeyResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return GroupByKeyResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/transformation/reduceByKey")
	public ReduceByKeyResponse reduceByKey(@RequestBody ReduceByKeyVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getReduceFunction(),
					vo.getKeyField()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return ReduceByKeyResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return ReduceByKeyResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/transformation/sortByKey")
	public SortByKeyResponse sortByKey(@RequestBody SortByKeyVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					vo.getSort(),
					vo.getKeyField()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return SortByKeyResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return SortByKeyResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/transformation/join")
	public JoinResponse join(@RequestBody JoinVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(vo.getDistributedDataset1()),
					vo.getDatasetJson(vo.getDistributedDataset2()),
					"" + vo.getJoinMethod()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return JoinResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return JoinResponse.getResponse(result);
	}

	@ResponseBody
	@PostMapping("/transformation/partition")
	public PartitionResponse partition(@RequestBody PartitionVo vo) {
		String result;
		try {
			result = iSparkSubmitService.submitApplication(params,
					vo.getMethodName(),
					vo.getDatasetJson(),
					"" + vo.getPartitionNumber()
			);

		} catch (Exception e) {
			e.printStackTrace();
			return PartitionResponse.getResponse(ErrorCodeEnum.FAILED, null);
		}

		return PartitionResponse.getResponse(result);
	}



}
