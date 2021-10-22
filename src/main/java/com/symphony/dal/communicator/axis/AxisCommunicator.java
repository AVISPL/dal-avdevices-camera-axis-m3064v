/*
 * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.dto.monitor.aggregator.AggregatedDevice;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.communicator.RestCommunicator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.symphony.dal.communicator.axis.common.AxisConstant;
import com.symphony.dal.communicator.axis.common.AxisMonitoringMetric;
import com.symphony.dal.communicator.axis.common.AxisRequest;
import com.symphony.dal.communicator.axis.common.AxisStatisticsFactory;
import com.symphony.dal.communicator.axis.dto.SchemaVersionStatus;
import com.symphony.dal.communicator.axis.dto.VideoOutput;
import com.symphony.dal.communicator.axis.dto.DeviceInfo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * This class supports monitoring and controlling from Symphony platform to Axis device
 */
public class AxisCommunicator extends RestCommunicator implements Monitorable {

	private ExtendedStatistics localExtendedStatistics;
	private Map<String, String> failedMonitor;

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

	/**
	 * This method is called by Symphony to get the list of statistics to be displayed
	 *
	 * @return List<Statistics> This return the list of statistics
	 */
	@Override
	public List<Statistics> getMultipleStatistics() {
		final Map<String, String> stats = new HashMap<>();
		final Map<String, String> dynamicStatistics = new HashMap<>();
		failedMonitor = new HashMap<>();
		if (localExtendedStatistics == null) {
			localExtendedStatistics = new ExtendedStatistics();
		}
		populateAxisStatisticsMetric(stats, dynamicStatistics);

		int numberHistorical = getNoOfHistoricalMetric();
		if (failedMonitor.size() == numberHistorical) {
			StringBuilder errBuilder = new StringBuilder();
			for (Map.Entry<String, String> metric : failedMonitor.entrySet()) {
				errBuilder.append(metric.getValue());
			}
			throw new ResourceNotReachableException(errBuilder.toString());
		} else {

			AggregatedDevice aggregatedDevice = new AggregatedDevice();
			aggregatedDevice.setProperties(dynamicStatistics);
			Map<String, String> dynamicProperties = aggregatedDevice.getProperties();

			localExtendedStatistics.setDynamicStatistics(dynamicProperties);
			localExtendedStatistics.setStatistics(stats);
		}

		return Collections.singletonList(localExtendedStatistics);
	}

	/**
	 * Count metric historical in the metrics
	 *
	 * @return number historical in the metric
	 */
	private int getNoOfHistoricalMetric() {
		int numberHistorical = 0;
		for (AxisMonitoringMetric metric : AxisMonitoringMetric.values()) {
			if (metric.isHistorical()) {
				numberHistorical++;
			}
		}
		return numberHistorical;
	}

	/**
	 * Retrieve data and add to stats and dynamic
	 *
	 * @param stats list statistics property
	 * @param dynamicStatistic list statistics property
	 */
	private void populateAxisStatisticsMetric(Map<String, String> stats, Map<String, String> dynamicStatistic) {
		Objects.requireNonNull(stats);
		retrieveDeviceInfo(stats);
		for (AxisMonitoringMetric metric : AxisMonitoringMetric.values()) {
			if (metric.isHistorical()) {
				dynamicStatistic.put(metric.toString(), retrieveDataByMetric(metric));
			} else {
				if (AxisMonitoringMetric.DEVICE_INFO.equals(metric)) {
					continue;
				}
				stats.put(metric.toString(), retrieveDataByMetric(metric));
			}
		}
	}

	/**
	 * @param path url of the request
	 * @return String full path of the device
	 */
	private String buildPath(String path) {
		Objects.requireNonNull(path);

		String login = getLogin();
		String password = getPassword();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(AxisConstant.HTTP);
		if (!isEmpty(login) && !isEmpty(password)) {
			stringBuilder.append(login);
			stringBuilder.append(AxisConstant.COLON);
			stringBuilder.append(password);
			stringBuilder.append(AxisConstant.AT);
		}
		stringBuilder.append(getHost());
		stringBuilder.append(":5500");
		stringBuilder.append(path);

		return stringBuilder.toString();
	}

	/**
	 * @param stringData field value
	 * @return (true / false)
	 */
	private boolean isEmpty(String stringData) {
		return stringData == null || stringData.equals("");
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
			case VIDEO_SOURCE:
				return retrieveVideoSource();
			case VIDEO_RESOLUTION:
				return retrieveResolution();
			case VIDEO_FRAME_RATE:
				return retrieveVideoFrameRate();
			case ROTATION:
				return retrieveRotation();
			case SCHEMA_VERSIONS:
				return retrieveSchemaVersion();
			case TEXT_OVERLAY_CONTENT:
				return retrieveTextOverlay();
			case MIRRORING:
				return retrieveMirroring();
			case DYNAMIC_OVERLAY:
				return retrieveDynamicOverlay();
			default:
				return "";
		}
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
			JsonNode responseData = doPost(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.DEVICE_INFO)), request, JsonNode.class);
			if (responseData != null) {
				Gson gson = new Gson();
				DeviceInfo deviceInfo = gson.fromJson(responseData.get(DeviceInfo.DATA).get(DeviceInfo.PROPERTIES).toString(), DeviceInfo.class);

				stats.put(AxisMonitoringMetric.BRAND, checkNoneDataDevice(deviceInfo.getBrand()));
				stats.put(AxisMonitoringMetric.BUILD_DATE, checkNoneDataDevice(deviceInfo.getBuildDate()));
				stats.put(AxisMonitoringMetric.HARD_WARE_ID, checkNoneDataDevice(deviceInfo.getHardwareID()));
				stats.put(AxisMonitoringMetric.DEVICE_NAME, checkNoneDataDevice(deviceInfo.getProdFullName()));
				stats.put(AxisMonitoringMetric.SERIAL_NUMBER, checkNoneDataDevice(deviceInfo.getSerialNumber()));
				stats.put(AxisMonitoringMetric.VERSION, checkNoneDataDevice(deviceInfo.getVersion()));
				stats.put(AxisMonitoringMetric.WEB_URL, checkNoneDataDevice(deviceInfo.getWebURL()));
			} else {
				statisticDeviceInfoIsNull(stats);
			}
		} catch (Exception e) {
			statisticDeviceInfoIsNull(stats);
		}
	}

	/**
	 * @param value value of device info
	 * @return String (none/value)
	 */
	private String checkNoneDataDevice(String value) {
		return value.equals("") ? AxisConstant.NONE : value;
	}

	/**
	 * value of list statistics property of device info is NONE
	 *
	 * @param stats list statistics property
	 */
	private void statisticDeviceInfoIsNull(Map<String, String> stats) {
		stats.put(AxisMonitoringMetric.BRAND, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.BUILD_DATE, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.HARD_WARE_ID, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_NAME, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.SERIAL_NUMBER, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.VERSION, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.WEB_URL, AxisConstant.NONE);
	}

	/**
	 * Retrieve video source information
	 *
	 * @return video source information
	 */
	private String retrieveVideoSource() {
		try {
			VideoOutput responseData = doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE))
					+ AxisConstant.QUESTION_MARK + AxisMonitoringMetric.SCHEMA_VERSION + AxisConstant.EQUALS
					+ AxisConstant.ONE + AxisConstant.AND + AxisRequest.CAMERA + AxisConstant.EQUALS + AxisConstant.ONE, VideoOutput.class);
			if (responseData == null || responseData.getError() != null) {
				return AxisConstant.NONE;
			}
			boolean videoSource = responseData.getSuccess().getVideoSourcesSuccess().getVideoSource().isActive();
			return videoSource ? AxisConstant.ACTIVE : AxisConstant.INACTIVE;
		} catch (Exception e) {
			failedMonitor.put(AxisMonitoringMetric.VIDEO_SOURCE.toString(), e.getMessage());
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve schema version information
	 *
	 * @return schema version  information
	 */
	private String retrieveSchemaVersion() {
		try {
			SchemaVersionStatus responseData = doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.SCHEMA_VERSIONS)), SchemaVersionStatus.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			String schemaVersion = responseData.getSuccess().getSchemaVersionsSuccess().getSchemaVersion().getVersionNumber();
			return checkNoneDataDevice(schemaVersion);
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve rotation information
	 *
	 * @return rotation degrees information
	 */
	private String retrieveRotation() {
		try {
			VideoOutput responseData = doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.ROTATION))
					+ AxisConstant.QUESTION_MARK + AxisMonitoringMetric.SCHEMA_VERSION + AxisConstant.EQUALS + AxisConstant.ONE, VideoOutput.class);
			if (responseData == null || responseData.getError() != null) {
				return AxisConstant.NONE;
			}
			return checkNoneDataDevice(String.valueOf(responseData.getSuccess().getRotationSuccess().getRotation().getDegrees()));
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve dynamic overlay information
	 *
	 * @return dynamic overlay enable information (Yes/No)
	 */
	private String retrieveDynamicOverlay() {
		try {
			VideoOutput responseData = doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.DYNAMIC_OVERLAY))
					+ AxisConstant.QUESTION_MARK + AxisMonitoringMetric.SCHEMA_VERSION + AxisConstant.EQUALS + AxisConstant.ONE, VideoOutput.class);
			if (responseData == null || responseData.getError() != null) {
				return AxisConstant.DISABLE;
			}
			boolean dynamicOverlay = responseData.getSuccess().getDynamicOverlaySuccess().isDynamicOverlaysEnabled();
			return dynamicOverlay ? AxisConstant.ENABLE : AxisConstant.DISABLE;
		} catch (Exception e) {
			return AxisConstant.DISABLE;
		}
	}

	/**
	 * Retrieve text overlay information
	 *
	 * @return the text overlay information
	 */
	private String retrieveTextOverlay() {
		try {
			VideoOutput responseData = doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT))
					+ AxisConstant.QUESTION_MARK + AxisMonitoringMetric.SCHEMA_VERSION + AxisConstant.EQUALS + AxisConstant.ONE, VideoOutput.class);
			if (responseData == null || responseData.getError() != null) {
				return AxisConstant.NONE;
			}
			return checkNoneDataDevice(responseData.getSuccess().getTextOverlaySuccess().getTextOverlay().getCurrentValue());
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
			VideoOutput responseData = doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.MIRRORING)) + AxisConstant.QUESTION_MARK
					+ AxisMonitoringMetric.SCHEMA_VERSION + AxisConstant.EQUALS + AxisConstant.ONE, VideoOutput.class);
			if (responseData == null || responseData.getError() == null) {
				return AxisConstant.DISABLE;
			}
			boolean mirroringEnable = responseData.getSuccess().getMirroringSuccess().isMirroringEnable();
			return mirroringEnable ? AxisConstant.ENABLE : AxisConstant.DISABLE;
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
			return checkNoneDataDevice(doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE))));
		} catch (Exception e) {
			String error = e.getMessage().substring(e.getMessage().lastIndexOf(AxisRequest.RESPONSE) + 10, e.getMessage().length()) + "\n";
			failedMonitor.put(AxisMonitoringMetric.VIDEO_FRAME_RATE.toString(), error);
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
			String responseData = doGet(buildPath(AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_RESOLUTION))
					+ AxisConstant.QUESTION_MARK + AxisMonitoringMetric.SCHEMA_VERSION + AxisConstant.EQUALS + AxisConstant.ONE);
			if (responseData == null) {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.toString(), responseData);
				return AxisConstant.NONE;
			}
			if (responseData.lastIndexOf(AxisConstant.IMAGE) > 0) {
				String width = responseData.substring(responseData.indexOf(AxisConstant.EQUALS) + 2, responseData.indexOf("\n"));
				String height = responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS) + 2, responseData.lastIndexOf("\n"));
				return width + " x " + height;
			} else {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.toString(), responseData);
				return AxisConstant.NONE;
			}
		} catch (Exception e) {
			failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.toString(), e.getMessage());
			return AxisConstant.NONE;
		}
	}
}
