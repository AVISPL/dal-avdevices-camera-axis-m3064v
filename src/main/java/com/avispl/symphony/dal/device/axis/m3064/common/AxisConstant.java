/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

/**
 * AxisConstant class provides the constant during the monitoring and controlling process
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
public final class AxisConstant {

	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";
	public static final String COLON = ":";
	public static final String NONE = "None";
	public static final String AT = "@";
	public static final String QUESTION_MARK = "?";
	public static final String EQUALS_SIGN = "=";
	public static final String OKE = "OK";
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String ENABLE = "Enable";
	public static final String DISABLE = "Disable";
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final String ERROR = "Error";
	public static final Integer SLIDER_RANGE_START = 0;
	public static final Integer SLIDER_RANGE_END = 100;
	public static final String BRAND = "#Brand";
	public static final String BUILD_DATE = "#BuildDate";
	public static final String HARD_WARE_ID = "#HardwareID";
	public static final String SERIAL_NUMBER = "#SerialNumber";
	public static final String VERSION = "#Version";
	public static final String DEVICE_NAME = "#DeviceName";
	public static final String NO_SET_ERR = "The request is failed. Can not control the value: ";
	public static final String NUM_OF_BYTES = "numOfBytes";
	public static final String TEXT_LENGTH_EXCEEDS_80 = ". Text length of " + NUM_OF_BYTES + " bytes exceeds the limit of 80 bytes. Please reduce the text length";
	public static final String NO_RESPONSE_ERR = "The request success but it has no response body";
	public static final String ERR_PROTOCOL = "The device is being down. Can not request the data from device";
	public static final String NAME_SPACE = "http://www.axis.com/ParameterDefinitionsSchema";
	public static final String NAME_SPACE_OUTPUT = "http://www.axis.com/vapix/http_cgi/videooutput1";
	public static final String ERR_SET_CONTROL = "Error when controlling: ";
	public static final String WITH_PROPERTY = " with property: ";
	public static final String ERROR_MESSAGE = ". Error message: ";
	public static final String UNDERSCORE = "_";
	public static final String NEWLINE = "\n";
	public static final String FPS = " fps";
	public static final String BOA_GROUP_POLICY = "BoaGroupPolicy";
	public static final String ADMIN = "admin";
	public static final String BOTH = "both";
	public static final String PROTOCOL_PREFIX = "://";
	public static final int MAX_TEXT_LENGTH = 80; // bytes

}
