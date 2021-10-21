/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.videooutput.mirroring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device Mirroring class.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Mirroring {

	@XmlElement(name = "MirroringEnabled")
	private boolean mirroringEnable;

	public void setMirroringEnable(boolean mirroringEnable) {
		this.mirroringEnable = mirroringEnable;
	}

	public boolean isMirroringEnable() {
		return mirroringEnable;
	}
}
