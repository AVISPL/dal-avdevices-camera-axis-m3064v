package com.symphony.dal.communicator.axis.dto.metric.videooutput.textoverlay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class TextOverlayChild {

    @XmlElement(name="CurrentValue")
    private String currentValue;

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}
