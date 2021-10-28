/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto.metric.schemaversion;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * SchemaVersionChild is the member data of SchemaVersions
 *
 * @author Ivan
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersionChild {

	@XmlElement(name = "VersionNumber", namespace = AxisConstant.NAME_SPACE_OUTPUT)
	private String versionNumber;

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
}
