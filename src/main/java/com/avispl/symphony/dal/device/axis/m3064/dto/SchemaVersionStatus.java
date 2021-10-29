/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto;

import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.schemaversion.Success;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SchemaVersionStatus represent for the response from getting the schema version
 *
 * @author Ivan
 * @since 1.0
 */
@XmlRootElement(name = "VideoOutputResponse", namespace = AxisConstant.NAME_SPACE_OUTPUT)
@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersionStatus {

	@XmlElement(name = "Success", namespace = AxisConstant.NAME_SPACE_OUTPUT)
	private Success success;

	@XmlElement(name = "Error", namespace = AxisConstant.NAME_SPACE_OUTPUT)
	private Error error;

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public Success getSuccess() {
		return success;
	}

	public void setSuccess(Success success) {
		this.success = success;
	}
}
