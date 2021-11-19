/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.device.axis.m3064.dto.metric;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.ChildGroup;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device Group class is the member data of ParameterDefinitions
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Group {

	@XmlElement(name = "group", namespace = AxisConstant.NAME_SPACE)
	private ChildGroup groupChild;

	/**
	 * Retrieves {@code {@link #groupChild}}
	 *
	 * @return value of {@link #groupChild}
	 */
	public ChildGroup getGroupChild() {
		return groupChild;
	}

	/**
	 * Sets {@code groupChild}
	 *
	 * @param groupChild the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.ChildGroup} field
	 */
	public void setGroupChild(ChildGroup groupChild) {
		this.groupChild = groupChild;
	}
}
