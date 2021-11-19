/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.device.axis.m3064.dto.metric;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.SystemChildGroup;

/**
 * Device SystemGroup class is the member data of SystemParameterDefinitions
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SystemGroup {

	@XmlAttribute(name = "name")
	private String name;

	@XmlElement(name = "group", namespace = AxisConstant.NAME_SPACE)
	private SystemChildGroup groupChildren;

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
	 * Retrieves {@code {@link #groupChildren}}
	 *
	 * @return value of {@link #groupChildren}
	 */
	public SystemChildGroup getGroupChildren() {
		return groupChildren;
	}

	/**
	 * Sets {@code groupChildren}
	 *
	 * @param groupChildren the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.SystemChildGroup} field
	 */
	public void setGroupChildren(SystemChildGroup groupChildren) {
		this.groupChildren = groupChildren;
	}
}
