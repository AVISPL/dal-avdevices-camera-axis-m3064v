/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto;

import com.symphony.dal.communicator.axis.dto.metric.schemaversion.Success;

import javax.xml.bind.annotation.*;

/**
 * Device Schema Version Status  class.
 */
@XmlRootElement(name = "OrientationResponse")
@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersionStatus {

    @XmlElement(name = "Success")
    private Success success;

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }
}
