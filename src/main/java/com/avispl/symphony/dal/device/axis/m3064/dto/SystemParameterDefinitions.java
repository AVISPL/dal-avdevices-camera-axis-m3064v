/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.SystemGroup;

/**
 * ParameterDefinitions is the definition of the response from Axis device when using param.cgi
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "parameterDefinitions", namespace = AxisConstant.NAME_SPACE)
@XmlAccessorType(XmlAccessType.NONE)
public class SystemParameterDefinitions {

	@XmlElement(name = "group", namespace = AxisConstant.NAME_SPACE)
	private SystemGroup group;

	/**
	 * Retrieves {@code {@link #group}}
	 *
	 * @return value of {@link #group}
	 */
	public SystemGroup getGroup() {
		return group;
	}

	/**
	 * Sets {@code group}
	 *
	 * @param group the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.Group} field
	 */
	public void setGroup(SystemGroup group) {
		this.group = group;
	}
}

