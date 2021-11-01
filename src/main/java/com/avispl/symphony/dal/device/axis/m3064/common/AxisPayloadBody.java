/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.common;

/**
 * AxisPayloadBody class provides the body for POST requests in both the monitoring and controlling process
 *
 * @author Ivan
 * @since 1.0
 */
public class AxisPayloadBody {

	public static final String ACTION = "action=listdefinitions&listformat=xmlschema&responseformat=rfc&responsecharset=utf8&group=";
	public static final String GET_MIRRORING_ENABLE = "Image.I0.Appearance.MirrorEnabled";
	public static final String GET_ROTATION = "ImageSource.I0.Rotation";
	public static final String GET_TEXT_OVERLAY_ENABLE = "Image.I0.Text.TextEnabled";
	public static final String GET_TEXT_OVERLAY_CONTENT = "Image.I0.Text.String";
	public static final String GET_VIDEO_FRAME_RATE = "action=list&group=Image.I0.Stream.FPS";
	public static final String GET_RESOLUTION = "Image.I0.Appearance.Resolution";
	public static final String GET_SATURATION = "action=list&group=ImageSource.I0.Sensor.ColorLevel";
	public static final String GET_CONTRAST = "action=list&group=ImageSource.I0.Sensor.Contrast";
	public static final String GET_BRIGHTNESS = "action=list&group=ImageSource.I0.Sensor.Brightness";
	public static final String GET_SHARPNESS = "action=list&group=ImageSource.I0.Sensor.Sharpness";
	public static final String GET_WIDE_DYNAMIC_RANGE = "action=list&group=ImageSource.I0.Sensor.WDR";
	public static final String GET_WHITE_BALANCE = "action=list&group=ImageSource.I0.Sensor.WhiteBalance";
	public static final String GET_IR_CUT_FILTER = "action=list&group=ImageSource.I0.DayNight.IrCutFilter";
	public static final String GET_TEXT_OVERLAY_SIZE = "action=list&group=Image.I0.Text.TextSize";
	public static final String GET_TEXT_OVERLAY_APPEARANCE = "action=list&group=Image.I0.Text.Color%2CImage.I0.Text.BGColor";
	public static final String SET_TEXT_ENABLE = "action=update&Image.I0.Text.TextEnabled";
	public static final String SET_ROTATION = "action=update&ImageSource.I0.Rotation";
	public static final String SET_TEXT_OVERLAY_CONTENT = "action=update&Image.I0.Text.String";
	public static final String SET_MIRRORING_ENABLE = "action=update&Image.I0.Appearance.MirrorEnabled";
	public static final String SET_SATURATION = "action=update&ImageSource.I0.Sensor.ColorLevel";
	public static final String SET_CONTRAST = "action=update&ImageSource.I0.Sensor.Contrast";
	public static final String SET_BRIGHTNESS = "action=update&ImageSource.I0.Sensor.Brightness";
	public static final String SET_SHARPNESS = "action=update&ImageSource.I0.Sensor.Sharpness";
	public static final String SET_WIDE_DYNAMIC_RANGE = "action=update&ImageSource.I0.Sensor.WDR";
	public static final String SET_WHITE_BALANCE = "action=update&ImageSource.I0.Sensor.WhiteBalance";
	public static final String SET_IR_CUT_FILTER = "action=update&ImageSource.I0.DayNight.IrCutFilter";
	public static final String SET_TEXT_OVERLAY_SIZE = "action=update&Image.I0.Text.TextSize";
	public static final String SET_TEXT_OVERLAY_APPEARANCE_COLOR = "action=update&Image.I0.Text.Color";
	public static final String SET_TEXT_OVERLAY_APPEARANCE_BG_COLOR = "&Image.I0.Text.BGColor";

}
