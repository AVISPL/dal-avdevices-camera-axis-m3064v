/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group.SystemParameter;

/**
 * Device SystemChildGroupList class is the member data of SystemChildGroup
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SystemChildGroupList {

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "value")
	private String value;

	@XmlElement(name = "parameter", namespace = AxisConstant.NAME_SPACE)
	private SystemParameter[] systemParameters;

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

	/**
	 * Retrieves {@code {@link #systemParameters}}
	 *
	 * @return value of {@link #systemParameters}
	 */
	public SystemParameter[] getSystemParameters() {
		return systemParameters;
	}

	/**
	 * Sets {@code systemParameters}
	 *
	 * @param systemParameters the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group.SystemParameter[]} field
	 */
	public void setSystemParameters(SystemParameter[] systemParameters) {
		this.systemParameters = systemParameters;
	}
}
