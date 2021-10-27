/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.communicator.RestCommunicator;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisConstant;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisControllingMetric;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisMonitoringMetric;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisPayloadBody;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisRequest;
import com.avispl.symphony.dal.device.axis.m3064.common.AxisStatisticsUtil;
import com.avispl.symphony.dal.device.axis.m3064.dto.DeviceInfo;
import com.avispl.symphony.dal.device.axis.m3064.dto.ParameterDefinitions;
import com.avispl.symphony.dal.device.axis.m3064.dto.SchemaVersionStatus;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group.Parameter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.ParameterChildItem;
import com.avispl.symphony.dal.util.StringUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

public class AxisCommunicator extends RestCommunicator implements Monitorable, Controller {

	private ExtendedStatistics localExtendedStatistics;
	private Map<String, String> failedMonitor;

	private Integer numberHistorical = null;

	@Override
	protected void authenticate() {
		// The device has its own authentication behavior, do not use the common one
	}

	@Override
	protected HttpHeaders putExtraRequestHeaders(HttpMethod httpMethod, String uri, HttpHeaders headers) throws Exception {
		headers.set("Content-Type", "text/xml");
		headers.set("Content-Type", "application/json");
		return super.putExtraRequestHeaders(httpMethod, uri, headers);
	}

	@Override
	public void controlProperties(List<ControllableProperty> list) {
		if (CollectionUtils.isEmpty(list)) {
			throw new IllegalArgumentException("NetGearCommunicator: Controllable properties cannot be null or empty");
		}
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) {
		String property = controllableProperty.getProperty();
		String value = String.valueOf(controllableProperty.getValue());
		if (logger.isDebugEnabled()) {
			logger.debug("controlProperty property" + property);
			logger.debug("controlProperty value" + value);
		}
		AxisControllingMetric axisControllingMetric = AxisControllingMetric.getByName(property);
		switch (axisControllingMetric) {
			case ROTATION:
				setControlRotation(value);
				break;
			case TEXT_OVERLAY_ENABLE:
				setControlTextOverlayEnable(Integer.parseInt(value));
				break;
			case MIRRORING:
				setControlMirroring(Integer.parseInt(value));
				break;
			case TEXT_OVERLAY_CONTENT:
				setControlTextOverlay(value);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + property);
		}
	}

	/**
	 * This method is called by Symphony to get the list of statistics to be displayed
	 *
	 * @return List<Statistics> This return the list of statistics
	 */
	@Override
	public List<Statistics> getMultipleStatistics() {
		final Map<String, String> stats = new HashMap<>();
		final Map<String, String> dynamicStatistics = new HashMap<>();
		Map<String, String> control = new HashMap<>();
		failedMonitor = new HashMap<>();
		if (localExtendedStatistics == null) {
			localExtendedStatistics = new ExtendedStatistics();
		}
		populateAxisStatisticsMetric(stats, dynamicStatistics, control);
		List<AdvancedControllableProperty> advancedControllableProperties = contributeAdvancedControllableProperties(control, stats);

		localExtendedStatistics.setDynamicStatistics(dynamicStatistics);
		localExtendedStatistics.setControllableProperties(advancedControllableProperties);
		localExtendedStatistics.setStatistics(stats);

		return Collections.singletonList(localExtendedStatistics);
	}

	/**
	 * Count metric historical in the metrics
	 *
	 * @return number historical in the metric
	 */
	private int getNoOfHistoricalMetric() {
		int noOfHistoricalMetric = 0;
		for (AxisMonitoringMetric metric : AxisMonitoringMetric.values()) {
			if (metric.isHistorical()) {
				noOfHistoricalMetric++;
			}
		}
		return noOfHistoricalMetric;
	}

	/**
	 * Add AdvancedControllableProperties for the metric
	 *
	 * @param control list control property
	 * @param stats list control property
	 * @return List<AdvancedControllableProperty> this return list of AdvancedControllableProperty
	 */
	private List<AdvancedControllableProperty> contributeAdvancedControllableProperties(Map<String, String> control, Map<String, String> stats) {
		List<AdvancedControllableProperty> advancedControllableProperties = new ArrayList<>();
		for (AxisControllingMetric controllingMetric : AxisControllingMetric.values()) {
			switch (controllingMetric) {
				case MIRRORING:
					advancedControllableProperties.add(controlSwitch(control, stats, AxisControllingMetric.MIRRORING));
					break;
				case ROTATION:
					String[] dropdown = AxisConstant.ROTATION_DROPDOWN;
					advancedControllableProperties.add(controlPropertiesByRotation(control, stats, dropdown));
					break;
				case TEXT_OVERLAY_CONTENT:
					advancedControllableProperties.add(controlPropertiesByTextOverlayContent(control, stats));
					break;
				case TEXT_OVERLAY_ENABLE:
					advancedControllableProperties.add(controlSwitch(control, stats, AxisControllingMetric.TEXT_OVERLAY_ENABLE));
					break;
				default:
					return Collections.emptyList();
			}
		}
		return advancedControllableProperties;
	}

	/**
	 * Add controlProperties for controlling metric
	 *
	 * @param control list control property
	 * @return AdvancedControllableProperty switch instance
	 */
	private AdvancedControllableProperty controlSwitch(Map<String, String> control, Map<String, String> stats, AxisControllingMetric axisControllingMetric) {
		stats.put(axisControllingMetric.getName(), control.get(axisControllingMetric.getName()));
		if (!control.get(axisControllingMetric.getName()).equals(AxisConstant.NONE)) {
			return control.get(axisControllingMetric.getName()).equals(AxisConstant.YES) ? createSwitch(axisControllingMetric.getName(), 1)
					: createSwitch(axisControllingMetric.getName(), 0);
		}
		return null;
	}

	/**
	 * Add controlProperties for metric text overlay content
	 *
	 * @param control list control property
	 * @return AdvancedControllableProperty Text instance
	 */
	private AdvancedControllableProperty controlPropertiesByTextOverlayContent(Map<String, String> control, Map<String, String> stats) {
		stats.put(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName(), control.get(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName()));
		return control.get(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName()) == null ? createText(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName(), "")
				: createText(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName(), control.get(AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName()));
	}

	/**
	 * Add controlProperties for metric text overlay rotation
	 *
	 * @return AdvancedControllableProperty Dropdown instance
	 */
	private AdvancedControllableProperty controlPropertiesByRotation(Map<String, String> control, Map<String, String> stats, String[] options) {
		stats.put(AxisControllingMetric.ROTATION.getName(), control.get(AxisControllingMetric.ROTATION.getName()));
		if (!control.get(AxisControllingMetric.ROTATION.getName()).equals(AxisConstant.NONE)) {
			return createDropdown(AxisControllingMetric.ROTATION.getName(), options, control.get(AxisControllingMetric.ROTATION.getName()));
		}
		return null;
	}

	/**
	 * Create a controllable property Text
	 *
	 * @param name the name of property
	 * @param stringValue character string
	 * @return AdvancedControllableProperty Text instance
	 */
	private AdvancedControllableProperty createText(String name, String stringValue) {
		AdvancedControllableProperty.Text text = new AdvancedControllableProperty.Text();
		return new AdvancedControllableProperty(name, new Date(), text, stringValue);
	}

	/**
	 * Create a controllable property switch
	 *
	 * @param name the name of property
	 * @param status initial status (0|1)
	 * @return AdvancedControllableProperty switch instance
	 */
	private AdvancedControllableProperty createSwitch(String name, int status) {
		AdvancedControllableProperty.Switch toggle = new AdvancedControllableProperty.Switch();
		toggle.setLabelOff(AxisConstant.DISABLE);
		toggle.setLabelOn(AxisConstant.ENABLE);

		AdvancedControllableProperty advancedControllableProperty = new AdvancedControllableProperty();
		advancedControllableProperty.setName(name);
		advancedControllableProperty.setValue(status);
		advancedControllableProperty.setType(toggle);
		advancedControllableProperty.setTimestamp(new Date());

		return advancedControllableProperty;
	}

	/***
	 * Create AdvancedControllableProperty preset instance
	 * @param name name of the control
	 * @param initialValue initial value of the control
	 * @return AdvancedControllableProperty preset instance
	 */
	private AdvancedControllableProperty createDropdown(String name, String[] values, String initialValue) {
		AdvancedControllableProperty.DropDown dropDown = new AdvancedControllableProperty.DropDown();
		dropDown.setOptions(values);
		dropDown.setLabels(values);

		return new AdvancedControllableProperty(name, new Date(), dropDown, initialValue);
	}

	/**
	 * Retrieve data and add to stats and dynamic
	 *
	 * @param stats list statistics property
	 * @param dynamicStatistic list statistics property
	 * @param control list statistics property
	 */
	private void populateAxisStatisticsMetric(Map<String, String> stats, Map<String, String> dynamicStatistic, Map<String, String> control) {
		Objects.requireNonNull(stats);
		Objects.requireNonNull(dynamicStatistic);
		Objects.requireNonNull(control);

		if (isRetrieveDeviceInfoFirstTime()) {
			retrieveDeviceInfo(stats);
		} else {
			Map<String, String> localStatistics = localExtendedStatistics.getStatistics();
			String deviceInfo = AxisMonitoringMetric.DEVICE_INFO.getName();

			stats.put(deviceInfo + AxisConstant.BRAND, localStatistics.get(deviceInfo + AxisConstant.BRAND));
			stats.put(deviceInfo + AxisConstant.BUILD_DATE, localStatistics.get(deviceInfo + AxisConstant.BUILD_DATE));
			stats.put(deviceInfo + AxisConstant.HARD_WARE_ID, localStatistics.get(deviceInfo + AxisConstant.HARD_WARE_ID));
			stats.put(deviceInfo + AxisConstant.DEVICE_NAME, localStatistics.get(deviceInfo + AxisConstant.DEVICE_NAME));
			stats.put(deviceInfo + AxisConstant.SERIAL_NUMBER, localStatistics.get(deviceInfo + AxisConstant.SERIAL_NUMBER));
			stats.put(deviceInfo + AxisConstant.VERSION, localStatistics.get(deviceInfo + AxisConstant.VERSION));
			stats.put(deviceInfo + AxisConstant.WEB_URL, localStatistics.get(deviceInfo + AxisConstant.WEB_URL));
		}

		for (AxisMonitoringMetric monitoringMetric : AxisMonitoringMetric.values()) {
			if (monitoringMetric.isHistorical()) {
				dynamicStatistic.put(monitoringMetric.getName(), retrieveDataByMetric(monitoringMetric));
			} else {
				if (!AxisMonitoringMetric.DEVICE_INFO.equals(monitoringMetric)) {
					stats.put(monitoringMetric.getName(), retrieveDataByMetric(monitoringMetric));
				}
			}
		}
		if (numberHistorical == null) {
			numberHistorical = getNoOfHistoricalMetric();
		}
		if (failedMonitor.size() == numberHistorical) {
			StringBuilder errBuilder = new StringBuilder();
			for (Map.Entry<String, String> failedMetric : failedMonitor.entrySet()) {
				errBuilder.append(failedMetric.getValue());
			}
			throw new ResourceNotReachableException(errBuilder.toString());
		}
		for (AxisControllingMetric controllingMetric : AxisControllingMetric.values()) {
			control.put(controllingMetric.getName(), retrieveDataControlByMetric(controllingMetric));
		}
	}

	private boolean isRetrieveDeviceInfoFirstTime() {
		Map<String, String> stats = localExtendedStatistics.getStatistics();
		return stats == null || stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.WEB_URL).equals(AxisConstant.NONE);
	}

	/**
	 * Retrieve data from device
	 *
	 * @param metric list metric of device
	 * @return data from metric of device or null
	 */
	private String retrieveDataByMetric(AxisMonitoringMetric metric) {
		Objects.requireNonNull(metric);

		switch (metric) {
			case VIDEO_RESOLUTION:
				return retrieveResolution();
			case VIDEO_FRAME_RATE:
				return retrieveVideoFrameRate();
			case SCHEMA_VERSIONS:
				return retrieveSchemaVersion();
			default:
				return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve data from device
	 *
	 * @param metric list metric of device
	 * @return data from metric of device or null
	 */
	private String retrieveDataControlByMetric(AxisControllingMetric metric) {
		Objects.requireNonNull(metric);
		switch (metric) {
			case TEXT_OVERLAY_CONTENT:
				return retrieveTextOverlay();
			case MIRRORING:
				return retrieveMirroring();
			case ROTATION:
				return retrieveRotation();
			case TEXT_OVERLAY_ENABLE:
				return retrieveTextOverlayEnable();
			default:
				return AxisConstant.NONE;
		}
	}

	/**
	 * @param path url of the request
	 * @return String full path of the device
	 */
	private String buildDeviceFullPath(String path) {
		Objects.requireNonNull(path);

		String login = getLogin();
		String password = getPassword();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(AxisConstant.HTTP);
		if (!StringUtils.isNullOrEmpty(login) && !StringUtils.isNullOrEmpty(password)) {
			stringBuilder.append(login);
			stringBuilder.append(AxisConstant.COLON);
			stringBuilder.append(password);
			stringBuilder.append(AxisConstant.AT);
		}
		stringBuilder.append(getHost());
		stringBuilder.append(path);

		return stringBuilder.toString();
	}

	/**
	 * Retrieve device information
	 *
	 * @param stats list statistics property
	 */
	private void retrieveDeviceInfo(Map<String, String> stats) {
		ObjectNode request = JsonNodeFactory.instance.objectNode();
		request.put(AxisRequest.API_VERSION, AxisRequest.API_VERSION_VALUE);
		request.put(AxisRequest.CONTEXT, AxisRequest.REQUEST_CONTEXT);
		request.put(AxisRequest.METHOD, AxisRequest.GET_ALL_PROPERTIES);
		try {
			JsonNode responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.DEVICE_INFO)), request, JsonNode.class);
			if (responseData != null) {
				Gson gson = new Gson();
				DeviceInfo deviceInfo = gson.fromJson(responseData.get(DeviceInfo.DATA).get(DeviceInfo.PROPERTIES).toString(), DeviceInfo.class);

				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND, checkNoneData(deviceInfo.getBrand()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE, checkNoneData(deviceInfo.getBuildDate()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID, checkNoneData(deviceInfo.getHardwareID()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME, checkNoneData(deviceInfo.getProdFullName()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER, checkNoneData(deviceInfo.getSerialNumber()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION, checkNoneData(deviceInfo.getVersion()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.WEB_URL, checkNoneData(deviceInfo.getWebURL()));
			} else {
				contributeNoneValueForDeviceStatistics(stats);
			}
		} catch (Exception e) {
			contributeNoneValueForDeviceStatistics(stats);
		}
	}

	/**
	 * Value of list statistics property of device info is NONE
	 *
	 * @param stats list statistics property
	 */
	private void contributeNoneValueForDeviceStatistics(Map<String, String> stats) {
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.WEB_URL, AxisConstant.NONE);
	}

	/**
	 * Retrieve schema version information
	 *
	 * @return schema version  information
	 */
	private String retrieveSchemaVersion() {
		try {
			SchemaVersionStatus responseData = doGet(buildDeviceFullPath(AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.SCHEMA_VERSIONS)), SchemaVersionStatus.class);
			if (responseData == null || responseData.getError() != null) {
				return AxisConstant.NONE;
			}
			return checkNoneData(responseData.getSuccess().getSchemaVersionsSuccess().getSchemaVersion().getVersionNumber());
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * @param value value of device info
	 * @return String (none/value)
	 */
	private String checkNoneData(String value) {
		return value.equals("") ? AxisConstant.NONE : value;
	}

	/**
	 * Retrieve rotation information
	 *
	 * @return rotation degrees information
	 */
	private String retrieveRotation() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.ROTATION)), AxisPayloadBody.ACTION + AxisPayloadBody.GET_ROTATION,
					ParameterDefinitions.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			Parameter parameter = responseData.getGroup().getGroupChild().getParameter();
			return checkNoneData(parameter.getValue());
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve text overlay enable information
	 *
	 * @return boolean
	 */
	private String retrieveTextOverlayEnable() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_ENABLE)),
					AxisPayloadBody.ACTION + AxisPayloadBody.GET_TEXT_OVERLAY_ENABLE,
					ParameterDefinitions.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			ParameterChildItem parameter = responseData.getGroup().getGroupChild().getGroupChildItem().getParameter();
			return checkNoneData(parameter.getValue());
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve text overlay information
	 *
	 * @return the text overlay information
	 */
	private String retrieveTextOverlay() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_CONTENT)),
					AxisPayloadBody.ACTION + AxisPayloadBody.GET_TEXT_OVERLAY_CONTENT,
					ParameterDefinitions.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			ParameterChildItem parameter = responseData.getGroup().getGroupChild().getGroupChildItem().getParameter();
			return parameter.getValue();
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve mirroring information
	 *
	 * @return mirroring enable information
	 */
	private String retrieveMirroring() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.MIRRORING)), AxisPayloadBody.ACTION + AxisPayloadBody.GET_MIRRORING_ENABLE,
					ParameterDefinitions.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			ParameterChildItem parameter = responseData.getGroup().getGroupChild().getGroupChildItem().getParameter();
			return parameter.getValue();
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve video input frame rate information
	 *
	 * @return video input frame rate information
	 */
	private String retrieveVideoFrameRate() {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)), AxisPayloadBody.GET_VIDEO_FRAME_RATE);
			if (responseData == null) {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_FRAME_RATE.getName(), AxisConstant.NO_RESPONSE_ERR);
				return AxisConstant.NONE;
			}
			int len = responseData.length();
			return checkNoneData(responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1, len));
		} catch (Exception e) {
			failedMonitor.put(AxisMonitoringMetric.VIDEO_FRAME_RATE.getName(), e.getMessage());
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve resolution information
	 *
	 * @return resolution information
	 */
	private String retrieveResolution() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)), AxisPayloadBody.ACTION + AxisPayloadBody.GET_RESOLUTION,
					ParameterDefinitions.class);
			if (responseData == null) {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.getName(), AxisConstant.NO_RESPONSE_ERR);
				return AxisConstant.NONE;
			}
			ParameterChildItem parameter = responseData.getGroup().getGroupChild().getGroupChildItem().getParameter();
			return checkNoneData(parameter.getValue());
		} catch (Exception e) {
			failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.getName(), e.getMessage());
			return AxisConstant.NONE;
		}
	}

	/**
	 * Set text for text overlay
	 *
	 * @param value this is the value to set for text overlay
	 */
	private void setControlTextOverlay(String value) {
		try {
			String responseData = doGet(buildDeviceFullPath(
					AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_CONTENT) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_TEXT_OVERLAY_CONTENT + AxisConstant.EQUALS_SIGN
							+ value));
			if (!responseData.equals(AxisConstant.OKE)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR);
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(e.getMessage());
		}
	}

	/**
	 * Set text for text overlay enable
	 *
	 * @param value this is the value to set for text overlay enable
	 */
	private void setControlTextOverlayEnable(int value) {
		try {
			String param = value == 1 ? AxisConstant.YES : AxisConstant.NO;
			String responseData = doGet(
					buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_ENABLE) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_TEXT_ENABLE + AxisConstant.EQUALS_SIGN
							+ param));
			if (!responseData.equals(AxisConstant.OKE)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR);
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(e.getMessage());
		}
	}

	/**
	 * Set mirroring
	 *
	 * @param value this is the value to set for mirroring
	 */
	private void setControlMirroring(int value) {
		try {
			String param = value == 1 ? AxisConstant.YES : AxisConstant.NO;
			String responseData = doGet(
					buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.MIRRORING)) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_MIRRORING_ENABLE + AxisConstant.EQUALS_SIGN
							+ param);
			if (!responseData.equals(AxisConstant.OKE)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR);
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(e.getMessage());
		}
	}

	/**
	 * Set rotation
	 *
	 * @param value this is the value to set for rotation
	 */
	private void setControlRotation(String value) {
		try {
			String responseData = doGet(
					buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.ROTATION)) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_ROTATION + AxisConstant.EQUALS_SIGN + value);
			if (!responseData.equals(AxisConstant.OKE)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR);
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(e.getMessage());
		}
	}
}
