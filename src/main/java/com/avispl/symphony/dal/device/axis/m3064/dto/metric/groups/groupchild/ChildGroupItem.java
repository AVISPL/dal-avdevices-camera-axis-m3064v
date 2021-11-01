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
	private ChildParameterItem parameter;

	/**
	 * Retrieves {@code {@link #parameter}}
	 *
	 * @return value of {@link #parameter}
	 */
	public ChildParameterItem getParameter() {
		return parameter;
	}

	/**
	 * Sets {@code parameter}
	 *
	 * @param parameter the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.ChildParameterItem} field
	 */
	public void setParameter(ChildParameterItem parameter) {
		this.parameter = parameter;
	}
}