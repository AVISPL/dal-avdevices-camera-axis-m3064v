/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.common;

/**
 * List URL get information from device
 */
public class AxisURL {

    public static final String DEVICE_INFO = "axis-cgi/basicdeviceinfo.cgi";
    public static final String SCHEMA_VERSIONS = "axis-cgi/orientation/getschemaversions.cgi";
    public static final String VIDEO_SOURCE = "axis-cgi/videooutput/getvideosources.cgi";
    public static final String MIRRORING = "axis-cgi/videooutput/getmirroring.cgi";
    public static final String ROTATION = "axis-cgi/videooutput/getrotation.cgi";
    public static final String TEXT_OVERLAY = "axis-cgi/videooutput/gettextoverlay.cgi";
    public static final String VIDEO_RESOLUTION = "axis-cgi/imagesize.cgi";
    public static final String DYNAMIC_OVERLAY = "axis-cgi/videooutput/getdynamicoverlays.cgi";
    public static final String VIDEO_FRAME_RATE = "video-frame-rate";
}
