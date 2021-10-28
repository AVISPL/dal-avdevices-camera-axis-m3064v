/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}

