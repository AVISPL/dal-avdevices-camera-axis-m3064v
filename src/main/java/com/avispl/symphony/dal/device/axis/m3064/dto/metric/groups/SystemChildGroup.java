/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.SystemChildGroupList;

/**
 * Device SystemChildGroup class is item of SystemGroup
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SystemChildGroup {

	@XmlElement(name = "group", namespace = AxisConstant.NAME_SPACE)
	private SystemChildGroupList[] systemChildGroupLists;

	@XmlAttribute(name = "name")
	private String name;

	/**
	 * Retrieves {@code {@link #systemChildGroupLists}}
	 *
	 * @return value of {@link #systemChildGroupLists}
	 */
	public SystemChildGroupList[] getSystemChildGroupLists() {
		return systemChildGroupLists;
	}

	/**
	 * Sets {@code systemChildGroupLists}
	 *
	 * @param systemChildGroupLists the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.SystemChildGroupList[]} field
	 */
	public void setSystemChildGroupLists(SystemChildGroupList[] systemChildGroupLists) {
		this.systemChildGroupLists = systemChildGroupLists;
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
	 * Sets {@code name}
	 *
	 * @param name the {@code java.lang.String} field
	 */
	public void setName(String name) {
		this.name = name;
	}
}