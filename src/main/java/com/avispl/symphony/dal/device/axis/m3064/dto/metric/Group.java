/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.device.axis.m3064.dto.metric;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.GroupChild;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device Group class is the member data of ParameterDefinitions
 *
 * @author Ivan
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Group {

	@XmlElement(name = "group", namespace = AxisConstant.NAME_SPACE)
	private GroupChild groupChild;

	public GroupChild getGroupChild() {
		return groupChild;
	}

	public void setGroupChild(GroupChild groupChild) {
		this.groupChild = groupChild;
	}
}
