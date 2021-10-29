/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

import java.util.HashMap;
import java.util.Map;

/**
 * WhiteBalanceDropdown class defined the enum for controlling process
 *
 * @author Ivan
 * @since 1.0
 */
public enum WhiteBalanceDropdown {

	AUTO("Auto", "auto"),
	HOLD("Hold", "hold"),
	FIXED_OUTDOORS_1("Fixed - outdoors 1", "fixed_outdoor1"),
	FIXED_OUTDOORS_2("Fixed - outdoors 2", "fixed_outdoor2"),
	FIXED_INDOORS("Fixed - indoors", "fixed_indoor"),
	FIXED_FLUORESCENT_1("Fixed – fluorescent 1", "fixed_fluor1"),
	FIXED_FLUORESCENT_2("Fixed – fluorescent 2", "fixed_fluor2");

	private final String name;

	private final String value;

	WhiteBalanceDropdown(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static String[] names() {
		Map<String, String> nameToValueMap = WhiteBalanceDropdown.getNameToValueMap();
		return nameToValueMap.keySet().toArray(new String[nameToValueMap.size()]);
	}

	public static Map<String, String> getNameToValueMap() {
		Map<String, String> nameToValue = new HashMap<>();
		for (WhiteBalanceDropdown whiteBalanceControl : WhiteBalanceDropdown.values()) {
			nameToValue.put(whiteBalanceControl.getName(), whiteBalanceControl.getValue());
		}
		return nameToValue;
	}

	public static Map<String, String> getValueToNameMap() {
		Map<String, String> valueToName = new HashMap<>();
		for (WhiteBalanceDropdown whiteBalanceControl : WhiteBalanceDropdown.values()) {
			valueToName.put(whiteBalanceControl.getValue(), whiteBalanceControl.getName());
		}
		return valueToName;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
