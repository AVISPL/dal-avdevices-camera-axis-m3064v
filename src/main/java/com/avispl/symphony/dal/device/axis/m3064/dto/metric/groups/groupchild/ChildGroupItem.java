/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device ChildGroupItem class is the member data of GroupChild
 *
 * @author Ivan
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ChildGroupItem {

	@XmlElement(name = "parameter", namespace = AxisConstant.NAME_SPACE)
	private ParameterChildItem parameter;

	public ParameterChildItem getParameter() {
		return parameter;
	}

	public void setParameter(ParameterChildItem parameter) {
		this.parameter = parameter;
	}
}