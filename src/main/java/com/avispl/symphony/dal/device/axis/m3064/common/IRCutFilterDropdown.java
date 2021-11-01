/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves {@code {@link #value}}
	 *
	 * @return value of {@link #value}
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Retrieves name to value map of IRCut metric
	 */
	public static Map<String, String> getNameToValueMap() {
		Map<String, String> nameToValue = new HashMap<>();
		for (IRCutFilterDropdown iRCutFilterDropdown : IRCutFilterDropdown.values()) {
			nameToValue.put(iRCutFilterDropdown.getName(), iRCutFilterDropdown.getValue());
		}
		return nameToValue;
	}

	/**
	 * Retrieves value to name map of IRCut metric
	 */
	public static Map<String, String> getValueToNameMap() {
		Map<String, String> valueToName = new HashMap<>();
		for (IRCutFilterDropdown iRCutFilterDropdown : IRCutFilterDropdown.values()) {
			valueToName.put(iRCutFilterDropdown.getValue(), iRCutFilterDropdown.getName());
		}
		return valueToName;
	}

	/**
	 * Retrieves all name of IRCut metric
	 */
	public static String[] names() {
		List<String> list = new LinkedList<>();
		for (IRCutFilterDropdown irCutFilterDropdown : IRCutFilterDropdown.values()) {
			list.add(irCutFilterDropdown.getName());
		}
		return list.toArray(new String[list.size()]);
	}
}