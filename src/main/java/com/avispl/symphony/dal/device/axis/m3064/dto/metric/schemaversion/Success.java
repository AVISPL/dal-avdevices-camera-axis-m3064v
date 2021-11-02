/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto.metric.schemaversion;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Supported XML schema versions of the device if getting successfully
 *
 * @author Ivan
 * @since 1.0
 * */
@XmlAccessorType(XmlAccessType.NONE)
public class Success {

	@XmlElement(name = "GetSchemaVersionsSuccess", namespace = AxisConstant.NAME_SPACE_OUTPUT)
	private SchemaVersions schemaVersionsSuccess;

	/**
	 * Retrieves {@code {@link #schemaVersionsSuccess}}
	 *
	 * @return value of {@link #schemaVersionsSuccess}
	 */
	public SchemaVersions getSchemaVersionsSuccess() {
		return schemaVersionsSuccess;
	}

	/**
	 * Sets {@code schemaVersionsSuccess}
	 *
	 * @param schemaVersionsSuccess the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.schemaversion.SchemaVersions} field
	 */
	public void setSchemaVersionsSuccess(SchemaVersions schemaVersionsSuccess) {
		this.schemaVersionsSuccess = schemaVersionsSuccess;
	}
}
