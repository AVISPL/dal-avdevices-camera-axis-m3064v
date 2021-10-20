package com.symphony.dal.communicator.axis.dto.metric.schemaversion.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Schema {
    @XmlElement(name = "SchemaVersion")
    private AudioStatus audioStatus;

    public AudioStatus getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(AudioStatus audioStatus) {
        this.audioStatus = audioStatus;
    }
}