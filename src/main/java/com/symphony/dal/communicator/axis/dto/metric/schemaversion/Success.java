/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.schemaversion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device Success class.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Success {

	@XmlElement(name = "GetSchemaVersionsSuccess")
	private SchemaVersions schemaVersionsSuccess;

	public SchemaVersions getSchemaVersionsSuccess() {
		return schemaVersionsSuccess;
	}

	public void setSchemaVersionsSuccess(SchemaVersions schemaVersionsSuccess) {
		this.schemaVersionsSuccess = schemaVersionsSuccess;
	}
}
