/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

import java.util.LinkedList;
import java.util.List;

/**
 * RotationDropdown class defined the enum for controlling process
 *
 * @author Ivan
 * @since 1.0
 */
public enum RotationDropdown {

	ZERO("0"),
	NICETY("90"),
	ONE_HUNDRED_EIGHTY("180"),
	TWO_HUNDRED_SEVENTY("270");

	RotationDropdown(String name) {
		this.name = name;
	}

	private final String name;

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves all name of RotationDropDown
	 */
	public static String[] names() {
		List<String> list = new LinkedList<>();
		for (RotationDropdown rotationDropdown : RotationDropdown.values()) {
			list.add(rotationDropdown.getName());
		}
		return list.toArray(new String[list.size()]);
	}
}
