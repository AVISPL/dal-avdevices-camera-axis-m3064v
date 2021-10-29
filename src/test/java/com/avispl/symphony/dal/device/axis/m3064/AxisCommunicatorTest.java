/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064;

import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisControllingMetric;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisMonitoringMetric;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisStatisticsUtil;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisURL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

/**
 * Unit test for AxisCommunicator
 * ExtendedStatistics and DynamicStatistics retrieve data success
 *
 * @author Ivan
 * @since 1.0
 */
public class AxisCommunicatorTest {

	AxisCommunicator axisCommunicator = new AxisCommunicator();
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(80).httpsPort(8088)
			.bindAddress("127.0.0.1"));

	@Before
	public void setUp() throws Exception {
		axisCommunicator.setHost("127.0.0.1");
		axisCommunicator.setProtocol("http");
		axisCommunicator.setPort(80);
		axisCommunicator.init();
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect device info get data success
	 */
	@Test
	public void testAxisCommunicatorDeviceInfoHaveData() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn(AxisURL.DEVICE_INFO);
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("AXIS", stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND));
			Assert.assertEquals("Feb 14 2018 13:08", stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE));
			Assert.assertEquals("AXIS Q3505 Mk II Fixed Dome Network Camera", stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME));
			Assert.assertEquals("714.4", stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID));
			Assert.assertEquals("8.20.1", stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION));
			Assert.assertEquals("http://www.axis.com", stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.WEB_URL));
			Assert.assertEquals("ACCC8E78B977", stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect schema version get data success
	 */
	@Test
	public void testAxisCommunicatorSchemaVersionNotEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn(AxisURL.SCHEMA_VERSIONS);
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("1.6.0", stats.get(AxisMonitoringMetric.SCHEMA_VERSIONS.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect dynamicStatistics have data are video frame rate, video resolution and video source
	 */
	@Test
	public void testAxisCommunicatorRetrieveDataFromDynamicStatistics() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("/video-frame-rate");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> dynamicStatistic = extendedStatistic.getDynamicStatistics();

			Assert.assertEquals("25", dynamicStatistic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.getName()));
			Assert.assertEquals("1280x720", dynamicStatistic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success with ControlProperties data
	 * Expect ControlProperties the metric not empty
	 */
	@Test
	public void testAxisCommunicatorGetControllablePropertyTheMetricNoEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn(AxisURL.DEVICE_INFO);
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn(AxisURL.SCHEMA_VERSIONS);
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("/video-frame-rate");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.MIRRORING)).thenReturn("/mirroring");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.ROTATION)).thenReturn("/rotation");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_CONTENT)).thenReturn("/text-overlay-content");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_ENABLE)).thenReturn("/text-overlay-enable");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			List<AdvancedControllableProperty> controllableProperties = extendedStatistic.getControllableProperties();
			Assert.assertEquals(4, controllableProperties.size());
			for (AdvancedControllableProperty property : controllableProperties) {
				if (property.getName().equals(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName())) {
					Assert.assertEquals("new text", property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.ROTATION.getName())) {
					Assert.assertEquals("0", property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.MIRRORING.getName())) {
					Assert.assertEquals(0, property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.TEXT_OVERLAY_ENABLE.getName())) {
					Assert.assertEquals(0, property.getValue());
				}
			}
		}
	}

	//test the metric get data is none

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect device info get data error and device info is NONE
	 */
	@Test
	public void testAxisCommunicatorDeviceInfoIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn("/device-error");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.WEB_URL));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect device info get data success but device info is null
	 */
	@Test
	public void testAxisCommunicatorDeviceInfoIsNullField() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn("/device-error-null");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.WEB_URL));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect schema version get data error and schema version is NONE
	 */
	@Test
	public void testAxisCommunicatorGetSchemaVersionIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn("/schema-version-error");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.SCHEMA_VERSIONS.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect video frame rate is NONE
	 */
	@Test
	public void testAxisCommunicatorRetrieveVideoFrameRateIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("/video-frame-rate-error");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> dynamicStatistic = extendedStatistic.getDynamicStatistics();

			Assert.assertEquals(AxisConstant.NONE, dynamicStatistic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect video resolution is NONE
	 */
	@Test
	public void testAxisCommunicatorRetrieveVideoResolutionIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("/video-frame-rate");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution-error");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> dynamicStatistic = extendedStatistic.getDynamicStatistics();

			Assert.assertEquals(AxisConstant.NONE, dynamicStatistic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect mirroring is NONE
	 */
	@Test
	public void testAxisCommunicatorRetrieveMirroringIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.MIRRORING)).thenReturn("/mirroring-error");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisControllingMetric.MIRRORING.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect rotation is NONE
	 */
	@Test
	public void testAxisCommunicatorRetrieveRotationIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.ROTATION)).thenReturn("/rotation-error");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisControllingMetric.ROTATION.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect text overlay content is NONE
	 */
	@Test
	public void testAxisCommunicatorRetrieveTestOverlayContentIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_CONTENT)).thenReturn("/text-overlay-content-error");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect text overlay enable is NONE
	 */
	@Test
	public void testAxisCommunicatorRetrieveTestOverlayEnableIsEmpty() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_ENABLE)).thenReturn("/text-overlay-enable-error");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisControllingMetric.TEXT_OVERLAY_ENABLE.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics with Api error message
	 * Expect throw error message
	 */
	@Test
	public void testAxisCommunicatorThrowExcept() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)).thenReturn("/video-frame-rate-error");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution-error");
			assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.getMultipleStatistics(),
					"Error response received from: 127.0.0.1. Request: http://127.0.0.1/video-resolution-error; status: 400; "
							+ "response: Error response received from: 127.0.0.1. Request: http://127.0.0.1/video-frame-rate-error; status: 400; response: ");
		}
	}

	//test response data is null
	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect device info get data success but device info is null body response
	 */
	@Test
	public void testAxisCommunicatorDeviceInfoIsNullResponse() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.DEVICE_INFO)).thenReturn("/device-error-null-body");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.WEB_URL));
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success with statistics
	 * Expect schema version get data success and response is null
	 */
	@Test
	public void testAxisCommunicatorGetSchemaVersionNullResponse() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.SCHEMA_VERSIONS)).thenReturn("/schema-version-error-null");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.SCHEMA_VERSIONS.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success with statistics
	 * Expect rotation get data success and response is null
	 */
	@Test
	public void testAxisCommunicatorRetrieveRotationNullResponse() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.ROTATION)).thenReturn("/rotation-error-null");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");
			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisControllingMetric.ROTATION.getName()));
		}
	}
}
