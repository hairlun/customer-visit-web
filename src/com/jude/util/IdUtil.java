package com.jude.util;

import java.net.InetAddress;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdUtil {
	private static final Logger logger = LoggerFactory.getLogger(IdUtil.class);
	private static SnowflakeIdGenerator idGenerator;

	public static synchronized long getId() {
		return idGenerator.nextId();
	}

	static {
		int workId;
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			workId = Integer.parseInt(ip.substring(ip.lastIndexOf(".") + 1));
			workId = workId % 10 + 1;
		} catch (Exception e) {
			workId = new Random().nextInt(15);
			logger.info("get ipaddress exception:" + e.getMessage());
		}
		logger.info("the workId is {}", Integer.valueOf(workId));
		idGenerator = new SnowflakeIdGenerator(workId);
	}
}