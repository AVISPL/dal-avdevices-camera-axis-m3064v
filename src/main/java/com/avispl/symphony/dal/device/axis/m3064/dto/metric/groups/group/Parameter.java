/*
 *
 *  * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 *
 */

package com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Parameter is the member data of GropChild
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Parameter {

	@XmlAttribute(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
