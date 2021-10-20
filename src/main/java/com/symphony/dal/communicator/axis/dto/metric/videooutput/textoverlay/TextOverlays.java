package com.symphony.dal.communicator.axis.dto.metric.videooutput.textoverlay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class TextOverlays {
    @XmlElement(name= "String")
    private TextOverlayChild textOverlay;

    public TextOverlayChild getTextOverlay() {
        return textOverlay;
    }

    public void setTextOverlay(TextOverlayChild textOverlay) {
        this.textOverlay = textOverlay;
    }
}
