package com.symphony.dal.communicator.axis;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */

public class AxisCommunicatorTest {
    AxisCommunicator axisCommunicator = new AxisCommunicator();

    @Before
    public void before() throws Exception {
        axisCommunicator.setHost("127.0.0.1");
        axisCommunicator.setProtocol("http");
        axisCommunicator.setPort(5500);
        axisCommunicator.init();
    }

    @Test
    public void checkExtendedStatistics() {
        ExtendedStatistics extendedStatistics = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
        Map<String, String> stats = extendedStatistics.getStatistics();
        Assert.assertEquals("Online", stats.get("Status"));
        Assert.assertEquals("Yes", stats.get("Audio Source"));
        Assert.assertEquals("AXIS Q3505 Mk II Fixed Dome Network Camera", stats.get("Device Name"));
        for (Map.Entry<String, String> entry : stats.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }
}
