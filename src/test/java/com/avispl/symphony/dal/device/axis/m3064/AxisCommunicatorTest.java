/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
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
 * @version 1.0
 * @since 1.0
 */
public class AxisCommunicatorTest {

	AxisCommunicator axisCommunicator = new AxisCommunicator();
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(80).httpsPort(443)
			.bindAddress("127.0.0.1"));

	@Before
	public void setUp() throws Exception {
		axisCommunicator.setHost("127.0.0.1");
		axisCommunicator.setProtocol("https");
		axisCommunicator.setLogin("root");
		axisCommunicator.setPassword("1234");
		axisCommunicator.setPort(443);
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

			Assert.assertEquals("25 fps", dynamicStatistic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.getName()));
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
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY)).thenReturn("/text-overlay-enable");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.IR_CUT_FILTER)).thenReturn("/ir-cut-filter");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE)).thenReturn("/text-overlay-appearance");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_SIZE)).thenReturn("/text-overlay-size");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WHITE_BALANCE)).thenReturn("/white-balance");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/wide-dynamic-range");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.SHARPNESS)).thenReturn("/sharpness");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.SATURATION)).thenReturn("/saturation");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.CONTRAST)).thenReturn("/contrast");
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.BRIGHTNESS)).thenReturn("/brightness");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			List<AdvancedControllableProperty> controllableProperties = extendedStatistic.getControllableProperties();
			Assert.assertEquals(13, controllableProperties.size());
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
				if (property.getName().equals(AxisControllingMetric.TEXT_OVERLAY.getName())) {
					Assert.assertEquals(0, property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.SHARPNESS.getName())) {
					Assert.assertEquals(50F, property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.CONTRAST.getName())) {
					Assert.assertEquals(50F, property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.BRIGHTNESS.getName())) {
					Assert.assertEquals(50F, property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.WHITE_BALANCE.getName())) {
					Assert.assertEquals("Auto", property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.WIDE_DYNAMIC_RANGE.getName())) {
					Assert.assertEquals(1, property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE.getName())) {
					Assert.assertEquals("White on Black", property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.TEXT_OVERLAY_SIZE.getName())) {
					Assert.assertEquals("Small", property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.SATURATION.getName())) {
					Assert.assertEquals(50F, property.getValue());
				}
				if (property.getName().equals(AxisControllingMetric.IR_CUT_FILTER.getName())) {
					Assert.assertEquals("Auto", property.getValue());
				}
			}
		}
	}

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
			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect device info get data success but device info is null
	 */
	@Test
	public void testAxisCommunicatorDeviceInfoIsNullFailed() {
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
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY)).thenReturn("/text-overlay-enable-error");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisControllingMetric.TEXT_OVERLAY.getName()));
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

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveSaturationSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.SATURATION)).thenReturn("/saturation");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("50", stats.get(AxisControllingMetric.SATURATION.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveContrastSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.CONTRAST)).thenReturn("/contrast");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("50", stats.get(AxisControllingMetric.CONTRAST.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveSharpnessSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.SHARPNESS)).thenReturn("/sharpness");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("50", stats.get(AxisControllingMetric.SHARPNESS.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveWideDynamicRangesSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/wide-dynamic-range");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("yes", stats.get(AxisControllingMetric.WIDE_DYNAMIC_RANGE.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveWhiteBalanceSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WHITE_BALANCE)).thenReturn("/white-balance");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("Auto", stats.get(AxisControllingMetric.WHITE_BALANCE.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveIRCutFilterSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.IR_CUT_FILTER)).thenReturn("/ir-cut-filter");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("Auto", stats.get(AxisControllingMetric.IR_CUT_FILTER.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveTextOverlaySizeSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_SIZE)).thenReturn("/text-overlay-size");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("Small", stats.get(AxisControllingMetric.TEXT_OVERLAY_SIZE.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get data success
	 */
	@Test
	public void testAxisCommunicatorRetrieveTextOverlayAppearanceSuccessfully() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE)).thenReturn("/text-overlay-appearance");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals("White on Black", stats.get(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE.getName()));
		}
	}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success
	 * Expect get text overlay appearance none data
	 */
	@Test
	public void testAxisCommunicatorRetrieveTextOverlayAppearanceFailed() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE)).thenReturn("/text-overlay-appearance-null");
			mock.when(() -> AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)).thenReturn("/video-resolution");

			ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
			Map<String, String> stats = extendedStatistic.getStatistics();

			Assert.assertEquals(AxisConstant.NONE, stats.get(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE.getName()));
		}
	}
}
