/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

/**
 * AxisControllingMetric class defined the enum for controlling process
 *
 * @author Ivan
 * @since 1.0
 */
public enum AxisControllingMetric {

	ROTATION("VideoOutputControl#Rotation"),
	MIRRORING("VideoOutputControl#Mirroring"),
	TEXT_OVERLAY_CONTENT("VideoOutputControl#TextOverlayContent"),
	TEXT_OVERLAY("VideoOutputControl#TextOverlay"),
	SATURATION("VideoOutputControl#Saturation"),
	CONTRAST("VideoOutputControl#Contrast"),
	BRIGHTNESS("VideoOutputControl#Brightness"),
	SHARPNESS("VideoOutputControl#Sharpness"),
	WIDE_DYNAMIC_RANGE("VideoOutputControl#WideDynamicRange"),
	WHITE_BALANCE("VideoOutputControl#WhiteBalance"),
	IR_CUT_FILTER("VideoOutputControl#IRCutFilter"),
	TEXT_OVERLAY_SIZE("VideoOutputControl#TextOverlaySize"),
	TEXT_OVERLAY_APPEARANCE("VideoOutputControl#TextOverlayAppearance");

	public static AxisControllingMetric getByName(String name) {
		for (AxisControllingMetric metric : AxisControllingMetric.values()) {
			if (metric.getName().equals(name)) {
				return metric;
			}
		}
		throw new IllegalArgumentException("Can not find the enum with name: " + name);
	}

	private final String name;

	AxisControllingMetric(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
