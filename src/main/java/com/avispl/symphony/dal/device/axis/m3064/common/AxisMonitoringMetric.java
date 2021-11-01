/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

/**
 * AxisMonitoringMetric class defined the enum for the monitoring process
 *
 * @author Ivan
 * @since 1.0
 */
public enum AxisMonitoringMetric {

	DEVICE_INFO("DeviceInformation", false),
	VIDEO_RESOLUTION("VideoResolution", true),
	VIDEO_FRAME_RATE("VideoFrameRate", true),
	SCHEMA_VERSIONS("SchemaVersions", false);

	private final String name;
	private final boolean isHistorical;

	AxisMonitoringMetric(String name, boolean isHistorical) {
		this.name = name;
		this.isHistorical = isHistorical;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves {@code {@link #isHistorical}}
	 *
	 * @return value of {@link #isHistorical}
	 */
	public boolean isHistorical() {
		return isHistorical;
	}
}
