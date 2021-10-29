/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TextOverlaySizeDropdown class defined the enum for controlling process
 *
 * @author Ivan
 * @since 1.0
 */
public enum TextOverlaySizeDropdown {

	SMALL("Small", "small"),
	MEDIUM("Medium", "medium"),
	LARGE("Large", "large");
	private final String name;

	private final String value;

	TextOverlaySizeDropdown(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public static String[] names() {
		List<String> list = new LinkedList<>();
		for (TextOverlaySizeDropdown textOverlaySizeDropdown : TextOverlaySizeDropdown.values()) {
			list.add(textOverlaySizeDropdown.getName());
		}
		return list.toArray(new String[list.size()]);
	}

	public static Map<String, String> getNameToValueMap() {
		Map<String, String> nameToValue = new HashMap<>();
		for (TextOverlaySizeDropdown textOverlaySizeDropdown : TextOverlaySizeDropdown.values()) {
			nameToValue.put(textOverlaySizeDropdown.getName(), textOverlaySizeDropdown.getValue());
		}
		return nameToValue;
	}

	public static Map<String, String> getValueToNameMap() {
		Map<String, String> valueToName = new HashMap<>();
		for (TextOverlaySizeDropdown textOverlaySizeDropdown : TextOverlaySizeDropdown.values()) {
			valueToName.put(textOverlaySizeDropdown.getValue(), textOverlaySizeDropdown.getName());
		}
		return valueToName;
	}
}
