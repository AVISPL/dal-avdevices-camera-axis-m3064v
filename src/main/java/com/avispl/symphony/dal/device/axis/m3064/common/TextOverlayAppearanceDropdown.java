/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TextOverlayAppearanceDropdown class defined the enum for controlling process
 *
 * @author Ivan
 * @since 1.0
 */
public enum TextOverlayAppearanceDropdown {

	BLACK_ON_WHITE("Black on White", "black_white"),
	WHITE_ON_BLACK("White on Black", "white_black"),
	WHITE_ON_TRANSPARENT("White on Transparent", "white_transparent"),
	BLACK_ON_TRANSPARENT("Black on Transparent", "black_transparent");

	private final String name;

	private final String value;

	TextOverlayAppearanceDropdown(String name, String value) {
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
	 * Retrieves name to value map of TextOverlayAppearanceDropdown
	 */
	public static Map<String, String> getNameToValueMap() {
		Map<String, String> nameToValue = new HashMap<>();
		for (TextOverlayAppearanceDropdown textOverlayAppearanceDropdown : TextOverlayAppearanceDropdown.values()) {
			nameToValue.put(textOverlayAppearanceDropdown.getName(), textOverlayAppearanceDropdown.getValue());
		}
		return nameToValue;
	}

	/**
	 * Retrieves value to name map of TextOverlayAppearanceDropdown
	 */
	public static Map<String, String> getValueToNameMap() {
		Map<String, String> valueToName = new HashMap<>();
		for (TextOverlayAppearanceDropdown textOverlayAppearanceDropdown : TextOverlayAppearanceDropdown.values()) {
			valueToName.put(textOverlayAppearanceDropdown.getValue(), textOverlayAppearanceDropdown.getName());
		}
		return valueToName;
	}

	/**
	 * Retrieves all name of TextOverlayAppearanceDropdown
	 */
	public static String[] names() {
		List<String> list = new LinkedList<>();
		for (TextOverlayAppearanceDropdown textOverlayAppearanceDropdown : TextOverlayAppearanceDropdown.values()) {
			list.add(textOverlayAppearanceDropdown.getName());
		}
		return list.toArray(new String[list.size()]);
	}
}
