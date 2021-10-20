/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.common;

public enum AxisMonitoringMetric {

    DEVICE_INFO("DeviceInfo", false),
    ROTATION("VideoOutputControl#Rotation", false),
    TEXT_OVERLAY_CONTENT("VideoOutputControl#TextOverlayContent", false),
    MIRRORING("VideoOutputControl#Mirroring", false),
    DYNAMIC_OVERLAY("VideoOutputControl#DynamicOverlays", false),
    VIDEO_RESOLUTION("VideoResolution", true),
    VIDEO_SOURCE("VideoSource", true),
    VIDEO_FRAME_RATE("VideoFrameRate", true),
    SCHEMA_VERSIONS("SchemaVersions", false);

    public static final String TEXT_OVERLAY = "VideoOutputControl#TextOverlay";
    public static final String BRAND = "DeviceInfo#Brand";
    public static final String BUILD_DATE = "DeviceInfo#BuildDate";
    public static final String HARD_WARE_ID = "DeviceInfo#HardwareID";
    public static final String SERIAL_NUMBER = "DeviceInfo#SerialNumber";
    public static final String VERSION = "DeviceInfo#Version";
    public static final String WEB_URL = "DeviceInfo#WebURL";
    public static final String DEVICE_NAME = "DeviceInfo#DeviceName";

    public static final String SCHEMA_VERSION = "schemaversion";
    public static final String ROTATION_PARAM = "rotation";
    public static final String DYNAMIC_OVERLAYS = "dynamicoverlays";
    public static final String MIRRORING_ENABLE = "mirroringenabled";
    private final String name;
    private final boolean isHistorical;

    AxisMonitoringMetric(String name, boolean isHistorical) {
        this.name = name;
        this.isHistorical = isHistorical;
    }

    public boolean isHistorical() {
        return isHistorical;
    }

    public String toString() {
        return this.name;
    }
}
