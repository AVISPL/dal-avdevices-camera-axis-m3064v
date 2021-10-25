/*
 *  * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.util.Map;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.symphony.dal.communicator.axis.common.AxisConstant;
import com.symphony.dal.communicator.axis.common.AxisMonitoringMetric;
import com.symphony.dal.communicator.axis.common.AxisStatisticsFactory;
import com.symphony.dal.communicator.axis.common.AxisURL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;

/**
 * Unit test for AxisCommunicator
 * ExtendedStatistics, DynamicStatistics retrieve data success and fail
 */
public class AxisCommunicatorTest {

	AxisCommunicator axisCommunicator = new AxisCommunicator();
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(5500).httpsPort(8088)
			.bindAddress("127.0.0.1"));

	@Before
	public void setUp() throws Exception {
		axisCommunicator.setHost("127.0.0.1");
		axisCommunicator.setProtocol("http");
		axisCommunicator.setPort(5500);
		axisCommunicator.init();
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success with statistics data is not empty
	 * Expect statistics have data
	 */
	@Test
	public void checkExtendedStatistics() {
		ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistic.getStatistics();
		Assert.assertEquals("1.0", stats.get(AxisMonitoringMetric.SCHEMA_VERSIONS.toString()));
		Assert.assertEquals("AXIS", stats.get(AxisMonitoringMetric.BRAND));
		Assert.assertEquals("Feb 14 2018 13:08", stats.get(AxisMonitoringMetric.BUILD_DATE));
		Assert.assertEquals("AXIS Q3505 Mk II Fixed Dome Network Camera", stats.get(AxisMonitoringMetric.DEVICE_NAME));
		Assert.assertEquals("714.4", stats.get(AxisMonitoringMetric.HARD_WARE_ID));
		Assert.assertEquals("8.20.1", stats.get(AxisMonitoringMetric.VERSION));
		Assert.assertEquals("http://www.axis.com", stats.get(AxisMonitoringMetric.WEB_URL));
		Assert.assertEquals("ACCC8E78B977", stats.get(AxisMonitoringMetric.SERIAL_NUMBER));
		Assert.assertEquals("The overlay text", stats.get(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT.toString()));
		Assert.assertEquals("35", stats.get(AxisMonitoringMetric.ROTATION.toString()));
		Assert.assertEquals(AxisConstant.ENABLE, stats.get(AxisMonitoringMetric.DYNAMIC_OVERLAY.toString()));
		Assert.assertEquals(AxisConstant.ENABLE, stats.get(AxisMonitoringMetric.MIRRORING.toString()));
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success with dynamicStatistics data is not empty
	 * Expect dynamicStatistics have data
	 */
	@Test
	public void checkDynamicStatistics() {
		ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
		Map<String, String> dynamic = extendedStatistic.getDynamicStatistics();

		Assert.assertEquals("192 x 144", dynamic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.toString()));
		Assert.assertEquals("60", dynamic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.toString()));
		Assert.assertEquals(AxisConstant.ACTIVE, dynamic.get(AxisMonitoringMetric.VIDEO_SOURCE.toString()));
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics with Api error message
	 * Expect throw error message
	 */
	@Test
	public void testGetFailForAllHistoricalStatistics() {
		try (MockedStatic<AxisStatisticsFactory> mock = Mockito.mockStatic(AxisStatisticsFactory.class)) {
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn(AxisURL.DEVICE_INFO);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DYNAMIC_OVERLAY)).thenReturn(AxisURL.DYNAMIC_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT)).thenReturn(AxisURL.TEXT_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.MIRRORING)).thenReturn(AxisURL.MIRRORING);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn(AxisURL.SCHEMA_VERSIONS);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.ROTATION)).thenReturn(AxisURL.ROTATION);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("video-resolution");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("video-frame-rate-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE)).thenReturn("/video-source");
			try {
				axisCommunicator.getMultipleStatistics();
				Assert.fail("Expect to fail here but getting historical statistics successfully");
			} catch (Exception e) {
				Assert.assertEquals("Haven't video frame rate\n" + "Haven't video resolution \n" + "Haven't video source", e.getMessage());
			}
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics retrieve data from dynamicStatistic
	 * Expect dynamicStatistic not empty videoResolution value is None
	 */
	@Test
	public void testGetFailForVideoResolutionOnlyAndTheRestSuccess() {
		try (MockedStatic<AxisStatisticsFactory> mock = Mockito.mockStatic(AxisStatisticsFactory.class)) {
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn(AxisURL.DEVICE_INFO);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DYNAMIC_OVERLAY)).thenReturn(AxisURL.DYNAMIC_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT)).thenReturn(AxisURL.TEXT_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.MIRRORING)).thenReturn(AxisURL.MIRRORING);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn(AxisURL.SCHEMA_VERSIONS);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.ROTATION)).thenReturn(AxisURL.ROTATION);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn(AxisURL.VIDEO_FRAME_RATE);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE)).thenReturn(AxisURL.VIDEO_SOURCE);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> dynamic = extendedStatistic.getDynamicStatistics();

			Assert.assertEquals(AxisConstant.NONE, dynamic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.toString()));
			Assert.assertEquals(AxisConstant.ACTIVE, dynamic.get(AxisMonitoringMetric.VIDEO_SOURCE.toString()));
			Assert.assertEquals("60", dynamic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.toString()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics retrieve data from dynamicStatistic
	 * Expect dynamicStatistic not empty videoSource value is None
	 */
	@Test
	public void testGetFailForVideoSourceOnlyAndTheRestSuccess() {
		try (MockedStatic<AxisStatisticsFactory> mock = Mockito.mockStatic(AxisStatisticsFactory.class)) {
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn(AxisURL.DEVICE_INFO);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DYNAMIC_OVERLAY)).thenReturn(AxisURL.DYNAMIC_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT)).thenReturn(AxisURL.TEXT_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.MIRRORING)).thenReturn(AxisURL.MIRRORING);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn(AxisURL.SCHEMA_VERSIONS);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.ROTATION)).thenReturn(AxisURL.ROTATION);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn(AxisURL.VIDEO_FRAME_RATE);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE)).thenReturn(AxisURL.VIDEO_SOURCE);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn(AxisURL.VIDEO_RESOLUTION);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE)).thenReturn("video-source");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> dynamic = extendedStatistic.getDynamicStatistics();

			Assert.assertEquals(AxisConstant.NONE, dynamic.get(AxisMonitoringMetric.VIDEO_SOURCE.toString()));
			Assert.assertEquals("192 x 144", dynamic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.toString()));
			Assert.assertEquals("60", dynamic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.toString()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics retrieve data from ExtendedStatistic data is None
	 * Expect dynamicStatistic not empty videoFrameRate value is None
	 */
	@Test
	public void testGetFailForVideoFrameRateOnlyAndTheRestSuccess() {
		try (MockedStatic<AxisStatisticsFactory> mock = Mockito.mockStatic(AxisStatisticsFactory.class)) {
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn(AxisURL.DEVICE_INFO);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DYNAMIC_OVERLAY)).thenReturn(AxisURL.DYNAMIC_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT)).thenReturn(AxisURL.TEXT_OVERLAY);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.MIRRORING)).thenReturn(AxisURL.MIRRORING);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn(AxisURL.SCHEMA_VERSIONS);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.ROTATION)).thenReturn(AxisURL.ROTATION);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn(AxisURL.VIDEO_FRAME_RATE);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE)).thenReturn(AxisURL.VIDEO_SOURCE);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn(AxisURL.VIDEO_RESOLUTION);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("video-frame-rate-test");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> dynamic = extendedStatistic.getDynamicStatistics();

			Assert.assertEquals(AxisConstant.NONE, dynamic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.toString()));
			Assert.assertEquals(AxisConstant.ACTIVE, dynamic.get(AxisMonitoringMetric.VIDEO_SOURCE.toString()));
			Assert.assertEquals("192 x 144", dynamic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.toString()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics retrieve data from dynamicStatistic
	 * Expect ExtendedStatistic None all field, dynamicStatistic None all except video source not None
	 */
	@Test
	public void testGetFailForAllStatisticExceptVideoSource() {
		try (MockedStatic<AxisStatisticsFactory> mock = Mockito.mockStatic(AxisStatisticsFactory.class)) {
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE)).thenReturn(AxisURL.VIDEO_SOURCE);
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("video-frame-rate-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn("device-info-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.DYNAMIC_OVERLAY)).thenReturn("dynamic-overlay-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT)).thenReturn("text-overlay-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.MIRRORING)).thenReturn("mirroring-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn("schema-version-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.ROTATION)).thenReturn("rotation-test");
			mock.when(() -> AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("video-resolution-test");
			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> dynamic = extendedStatistic.getDynamicStatistics();
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.SCHEMA_VERSIONS.toString()));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.BRAND));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.BUILD_DATE));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_NAME));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.HARD_WARE_ID));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.VERSION));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.WEB_URL));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.SERIAL_NUMBER));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT.toString()));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.ROTATION.toString()));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.MIRRORING.toString()));
			Assert.assertEquals(AxisConstant.NONE, dynamic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.toString()));
			Assert.assertEquals(AxisConstant.NONE, dynamic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.toString()));
			Assert.assertEquals(AxisConstant.ACTIVE, dynamic.get(AxisMonitoringMetric.VIDEO_SOURCE.toString()));
			Assert.assertEquals(AxisConstant.DISABLE, stats.get(AxisMonitoringMetric.DYNAMIC_OVERLAY.toString()));
		}
	}
}
