/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group.Parameter;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.ChildGroupItem;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device GroupChild class is item of Group
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ChildGroup {

	@XmlElement(name = "parameter", namespace = AxisConstant.NAME_SPACE)
	private Parameter parameter;

	@XmlElement(name = "group", namespace = AxisConstant.NAME_SPACE)
	private ChildGroupItem childGroupItem;

	/**
	 * Retrieves {@code {@link #parameter}}
	 *
	 * @return value of {@link #parameter}
	 */
	public Parameter getParameter() {
		return parameter;
	}

	/**
	 * Sets {@code parameter}
	 *
	 * @param parameter the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group.Parameter} field
	 */
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * Retrieves {@code {@link #childGroupItem}}
	 *
	 * @return value of {@link #childGroupItem}
	 */
	public ChildGroupItem getChildGroupItem() {
		return childGroupItem;
	}

	/**
	 * Sets {@code childGroupItem}
	 *
	 * @param childGroupItem the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.ChildGroupItem} field
	 */
	public void setChildGroupItem(ChildGroupItem childGroupItem) {
		this.childGroupItem = childGroupItem;
	}
}