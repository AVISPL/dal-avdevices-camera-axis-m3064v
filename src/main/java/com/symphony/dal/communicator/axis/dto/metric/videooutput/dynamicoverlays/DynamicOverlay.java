/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.videooutput.dynamicoverlays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device Dynamic Overlay class.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class DynamicOverlay {

	@XmlElement(name = "DynamicOverlaysEnabled")
	private boolean dynamicOverlaysEnabled;

	public boolean isDynamicOverlaysEnabled() {
		return dynamicOverlaysEnabled;
	}

	public void setDynamicOverlaysEnabled(boolean dynamicOverlaysEnabled) {
		this.dynamicOverlaysEnabled = dynamicOverlaysEnabled;
	}
}
