package com.yunify.springbootspark.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yunify.springbootspark.entity.SparkApplicationParam;
import com.yunify.springbootspark.service.ISparkSubmitService;
import com.yunify.springbootspark.util.HttpUtil;
import org.apache.hadoop.io.IOUtils;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

/**
 * @Author yunify
 **/
@Service
public class SparkSubmitServiceImpl implements ISparkSubmitService {

	private static Logger log = LoggerFactory.getLogger(SparkSubmitServiceImpl.class);

	@Value("${spark.url:hdfs://192.168.110.8:9000/}")
	private String url;


	@Override
	public String submitApplication(SparkApplicationParam sparkAppParams, String... otherParams) throws IOException, InterruptedException {
		log.info("spark任务传入参数：{}", sparkAppParams.toString());
		String defaultJarPath = "/root/sparkJob/orc-core-1.5.5.jar";
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Map<String, String> confParams = sparkAppParams.getOtherConfParams();
		SparkLauncher launcher = new SparkLauncher()
				.addJar(defaultJarPath)
				.setMainClass(sparkAppParams.getMainClass())
				.setMaster(sparkAppParams.getMaster())
				.setDeployMode(sparkAppParams.getDeployMode())
				.setAppResource(sparkAppParams.getJar())
				.setConf("spark.driver.memory", sparkAppParams.getDriverMemory())
				.setConf("spark.executor.memory", sparkAppParams.getExecutorMemory())
				.setConf("spark.executor.cores", sparkAppParams.getExecutorCores())
				.setConf(SparkLauncher.DRIVER_EXTRA_CLASSPATH, defaultJarPath);
		if (confParams != null && confParams.size() != 0) {
			log.info("开始设置spark job运行参数:{}", JSONObject.toJSONString(confParams));
			for (Map.Entry<String, String> conf : confParams.entrySet()) {
				log.info("{}:{}", conf.getKey(), conf.getValue());
				launcher.setConf(conf.getKey(), conf.getValue());
			}
		}
		if (otherParams.length != 0) {
			log.info("开始设置spark job参数:{}", Arrays.toString(otherParams));
			launcher.addAppArgs(otherParams);
		}
		log.info("参数设置完成，开始提交spark任务");
		SparkAppHandle handle = launcher.setVerbose(true).startApplication(new SparkAppHandle.Listener() {
					@Override
					public void stateChanged(SparkAppHandle sparkAppHandle) {
						if (sparkAppHandle.getState().isFinal()) {
							countDownLatch.countDown();
						}
						log.info("stateChanged:{}", sparkAppHandle.getState().toString());
					}

					@Override
					public void infoChanged(SparkAppHandle sparkAppHandle) {
						log.info("infoChanged:{}", sparkAppHandle.getState().toString());
					}
				});
		log.info("The task is executing, please wait ....");
		//线程等待任务结束
		countDownLatch.await();
		log.info("The task is finished!");
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", url);

		FileSystem fSystem = FileSystem.get(conf);
		String resultDirPath = "/hadoop/qingmr-spark";
		Path path = new Path(resultDirPath + File.separator + otherParams[0]);
		FSDataInputStream out = fSystem.open(path);
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int length;

		while ((length = out.read(buffer)) > 0) {
			resultStream.write(buffer, 0, length);
		}

		IOUtils.closeStream(out);

		return resultStream.toString("UTF-8");
	}
}
