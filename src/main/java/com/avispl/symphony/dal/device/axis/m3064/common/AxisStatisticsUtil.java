/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

import java.util.Objects;

/**
 * AxisStatisticsUtil class support getting the URL by metric
 *
 * @author Ivan
 * @since 1.0
 */
public class AxisStatisticsUtil {

	public static String getMonitorURL(AxisMonitoringMetric axisMonitoringMetric) {
		Objects.requireNonNull(axisMonitoringMetric);
		switch (axisMonitoringMetric) {
			case DEVICE_INFO:
				return AxisURL.DEVICE_INFO;
			case VIDEO_RESOLUTION:
			case VIDEO_FRAME_RATE:
				return AxisURL.URL_PARAM_CGI;
			case SCHEMA_VERSIONS:
				return AxisURL.SCHEMA_VERSIONS;
			default:
				throw new IllegalArgumentException("Do not support axisStatisticsMetric: " + axisMonitoringMetric.name());
		}
	}

	public static String getControlURL(AxisControllingMetric axisControllingMetric) {
		Objects.requireNonNull(axisControllingMetric);
		switch (axisControllingMetric) {
			case TEXT_OVERLAY_CONTENT:
			case ROTATION:
			case MIRRORING:
			case TEXT_OVERLAY:
			case BRIGHTNESS:
			case CONTRAST:
			case SATURATION:
			case SHARPNESS:
			case WHITE_BALANCE:
			case IR_CUT_FILTER:
			case TEXT_OVERLAY_APPEARANCE:
			case TEXT_OVERLAY_SIZE:
			case WIDE_DYNAMIC_RANGE:
				return AxisURL.URL_PARAM_CGI;
			default:
				throw new IllegalArgumentException("Do not support axisStatisticsMetric: " + axisControllingMetric.name());
		}
	}
}
