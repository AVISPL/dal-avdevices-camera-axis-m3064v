/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisControllingMetric;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisStatisticsUtil;

/**
 * Unit Test for controlling metric
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class AxisCommunicatorControlTest {

	@Spy
	@InjectMocks
	private AxisCommunicator axisCommunicator;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(80).httpsPort(443)
			.bindAddress("127.0.0.1"));

	@Before
	public void setUp() throws Exception {
		axisCommunicator.setHost("127.0.0.1");
		axisCommunicator.setProtocol("http");
		axisCommunicator.setPort(443);
		axisCommunicator.init();
	}

	/**
	 * Set text overlay with value is enable
	 * Expect verify with url with argument TextEnabled=yes
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testTextOverlayEnableWithValueIsEnable() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY.getName());
		controllableProperty.setValue(1);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.TextEnabled=yes");
	}

	/**
	 * Set text overlay with value is disable
	 * Expect verify with url with argument TextEnabled=no
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testTextOverlayEnableWithValueIsDisable() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY.getName());
		controllableProperty.setValue(0);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.TextEnabled=no");
	}

	/**
	 * Set text overlay content with value is "The text overlay"
	 * Expect verify url with argument String is "The text overlay"
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testTextOverlayContentWithValueIsString() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName());
		controllableProperty.setValue("The text overlay");
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.String=The text overlay");
	}

	/**
	 * Set rotation with value is 0
	 * Expect verify with url with argument Rotation=0
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testRotationWithValueIsZero() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.ROTATION.getName());
		controllableProperty.setValue(0);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=0");
	}

	/**
	 * Set rotation with value is 90
	 * Expect verify with url with argument Rotation=90
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testRotationWithValueIs90() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.ROTATION.getName());
		controllableProperty.setValue(90);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=90");
	}

	/**
	 * Set rotation with value is 180
	 * Expect verify with url with argument Rotation=180
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testRotationWithValueIs180() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.ROTATION.getName());
		controllableProperty.setValue(180);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=180");
	}

	/**
	 * Set rotation with value is 270
	 * Expect verify with url with argument Rotation=270
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testRotationWithValueIs270() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.ROTATION.getName());
		controllableProperty.setValue(270);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=270");
	}

	/**
	 * Set mirroring with value is enable
	 * Expect verify with url with argument MirrorEnabled=yes
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testMirroringEnableWithValueIsEnable() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.MIRRORING.getName());
		controllableProperty.setValue(1);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Appearance.MirrorEnabled=yes");
	}

	/**
	 * Set mirroring with value is disable
	 * Expect verify with url with argument MirrorEnabled=no
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testMirroringEnableWithValueIsDisable() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.MIRRORING.getName());
		controllableProperty.setValue(0);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Appearance.MirrorEnabled=no");
	}

	/**
	 * Set rotation with value is 100
	 * Expect throw exception can't set value
	 */
	@Test
	public void testRotationSetValueFail() {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.ROTATION.getName());
		controllableProperty.setValue(100);
		assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty),
				"Error response received from: 127.0.0.1. Request: https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=100; status: 400; response: Error");
	}

	/**
	 * Set text overlay content none
	 * Expect throw exception can't set value
	 */
	@Test
	public void testTextOverlayContentFail() {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName());
		controllableProperty.setValue("none");
		assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty),
				"Error response received from: 127.0.0.1. Request: https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.String=none; status: 400; response: Error");
	}

	/**
	 * Set brightness with value is 50
	 * Expect verify with url with argument brightness=50
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testSetSaturationValueSuccessfully() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.SATURATION.getName());
		controllableProperty.setValue(50);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Sensor.ColorLevel=50");
	}

	/**
	 * Set sharpness with value is 50
	 * Expect verify with url with argument sharpness=50
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testSetSharpnessValueSuccessfully() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.SHARPNESS.getName());
		controllableProperty.setValue(50);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Sensor.Sharpness=50");
	}

	/**
	 * Set wide dynamic range with value is on
	 * Expect verify with url with argument WDR=on
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testSetWideDynamicRangeValueIsOn() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.WIDE_DYNAMIC_RANGE.getName());
		controllableProperty.setValue("1");
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Sensor.WDR=on");
	}

	/**
	 * Set white balance with value is auto
	 * * Expect verify with url with argument WhiteBalance=auto
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testSetWhiteBalanceIsAuto() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.WHITE_BALANCE.getName());
		controllableProperty.setValue("Auto");
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Sensor.WhiteBalance=auto");
	}

	/**
	 * Set IRCutFilter with value is auto
	 * Expect verify with url with argument IrCutFilter=auto
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testSetIRCutFilterValueIsAuto() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.IR_CUT_FILTER.getName());
		controllableProperty.setValue("Auto");
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.DayNight.IrCutFilter=auto");
	}

	/**
	 * Set text overlay size with value is small
	 * Expect verify with url with argument TextSize=mall
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testSetTextOverlaySizeValueIsSmall() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_SIZE.getName());
		controllableProperty.setValue("Small");
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.TextSize=small");
	}

	/**
	 * Set text overlay appearance with value is white on black
	 * Expect verify with url with argument brightness=50
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testSetTextOverlayAppearanceValueIsWhiteOnBlack() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE.getName());
		controllableProperty.setValue("White on Black");
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("https://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.Color=white&Image.I0.Text.BGColor=black");
	}


	/**
	 * Set saturation with value is 50
	 * Expect verify with url with argument Brightness=50 can't set the value
	 */
	@Test
	public void testSetBrightnessWithValueFailed() {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.BRIGHTNESS.getName());
		controllableProperty.setValue(120);
		assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
	}

	/**
	 * Set Saturation with value is 120
	 * Expect verify with url with argument Saturation=120 can't set the value
	 */
	@Test
	public void testSetSaturationWithValueFailed() {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.SATURATION.getName());
		controllableProperty.setValue(120);
		assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
	}

	/**
	 * Set Contrast with value is 120
	 * Expect verify with url with argument Contrast=120 can't set the value
	 */
	@Test
	public void testSetContrastWithValueFailed() {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.CONTRAST.getName());
		controllableProperty.setValue(120);
		assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
	}

	/**
	 * Set Sharpness with value is 120
	 * Expect verify with url with argument Sharpness=120 can't set the value
	 */
	@Test
	public void testSetSharpnessWithValueFailed() {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.SHARPNESS.getName());
		controllableProperty.setValue(120);
		assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
	}

	/**
	 * Set wide dynamic range with value is off
	 * Expect verify with url with argument WideDynamicRange=off can't set the value
	 */
	@Test
	public void testSetWideDynamicRangeWithValueIsOffFail() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/dynamic-range");
			ControllableProperty controllableProperty = new ControllableProperty();
			controllableProperty.setProperty(AxisControllingMetric.WIDE_DYNAMIC_RANGE.getName());
			controllableProperty.setValue(0);
			assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
		}
	}

	/**
	 * Set wide dynamic range with value is on
	 * Expect verify with url with argument WideDynamicRange=off can't set the value
	 */
	@Test
	public void testSetWideDynamicRangeWithValueIsOnFail() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/dynamic-range");
			ControllableProperty controllableProperty = new ControllableProperty();
			controllableProperty.setProperty(AxisControllingMetric.WIDE_DYNAMIC_RANGE.getName());
			controllableProperty.setValue(1);
			assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
		}
	}

	/**
	 * Set white balance with value is auto
	 * Expect verify with url with argument WhiteBalance=auto can't set the value
	 */
	@Test
	public void testSetWhiteBalanceWithValueFailed() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/white-balance");
			ControllableProperty controllableProperty = new ControllableProperty();
			controllableProperty.setProperty(AxisControllingMetric.WHITE_BALANCE.getName());
			controllableProperty.setValue("Auto");
			assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
		}
	}

	/**
	 * Set iR cut filter with value is auto
	 * Expect verify with url with argument IrCutFilter=auto can't set the value
	 */
	@Test
	public void testSetIRCutFilterWithValueFailed() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/ir-cut-filter");
			ControllableProperty controllableProperty = new ControllableProperty();
			controllableProperty.setProperty(AxisControllingMetric.IR_CUT_FILTER.getName());
			controllableProperty.setValue("Auto");
			assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
		}
	}

	/**
	 * Set text overlay size with value is small
	 * Expect verify with url with argument TextSize=small can't set the value
	 */
	@Test
	public void testSetTextOverlaySizeWithValueFailed() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/text-overlay-size");
			ControllableProperty controllableProperty = new ControllableProperty();
			controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_SIZE.getName());
			controllableProperty.setValue("Small");
			assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
		}
	}

	/**
	 * Set text overlay appearance with value is white on black
	 * Expect verify with url with argument Color=white and BGColor=black can't set the value
	 */
	@Test
	public void testSetTextOverlayAppearanceValueFailed() {
		try (MockedStatic<AxisStatisticsUtil> mock = Mockito.mockStatic(AxisStatisticsUtil.class)) {
			mock.when(() -> AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)).thenReturn("/text-overlay-appearance");
			ControllableProperty controllableProperty = new ControllableProperty();
			controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE.getName());
			controllableProperty.setValue("White on Black");
			assertThrows(ResourceNotReachableException.class, () -> axisCommunicator.controlProperty(controllableProperty), "The request failed can't set value");
		}
	}
}