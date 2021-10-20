/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.videooutput.rotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Rotations {
    @XmlElement(name="Rotation")
    private RotationChild rotation;

    public RotationChild getRotation() {
        return rotation;
    }

    public void setRotation(RotationChild rotation) {
        this.rotation = rotation;
    }
}
