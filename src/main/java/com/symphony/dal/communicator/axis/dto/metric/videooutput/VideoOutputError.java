/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.videooutput;

import com.symphony.dal.communicator.axis.dto.metric.videooutput.errorcode.GeneralError;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Video Output Error class.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class VideoOutputError {

	@XmlElement(name = "GeneralError")
	private GeneralError generalError;

	public GeneralError getGeneralError() {
		return generalError;
	}

	public void setGeneralError(GeneralError generalError) {
		this.generalError = generalError;
	}

}
