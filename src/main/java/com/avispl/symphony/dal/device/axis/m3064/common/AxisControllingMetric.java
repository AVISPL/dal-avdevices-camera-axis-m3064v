/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

/**
 * AxisControllingMetric class defined the enum for controlling process
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
public enum AxisControllingMetric {

	ROTATION("Orientation#Rotation"),
	MIRRORING("Orientation#Mirroring"),
	SATURATION("Appearance#Saturation"),
	CONTRAST("Appearance#Contrast"),
	BRIGHTNESS("Appearance#Brightness"),
	SHARPNESS("Appearance#Sharpness"),
	WIDE_DYNAMIC_RANGE("Sensor#WideDynamicRange"),
	WHITE_BALANCE("Sensor#WhiteBalance"),
	IR_CUT_FILTER("Sensor#IRCutFilter"),
	TEXT_OVERLAY("TextOverlay#TextOverlay"),
	TEXT_OVERLAY_CONTENT("TextOverlay#TextOverlayContent"),
	TEXT_OVERLAY_SIZE("TextOverlay#TextOverlaySize"),
	TEXT_OVERLAY_APPEARANCE("TextOverlay#TextOverlayAppearance");

	private final String name;

	/**
	 * AxisControllingMetric instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	AxisControllingMetric(String name) {
		this.name = name;
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
	 *
	 * @param name {@code {@link #name}}
	 * @return name of metric
	 * @throws Exception if can not find the enum with name
	 */
	public static AxisControllingMetric getByName(String name) {
		for (AxisControllingMetric metric : AxisControllingMetric.values()) {
			if (metric.getName().equals(name)) {
				return metric;
			}
		}
		throw new IllegalArgumentException("Can not find the enum with name: " + name);
	}
}