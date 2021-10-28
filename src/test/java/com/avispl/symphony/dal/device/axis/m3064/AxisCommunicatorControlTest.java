/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;

/**
 * Unit Test for controlling metric
 */
@RunWith(MockitoJUnitRunner.class)
public class AxisCommunicatorControlTest {

	@Spy
	@InjectMocks
	private AxisCommunicator axisCommunicator;

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
	 * Set text overlay with value is enable
	 * Expect verify with url with argument TextEnabled=yes
	 *
	 * @throws Exception if the url is not correct
	 */
	@Test
	public void testTextOverlayEnableWithValueIsEnable() throws Exception {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_ENABLE.getName());
		controllableProperty.setValue(1);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.TextEnabled=yes");
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
		controllableProperty.setProperty(AxisControllingMetric.TEXT_OVERLAY_ENABLE.getName());
		controllableProperty.setValue(0);
		axisCommunicator.controlProperty(controllableProperty);
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.TextEnabled=no");
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
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.String=The text overlay");
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
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=0");
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
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=90");
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
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=180");
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
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=270");
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
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Appearance.MirrorEnabled=yes");
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
		Mockito.verify(axisCommunicator, times(1)).doGet("http://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Appearance.MirrorEnabled=no");
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
				"Error response received from: 127.0.0.1. Request: http://127.0.0.1/axis-cgi/param.cgi?action=update&ImageSource.I0.Rotation=100; status: 400; response: Error");
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
				"Error response received from: 127.0.0.1. Request: http://127.0.0.1/axis-cgi/param.cgi?action=update&Image.I0.Text.String=none; status: 400; response: Error");
	}
}
