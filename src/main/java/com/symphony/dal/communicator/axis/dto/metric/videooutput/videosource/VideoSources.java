/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.videooutput.videosource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Video Sources class.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class VideoSources {

    @XmlElement(name="VideoSource")
    private VideoSourceChild videoSource;

    public void setVideoSource(VideoSourceChild videoSource) {
        this.videoSource = videoSource;
    }

    public VideoSourceChild getVideoSource() {
        return videoSource;
    }
}