package com.sfu.an3di.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {
	public static Logger getLogger() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callersClassName = stackTrace[2].getClassName();
        return LoggerFactory.getLogger(callersClassName);
	}
}
