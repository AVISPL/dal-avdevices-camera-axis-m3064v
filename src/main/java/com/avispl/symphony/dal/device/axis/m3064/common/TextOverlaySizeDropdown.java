/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
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
 * @version 1.0
 * @since 1.0
 */
public enum TextOverlaySizeDropdown {

	SMALL("Small", "small"),
	MEDIUM("Medium", "medium"),
	LARGE("Large", "large");

	private final String name;

	private final String value;

	/**
	 * TextOverlaySizeDropdown instantiation
	 *
	 * @param name {@code {@link #name}}
	 * @param value {@code {@link #value}}
	 */
	TextOverlaySizeDropdown(String name, String value) {
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
	 * Retrieves name to value map of TextOverlaySizeDropdown
	 * @return Map<String,String> are name and value
	 */
	public static Map<String, String> getNameToValueMap() {
		Map<String, String> nameToValue = new HashMap<>();
		for (TextOverlaySizeDropdown textOverlaySizeDropdown : TextOverlaySizeDropdown.values()) {
			nameToValue.put(textOverlaySizeDropdown.getName(), textOverlaySizeDropdown.getValue());
		}
		return nameToValue;
	}

	/**
	 * Retrieves value to name map of TextOverlaySizeDropdown
	 * @return Map<String,String> are value and name
	 */
	public static Map<String, String> getValueToNameMap() {
		Map<String, String> valueToName = new HashMap<>();
		for (TextOverlaySizeDropdown textOverlaySizeDropdown : TextOverlaySizeDropdown.values()) {
			valueToName.put(textOverlaySizeDropdown.getValue(), textOverlaySizeDropdown.getName());
		}
		return valueToName;
	}

	/**
	 * Retrieves all name of TextOverlaySizeDropdown
	 * @return list name of text overlay size dropdown
	 */
	public static String[] names() {
		List<String> list = new LinkedList<>();
		for (TextOverlaySizeDropdown textOverlaySizeDropdown : TextOverlaySizeDropdown.values()) {
			list.add(textOverlaySizeDropdown.getName());
		}
		return list.toArray(new String[list.size()]);
	}
}
