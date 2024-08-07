package com.yunify.springbootspark.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author yunify
 **/
@Data
@Configuration
@Component
public class SparkApplicationParam {
	/**
	 * 任务的主类
	 */
	@Value("${spark.mainClass:com.qingcloud.App}")
	private String mainClass;
	/**
	 * jar包路径
	 */
	@Value("${spark.jar:/root/sparkJob/memory-computation-1.0-SNAPSHOT-shaded.jar}")
	private String jar;
	@Value("${spark.master:yarn}")
	private String master;
	@Value("${spark.deploy.mode:cluster}")
	private String deployMode;
	@Value("${spark.driver.memory:1g}")
	private String driverMemory;
	@Value("${spark.executor.memory:1g}")
	private String executorMemory;
	@Value("${spark.executor.cores:1}")
	private String executorCores;
	/**
	 * 其他配置：传递给spark job的参数
	 */
	private Map<String, String> otherConfParams;
}
