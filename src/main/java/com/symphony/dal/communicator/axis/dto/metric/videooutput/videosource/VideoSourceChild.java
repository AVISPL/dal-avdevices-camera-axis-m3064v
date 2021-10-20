package com.symphony.dal.communicator.axis.dto.metric.videooutput.videosource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Available video sources from the Axis product. <br/>
 * Video sources used as video output are marked as “active”.
 * */
@XmlAccessorType(XmlAccessType.NONE)
public class VideoSourceChild {

    /**
     * Video source name
     * */
    @XmlElement(name="Name")
    private String name;

    /**
     * Video source identifier
     * */
    @XmlElement(name="Id")
    private String id;

    /**
     * true = The video source is used for video output.
     * false = The video source is not used for video output.
     * */
    @XmlElement(name="Active")
    private boolean active;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
