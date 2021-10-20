/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.common;

import java.util.Objects;

public class AxisStatisticsFactory {

    public static String getURL(AxisMonitoringMetric axisStatisticsMetric) {
        Objects.requireNonNull(axisStatisticsMetric);
        switch (axisStatisticsMetric) {
            case DEVICE_INFO:
                return AxisURL.DEVICE_INFO;
            case VIDEO_SOURCE:
                return AxisURL.VIDEO_SOURCE;
            case ROTATION:
                return AxisURL.ROTATION;
            case SCHEMA_VERSIONS:
                return AxisURL.SCHEMA_VERSIONS;
            case VIDEO_RESOLUTION:
                return AxisURL.VIDEO_RESOLUTION;
            case TEXT_OVERLAY_CONTENT:
                return AxisURL.TEXT_OVERLAY;
            case MIRRORING:
                return AxisURL.MIRRORING;
            case DYNAMIC_OVERLAY:
                return AxisURL.DYNAMIC_OVERLAY;
            case VIDEO_FRAME_RATE:
                return AxisURL.VIDEO_FRAME_RATE;
            default:
                throw new IllegalArgumentException("Do not support axisStatisticsMetric: " + axisStatisticsMetric.name());
        }
    }
}
