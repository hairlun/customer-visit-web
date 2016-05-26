package com.jude.util;

import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdUtil {
	private static final Logger logger = LoggerFactory.getLogger(IdUtil.class);

	public static synchronized String getId() {
		String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
	}
}