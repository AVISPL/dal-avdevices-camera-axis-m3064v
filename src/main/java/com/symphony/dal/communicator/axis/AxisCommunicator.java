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
import com.symphony.dal.communicator.axis.dto.DeviceInfo;
import com.symphony.dal.communicator.axis.dto.SchemaVersionStatus;
import com.symphony.dal.communicator.axis.dto.VideoOutput;
import com.symphony.dal.communicator.axis.dto.metric.Device;

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
		final Map<String, String> dynamic = new HashMap<>();
		failedMonitor = new HashMap<>();
		if (localExtendedStatistics == null) {
			localExtendedStatistics = new ExtendedStatistics();
		}
		populateAxisStatisticsMetric(stats, dynamic);

		int numberHistorical = getNoOfHistoricalMetric();
		// if it is failed to get all the historical stats
		if (failedMonitor.size() == numberHistorical) {
			StringBuilder errBuilder = new StringBuilder();
			for (Map.Entry<String, String> metric : failedMonitor.entrySet()) {
				errBuilder.append(metric.getValue());
			}
			throw new ResourceNotReachableException(errBuilder.toString());
		} else {

			AggregatedDevice dynamicStatistics = new AggregatedDevice();
			dynamicStatistics.setProperties(dynamic);
			Map<String, String> dynamicProperties = dynamicStatistics.getProperties();

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
	 * @param dynamic list statistics property
	 */
	private void populateAxisStatisticsMetric(Map<String, String> stats, Map<String, String> dynamic) {
		Objects.requireNonNull(stats);
		retrieveInfoDevice(stats);
		for (AxisMonitoringMetric metric : AxisMonitoringMetric.values()) {
			if (metric.isHistorical()) {
				dynamic.put(metric.toString(), retrieveDataByMetric(metric));
			} else {
				if (AxisMonitoringMetric.DEVICE_INFO.equals(metric)) {
					continue;
				}
				stats.put(metric.toString(), retrieveDataByMetric(metric));
			}
		}
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
	private void retrieveInfoDevice(Map<String, String> stats) {
		ObjectNode request = JsonNodeFactory.instance.objectNode();
		request.put(AxisRequest.API_VERSION, AxisRequest.API_VERSION_VALUE);
		request.put(AxisRequest.CONTEXT, AxisRequest.REQUEST_CONTEXT);
		request.put(AxisRequest.METHOD, AxisRequest.GET_ALL_PROPERTIES);
		try {
			JsonNode responseData = doPost(AxisStatisticsFactory.getURL(AxisMonitoringMetric.DEVICE_INFO),
					request, JsonNode.class);
			Gson gson = new Gson();
			Device deviceInfo = gson.fromJson(responseData.get(DeviceInfo.DATA).get(DeviceInfo.PROPERTIES).toString(), Device.class);

			stats.put(AxisMonitoringMetric.BRAND, deviceInfo.getBrand());
			stats.put(AxisMonitoringMetric.BUILD_DATE, deviceInfo.getBuildDate());
			stats.put(AxisMonitoringMetric.HARD_WARE_ID, deviceInfo.getHardwareID());
			stats.put(AxisMonitoringMetric.DEVICE_NAME, deviceInfo.getProdFullName());
			stats.put(AxisMonitoringMetric.SERIAL_NUMBER, deviceInfo.getSerialNumber());
			stats.put(AxisMonitoringMetric.VERSION, deviceInfo.getVersion());
			stats.put(AxisMonitoringMetric.WEB_URL, deviceInfo.getWebURL());
		} catch (Exception e) {
			stats.put(AxisMonitoringMetric.BRAND, AxisConstant.NONE);
			stats.put(AxisMonitoringMetric.BUILD_DATE, AxisConstant.NONE);
			stats.put(AxisMonitoringMetric.HARD_WARE_ID, AxisConstant.NONE);
			stats.put(AxisMonitoringMetric.DEVICE_NAME, AxisConstant.NONE);
			stats.put(AxisMonitoringMetric.SERIAL_NUMBER, AxisConstant.NONE);
			stats.put(AxisMonitoringMetric.VERSION, AxisConstant.NONE);
			stats.put(AxisMonitoringMetric.WEB_URL, AxisConstant.NONE);
			logger.error("Get device info error: " + e.getMessage());
		}
	}

	/**
	 * Retrieve video source information
	 *
	 * @return video source information
	 */
	private String retrieveVideoSource() {
		try {
			VideoOutput responseData = doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_SOURCE) + "?"
					+ AxisMonitoringMetric.SCHEMA_VERSION + "=1&" + AxisRequest.CAMERA + "=1", VideoOutput.class);
			if (responseData.getSuccess() != null) {
				boolean videoSource = responseData.getSuccess().getVideoSourcesSuccess().getVideoSource().isActive();
				if (videoSource) {
					return AxisConstant.ACTIVE;
				}
				return AxisConstant.INACTIVE;
			} else {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_SOURCE.toString(), responseData.getError().getGeneralError().getErrorDescription());
				return AxisConstant.NONE;
			}
		} catch (Exception e) {
			logger.error("get video source error: " + e.getMessage());
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
			SchemaVersionStatus responseData = doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.SCHEMA_VERSIONS), SchemaVersionStatus.class);
			if (responseData.getSuccess() != null) {
				return responseData.getSuccess().getSchemaVersionsSuccess().getSchemaVersion().getVersionNumber();
			}
			return AxisConstant.NONE;
		} catch (Exception e) {
			logger.error("get schemaVersion error: " + e.getMessage());
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
			VideoOutput responseData = doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.ROTATION)
					+ "?" + AxisMonitoringMetric.SCHEMA_VERSION + "=1", VideoOutput.class);
			if (responseData.getSuccess() != null) {
				return String.valueOf(responseData.getSuccess().getRotationSuccess().getRotation().getDegrees());
			}
			return AxisConstant.NONE;
		} catch (Exception e) {
			logger.error("Get rotation error: " + e.getMessage());
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
			VideoOutput responseData = doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.DYNAMIC_OVERLAY)
					+ "?" + AxisMonitoringMetric.SCHEMA_VERSION + "=1", VideoOutput.class);

			if (responseData.getSuccess() != null) {
				boolean dynamicOverlay = responseData.getSuccess().getDynamicOverlaySuccess().isDynamicOverlaysEnabled();
				if (dynamicOverlay) {
					return AxisConstant.ENABLE;
				}
				return AxisConstant.DISABLE;
			}
			return AxisConstant.NONE;
		} catch (Exception e) {
			logger.error("Get dynamic overlay error: " + e.getMessage());
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
			VideoOutput responseData = doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT)
					+ "?" + AxisMonitoringMetric.SCHEMA_VERSION + "=1", VideoOutput.class);
			if (responseData.getSuccess() != null) {
				return responseData.getSuccess().getTextOverlaySuccess().getTextOverlay().getCurrentValue();
			}
			return AxisConstant.NONE;
		} catch (Exception e) {
			logger.error("Get text overlay error: " + e.getMessage());
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
			VideoOutput responseData = doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.MIRRORING)
					+ "?" + AxisMonitoringMetric.SCHEMA_VERSION + "=1", VideoOutput.class);
			if (responseData.getSuccess() != null) {
				boolean mirroringEnable = responseData.getSuccess().getMirroringSuccess().isMirroringEnable();
				if (mirroringEnable) {
					return AxisConstant.ENABLE;
				}
				return AxisConstant.DISABLE;
			}
			return AxisConstant.NONE;
		} catch (Exception e) {
			logger.error("Get mirroring error: " + e.getMessage());
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
			return doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_FRAME_RATE));
		} catch (Exception e) {
			logger.error("Get video frame rate error: " + e.getMessage());
			String error = e.getMessage().substring(e.getMessage().lastIndexOf("response") + 10, e.getMessage().length()) + "\n";
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
			String responseData = doGet(AxisStatisticsFactory.getURL(AxisMonitoringMetric.VIDEO_RESOLUTION)
					+ "?" + AxisMonitoringMetric.SCHEMA_VERSION + "=1");
			if (responseData.lastIndexOf("image") > 0) {
				String width = responseData.substring(responseData.indexOf("=") + 2, responseData.indexOf("\n"));
				String height = responseData.substring(responseData.lastIndexOf("=") + 2, responseData.lastIndexOf("\n"));
				return width + " x " + height;
			} else {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.toString(), responseData);
				return AxisConstant.NONE;
			}
		} catch (Exception e) {
			logger.error("Get video resolution error: " + e);
			failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.toString(), e.getMessage());
			return AxisConstant.NONE;
		}
	}
}
