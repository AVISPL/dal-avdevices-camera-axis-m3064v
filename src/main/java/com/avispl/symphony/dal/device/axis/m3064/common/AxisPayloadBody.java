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
	public static final String SET_TEXT_ENABLE = "action=update&Image.I0.Text.TextEnabled";
	public static final String SET_ROTATION = "action=update&ImageSource.I0.Rotation";
	public static final String SET_TEXT_OVERLAY_CONTENT = "action=update&Image.I0.Text.String";
	public static final String SET_MIRRORING_ENABLE = "action=update&Image.I0.Appearance.MirrorEnabled";

}
