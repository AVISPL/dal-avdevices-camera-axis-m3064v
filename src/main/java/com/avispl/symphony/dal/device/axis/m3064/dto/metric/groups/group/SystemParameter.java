/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Device SystemParameter class is the member data of SystemChildGroupList
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SystemParameter {

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "value")
	private String value;

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets {@code name}
	 *
	 * @param name the {@code java.lang.String} field
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Sets {@code value}
	 *
	 * @param value the {@code java.lang.String} field
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
