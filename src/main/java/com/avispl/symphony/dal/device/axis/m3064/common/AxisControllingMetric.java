/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

/**
 * AxisControllingMetric class defined the enum for controlling process
 */
public enum AxisControllingMetric {

	ROTATION("VideoOutputControl#Rotation"),
	MIRRORING("VideoOutputControl#Mirroring"),
	TEXT_OVERLAY_CONTENT("VideoOutputControl#TextOverlayContent"),
	TEXT_OVERLAY_ENABLE("VideoOutputControl#TextOverlayEnable");

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
