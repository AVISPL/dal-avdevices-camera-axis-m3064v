/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.videooutput.rotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import java.util.Objects;

/**
 * Current rotation and available rotations on the video output
 */
@XmlAccessorType(XmlAccessType.NONE)
public class RotationChild {

	@XmlElement(name = "Degrees")
	private int degrees;

	@XmlElement(name = "QuadViewSupport")
	private boolean quadViewSupport;

	@XmlElement(name = "Active")
	private boolean active;

	public int getDegrees() {
		return degrees;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isQuadViewSupport() {
		return quadViewSupport;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setDegrees(int degrees) {
		this.degrees = degrees;
	}

	public void setQuadViewSupport(boolean quadViewSupport) {
		this.quadViewSupport = quadViewSupport;
	}

	@Override
	public String toString() {
		return "Rotation{" +
				"degrees=" + degrees +
				", quadViewSupport=" + quadViewSupport +
				", active=" + active +
				'}';
	}

	@Override
	public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
		RotationChild rotation = (RotationChild) o;
		return degrees == rotation.degrees && quadViewSupport == rotation.quadViewSupport && active == rotation.active;
	}

	@Override
	public int hashCode() {
		return Objects.hash(degrees, quadViewSupport, active);
	}
}
