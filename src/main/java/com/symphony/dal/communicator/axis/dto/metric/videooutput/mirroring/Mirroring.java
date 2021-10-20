package com.symphony.dal.communicator.axis.dto.metric.videooutput.mirroring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Mirroring {

    @XmlElement(name="MirroringEnabled")
    private boolean mirroringEnable;

    public void setMirroringEnable(boolean mirroringEnable) {
        this.mirroringEnable = mirroringEnable;
    }

    public boolean isMirroringEnable() {
        return mirroringEnable;
    }
}