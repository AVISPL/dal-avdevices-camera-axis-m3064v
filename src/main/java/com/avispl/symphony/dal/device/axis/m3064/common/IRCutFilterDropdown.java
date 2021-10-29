/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

import java.util.HashMap;
import java.util.Map;

/**
 * IRCutFilterDropdown class defined the enum for controlling process
 *
 * @author Ivan
 * @since 1.0
 */
public enum IRCutFilterDropdown {

	AUTO("Auto", "auto"),
	OFF("Off", "no"),
	ON("On", "yes");

	private final String name;

	private final String value;

	IRCutFilterDropdown(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public static Map<String, String> getNameToValueMap() {
		Map<String, String> nameToValue = new HashMap<>();
		for (IRCutFilterDropdown iRCutFilterDropdown : IRCutFilterDropdown.values()) {
			nameToValue.put(iRCutFilterDropdown.getName(), iRCutFilterDropdown.getValue());
		}
		return nameToValue;
	}

	public static Map<String, String> getValueToNameMap() {
		Map<String, String> valueToName = new HashMap<>();
		for (IRCutFilterDropdown iRCutFilterDropdown : IRCutFilterDropdown.values()) {
			valueToName.put(iRCutFilterDropdown.getValue(), iRCutFilterDropdown.getName());
		}
		return valueToName;
	}

	public static String[] names() {
		Map<String, String> nameToValueMap = IRCutFilterDropdown.getNameToValueMap();
		return nameToValueMap.keySet().toArray(new String[nameToValueMap.size()]);
	}
}