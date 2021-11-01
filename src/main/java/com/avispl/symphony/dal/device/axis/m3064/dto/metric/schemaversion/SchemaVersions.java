/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto.metric.schemaversion;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Supported XML schema versions of the device
 *
 * @author Ivan
 * @since 1.0
 * */
@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersions {

	@XmlElement(name = "SchemaVersion", namespace = AxisConstant.NAME_SPACE_OUTPUT)
	private ChildSchemaVersion schemaVersion;

	/**
	 * Retrieves {@code {@link #schemaVersion}}
	 *
	 * @return value of {@link #schemaVersion}
	 */
	public ChildSchemaVersion getSchemaVersion() {
		return schemaVersion;
	}

	/**
	 * Sets {@code schemaVersion}
	 *
	 * @param schemaVersion the {@code com.avispl.symphony.dal.device.axis.m3064.dto.metric.schemaversion.ChildSchemaVersion} field
	 */
	public void setSchemaVersion(ChildSchemaVersion schemaVersion) {
		this.schemaVersion = schemaVersion;
	}
}
