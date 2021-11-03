/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.Group;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ParameterDefinitions is the definition of the response from Axis device when using param.cgi
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "parameterDefinitions", namespace = AxisConstant.NAME_SPACE)
@XmlAccessorType(XmlAccessType.NONE)
public class ParameterDefinitions {

	@XmlElement(name = "model", namespace = AxisConstant.NAME_SPACE)
	private String model;

	@XmlElement(name = "firmwareVersion", namespace = AxisConstant.NAME_SPACE)
	private String firmwareVersion;

	@XmlElement(name = "group", namespace = AxisConstant.NAME_SPACE)
	private Group group;

	/**
	 * Retrieves {@code {@link #model}}
	 *
	 * @return value of {@link #model}
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets {@code model}
	 *
	 * @param model the {@code java.lang.String} field
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Retrieves {@code {@link #firmwareVersion}}
	 *
	 * @return value of {@link #firmwareVersion}
	 */
	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	/**
	 * Sets {@code firmwareVersion}
	 *
	 * @param firmwareVersion the {@code java.lang.String} field
	 */
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	/**
	 * Retrieves {@code {@link #group}}
	 *
	 * @return value of {@link #group}
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Sets {@code group}
	 *
	 * @param group the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.Group} field
	 */
	public void setGroup(Group group) {
		this.group = group;
	}
}

