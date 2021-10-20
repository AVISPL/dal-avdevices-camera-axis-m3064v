/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto;

import com.symphony.dal.communicator.axis.dto.metric.videooutput.VideoOutputError;
import com.symphony.dal.communicator.axis.dto.metric.videooutput.VideoOutputs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Device Video Output class.
 */
@XmlRootElement(name = "VideoOutputResponse")
@XmlAccessorType(XmlAccessType.NONE)
public class VideoOutput {

    @XmlElement(name = "Success")
    private VideoOutputs success;

    @XmlElement(name = "Error")
    private VideoOutputError error;

    public VideoOutputs getSuccess() {
        return success;
    }

    public void setSuccess(VideoOutputs success) {
        this.success = success;
    }

    public VideoOutputError getError() {
        return error;
    }

    public void setError(VideoOutputError error) {
        this.error = error;
    }
}
