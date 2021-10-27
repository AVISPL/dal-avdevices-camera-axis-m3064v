/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto.metric.schemaversion;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Success {

	@XmlElement(name = "GetSchemaVersionsSuccess", namespace = AxisConstant.NAME_SPACE_OUTPUT)
	private SchemaVersions schemaVersionsSuccess;

	public SchemaVersions getSchemaVersionsSuccess() {
		return schemaVersionsSuccess;
	}

	public void setSchemaVersionsSuccess(SchemaVersions schemaVersionsSuccess) {
		this.schemaVersionsSuccess = schemaVersionsSuccess;
	}
}
