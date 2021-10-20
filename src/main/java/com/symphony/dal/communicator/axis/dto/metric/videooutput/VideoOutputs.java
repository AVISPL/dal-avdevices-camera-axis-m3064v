/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto.metric.videooutput;

import com.symphony.dal.communicator.axis.dto.metric.videooutput.dynamicoverlays.DynamicOverlay;
import com.symphony.dal.communicator.axis.dto.metric.videooutput.mirroring.Mirroring;
import com.symphony.dal.communicator.axis.dto.metric.videooutput.rotation.Rotations;
import com.symphony.dal.communicator.axis.dto.metric.videooutput.textoverlay.TextOverlays;
import com.symphony.dal.communicator.axis.dto.metric.videooutput.videosource.VideoSources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Device Video Outputs class.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class VideoOutputs {

    @XmlElement(name = "GetVideoSourcesSuccess")
    private VideoSources videoSourcesSuccess;

    @XmlElement(name = "GetRotationSuccess")
    private Rotations rotationSuccess;

    @XmlElement(name = "GetDynamicOverlaySuccess")
    private DynamicOverlay dynamicOverlaySuccess;

    @XmlElement(name = "GetTextOverlaySuccess")
    private TextOverlays textOverlaySuccess;

    @XmlElement(name = "GetMirroringSuccess")
    private Mirroring mirroringSuccess;

    @XmlElement(name = "GeneralSuccess")
    private String generalSuccess;

    public void setGeneralSuccess(String generalSuccess) {
        this.generalSuccess = generalSuccess;
    }

    public String getGeneralSuccess() {
        return generalSuccess;
    }

    public void setVideoSourcesSuccess(VideoSources videoSourcesSuccess) {
        this.videoSourcesSuccess = videoSourcesSuccess;
    }

    public VideoSources getVideoSourcesSuccess() {
        return videoSourcesSuccess;
    }

    public Rotations getRotationSuccess() {
        return rotationSuccess;
    }

    public void setRotationSuccess(Rotations rotationSuccess) {
        this.rotationSuccess = rotationSuccess;
    }

    public DynamicOverlay getDynamicOverlaySuccess() {
        return dynamicOverlaySuccess;
    }

    public void setDynamicOverlaySuccess(DynamicOverlay dynamicOverlaySuccess) {
        this.dynamicOverlaySuccess = dynamicOverlaySuccess;
    }

    public TextOverlays getTextOverlaySuccess() {
        return textOverlaySuccess;
    }

    public void setTextOverlaySuccess(TextOverlays textOverlaySuccess) {
        this.textOverlaySuccess = textOverlaySuccess;
    }

    public Mirroring getMirroringSuccess() {
        return mirroringSuccess;
    }

    public void setMirroringSuccess(Mirroring mirroringSuccess) {
        this.mirroringSuccess = mirroringSuccess;
    }
}
