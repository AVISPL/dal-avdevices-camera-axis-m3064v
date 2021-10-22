/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.schemaversion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device Schema Version Child class.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersionChild {

	@XmlElement(name = "VersionNumber")
	private String versionNumber;

	@XmlElement(name = "Deprecated")
	private String deprecated;

	public String getVersionNumber() {
		return versionNumber;
	}

	public String getDeprecated() {
		return deprecated;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public void setDeprecated(String deprecated) {
		this.deprecated = deprecated;
	}
}
