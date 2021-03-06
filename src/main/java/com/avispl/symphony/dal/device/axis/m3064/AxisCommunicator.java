/*
 * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064;

import java.nio.charset.StandardCharsets;
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
import com.avispl.symphony.dal.device.axis.m3064.common.AxisURL;
import com.avispl.symphony.dal.device.axis.m3064.common.IRCutFilterDropdown;
import com.avispl.symphony.dal.device.axis.m3064.common.RotationDropdown;
import com.avispl.symphony.dal.device.axis.m3064.common.TextOverlayAppearanceDropdown;
import com.avispl.symphony.dal.device.axis.m3064.common.TextOverlaySizeDropdown;
import com.avispl.symphony.dal.device.axis.m3064.common.WhiteBalanceDropdown;
import com.avispl.symphony.dal.device.axis.m3064.dto.DeviceInfo;
import com.avispl.symphony.dal.device.axis.m3064.dto.ParameterDefinitions;
import com.avispl.symphony.dal.device.axis.m3064.dto.SchemaVersionStatus;
import com.avispl.symphony.dal.device.axis.m3064.dto.SystemParameterDefinitions;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group.Parameter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.group.SystemParameter;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.ChildParameterItem;
import com.avispl.symphony.dal.device.axis.m3064.dto.metric.groups.groupchild.SystemChildGroupList;
import com.avispl.symphony.dal.util.StringUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

/**
 * An implementation of RestCommunicator to provide communication and interaction with AXIS M3064-V Camera.
 * Supported features are:
 * <p>
 * Monitoring:
 * <ul>
 * 		<li>- Schema versions </li>
 * 		<li>- Video resolution </li>
 * 		<li>- Video frame rate </li>
 * 		<li>- Brand </li>
 * 		<li>- Build day </li>
 * 		<li>- Device name </li>
 * 		<li>- Hardware ID </li>
 * 		<li>- Serial number </li>
 * 		<li>- Version </li>
 * 		<li>- Web URL </li>
 * </ul>
 * <p>
 * Controlling:
 * <ul>
 * 		<li>- Mirroring </li>
 * 		<li>- Rotation </li>
 * 		<li>- Text overlay </li>
 * 		<li>- Text overlay content </li>
 * 		<li>- Saturation </li>
 * 		<li>- Contrast </li>
 * 		<li>- Brightness </li>
 * 		<li>- Sharpness </li>
 * 		<li>- Wide dynamic range </li>
 * 		<li>- White balance </li>
 * 		<li>- IR-cut filter </li>
 * 		<li>- Text overlay size </li>
 * 		<li>- Text overlay appearance </li>
 * </ul>
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
public class AxisCommunicator extends RestCommunicator implements Monitorable, Controller {

	private ExtendedStatistics localExtendedStatistics;
	private Map<String, String> failedMonitor;
	private Integer numberHistorical = null;
	private String axisProtocol = AxisConstant.HTTP;

	/**
	 * AxisCommunicator instantiation
	 */
	public AxisCommunicator() {
		// typically devices do not have trusted certificates, instruct to trust all
		setTrustAllCertificates(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void authenticate() {
		// The device has its own authentication behavior, do not use the common one
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Set content type into in headers
	 */
	@Override
	protected HttpHeaders putExtraRequestHeaders(HttpMethod httpMethod, String uri, HttpHeaders headers) throws Exception {
		headers.set("Content-Type", "text/xml");
		headers.set("Content-Type", "application/json");
		return super.putExtraRequestHeaders(httpMethod, uri, headers);
	}

	/**
	 * This method is called by Symphony to control the properties in the device
	 *
	 * @param list the list ControllableProperty instance
	 * @throws Exception if ControllableProperty list is empty
	 */
	@Override
	public void controlProperties(List<ControllableProperty> list) {
		if (CollectionUtils.isEmpty(list)) {
			throw new IllegalArgumentException("AxisCommunicator: Controllable properties cannot be null or empty");
		}
		retrieveProtocol();
		for (ControllableProperty controllableProperty : list) {
			controlProperty(controllableProperty);
		}
	}

	/**
	 * This method is called by Symphony to control the properties in the device
	 *
	 * @param controllableProperty ControllableProperty instance
	 * @throws Exception if unexpected the value
	 */
	@Override
	public void controlProperty(ControllableProperty controllableProperty) {
		retrieveProtocol();
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
			case TEXT_OVERLAY_CONTENT:
				setControlTextOverlay(value);
				break;
			case TEXT_OVERLAY_APPEARANCE:
				setControlAppearance(value);
				break;
			case WIDE_DYNAMIC_RANGE:
				setControlWideDynamicRange(Integer.parseInt(value));
				break;
			case TEXT_OVERLAY:
				setControlTextOverlayEnable(Integer.parseInt(value));
				break;
			case MIRRORING:
				setControlMirroring(Integer.parseInt(value));
				break;
			case BRIGHTNESS:
				setControlSlider(AxisControllingMetric.BRIGHTNESS, AxisPayloadBody.SET_BRIGHTNESS, (int) Float.parseFloat(value));
				break;
			case CONTRAST:
				setControlSlider(AxisControllingMetric.CONTRAST, AxisPayloadBody.SET_CONTRAST, (int) Float.parseFloat(value));
				break;
			case SATURATION:
				setControlSlider(AxisControllingMetric.SATURATION, AxisPayloadBody.SET_SATURATION, (int) Float.parseFloat(value));
				break;
			case SHARPNESS:
				setControlSlider(AxisControllingMetric.SHARPNESS, AxisPayloadBody.SET_SHARPNESS, (int) Float.parseFloat(value));
				break;
			case WHITE_BALANCE:
				Map<String, String> mapNameWhiteBalance = WhiteBalanceDropdown.getNameToValueMap();
				setControlDropdown(AxisControllingMetric.WHITE_BALANCE, AxisPayloadBody.SET_WHITE_BALANCE, mapNameWhiteBalance.get(value));
				break;
			case IR_CUT_FILTER:
				Map<String, String> mapNameIRCutFilter = IRCutFilterDropdown.getNameToValueMap();
				setControlDropdown(AxisControllingMetric.IR_CUT_FILTER, AxisPayloadBody.SET_IR_CUT_FILTER, mapNameIRCutFilter.get(value));
				break;
			case TEXT_OVERLAY_SIZE:
				Map<String, String> mapNameOverLayTextSize = TextOverlaySizeDropdown.getNameToValueMap();
				setControlDropdown(AxisControllingMetric.TEXT_OVERLAY_SIZE, AxisPayloadBody.SET_TEXT_OVERLAY_SIZE, mapNameOverLayTextSize.get(value));
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + property);
		}
	}

	/**
	 * Set value for metric of the device. The value is a value of the slider
	 *
	 * @param axisControllingMetric is the metric of the device
	 * @param payloadBody is payload action to set the value
	 * @param value is value current of metric
	 * @throws Exception if device set the value fails or device can't call URL
	 */
	private void setControlSlider(AxisControllingMetric axisControllingMetric, String payloadBody, int value) {
		try {
			String responseData = doGet(buildDeviceFullPath(
					AxisStatisticsUtil.getControlURL(axisControllingMetric)) + AxisConstant.QUESTION_MARK + payloadBody + AxisConstant.EQUALS_SIGN + value);
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + axisControllingMetric.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(AxisConstant.ERR_SET_CONTROL + axisControllingMetric.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * Set value for metric of device. The value is a value of the dropdown
	 *
	 * @param axisControllingMetric is the metric of the device
	 * @param payloadBody is payload action to set the value
	 * @param value is value current of metric
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private void setControlDropdown(AxisControllingMetric axisControllingMetric, String payloadBody, String value) {
		try {
			String responseData = doGet(buildDeviceFullPath(
					AxisStatisticsUtil.getControlURL(axisControllingMetric)) + AxisConstant.QUESTION_MARK + payloadBody + AxisConstant.EQUALS_SIGN + value);
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + axisControllingMetric.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(AxisConstant.ERR_SET_CONTROL + axisControllingMetric.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * Set value for metric of device like saturation, contrast, brightness, sharpness
	 *
	 * @param value is value current of metric
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private void setControlAppearance(String value) {
		try {
			Map<String, String> nameToValueTextOverlayMap = TextOverlayAppearanceDropdown.getNameToValueMap();
			String textOverlayValue = nameToValueTextOverlayMap.get(value);
			String colorName = textOverlayValue.substring(0, textOverlayValue.indexOf(AxisConstant.UNDERSCORE));
			String bgColorName = textOverlayValue.substring(textOverlayValue.indexOf(AxisConstant.UNDERSCORE) + 1);
			String responseData = doGet(buildDeviceFullPath(
					AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE)) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_TEXT_OVERLAY_APPEARANCE_COLOR
					+ AxisConstant.EQUALS_SIGN + colorName + AxisPayloadBody.SET_TEXT_OVERLAY_APPEARANCE_BG_COLOR + AxisConstant.EQUALS_SIGN + bgColorName);
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + AxisControllingMetric.TEXT_OVERLAY_APPEARANCE.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(
					AxisConstant.ERR_SET_CONTROL + AxisControllingMetric.TEXT_OVERLAY_APPEARANCE.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * Set value for the wide dynamic range of device
	 *
	 * @param value is value current of metric
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private void setControlWideDynamicRange(int value) {
		try {
			String param = value == 1 ? AxisConstant.ON : AxisConstant.OFF;
			String responseData = doGet(
					buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_WIDE_DYNAMIC_RANGE
							+ AxisConstant.EQUALS_SIGN
							+ param);
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + AxisControllingMetric.WIDE_DYNAMIC_RANGE.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(
					AxisConstant.ERR_SET_CONTROL + AxisControllingMetric.WIDE_DYNAMIC_RANGE.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * This method is called by Symphony to get the list of statistics to be displayed
	 *
	 * @return List<Statistics> This return the list of statistics
	 * @throws Exception if dynamic statistics can't get data
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
	 * @param stats list statistics
	 * @return List<AdvancedControllableProperty>/null this return list of AdvancedControllableProperty if add property success else will is null
	 */
	private List<AdvancedControllableProperty> contributeAdvancedControllableProperties(Map<String, String> control, Map<String, String> stats) {
		List<AdvancedControllableProperty> advancedControllableProperties = new ArrayList<>();
		for (AxisControllingMetric controllingMetric : AxisControllingMetric.values()) {
			switch (controllingMetric) {
				case MIRRORING:
					advancedControllableProperties.add(controlSwitch(control, stats, AxisControllingMetric.MIRRORING));
					break;
				case ROTATION:
					String[] dropdown = RotationDropdown.names();
					AdvancedControllableProperty dropdownRotation = controlDropdown(control, stats, dropdown, AxisControllingMetric.ROTATION);
					if (dropdownRotation != null) {
						advancedControllableProperties.add(dropdownRotation);
					}
					break;
				case WHITE_BALANCE:
					String[] dropdownWhiteBalance = WhiteBalanceDropdown.names();
					AdvancedControllableProperty dropdownControlProperty = controlDropdown(control, stats, dropdownWhiteBalance, AxisControllingMetric.WHITE_BALANCE);
					if (dropdownControlProperty != null) {
						advancedControllableProperties.add(dropdownControlProperty);
					}
					break;
				case IR_CUT_FILTER:
					String[] dropdownIRCutFilter = IRCutFilterDropdown.names();
					AdvancedControllableProperty dropdownIRCutFilterControlProperty = controlDropdown(control, stats, dropdownIRCutFilter, AxisControllingMetric.IR_CUT_FILTER);
					if (dropdownIRCutFilterControlProperty != null) {
						advancedControllableProperties.add(dropdownIRCutFilterControlProperty);
					}
					break;
				case TEXT_OVERLAY_APPEARANCE:
					String[] dropdownTextOverlayAppearance = TextOverlayAppearanceDropdown.names();
					AdvancedControllableProperty dropdownTextOverlayControlProperty = controlDropdown(control, stats, dropdownTextOverlayAppearance, AxisControllingMetric.TEXT_OVERLAY_APPEARANCE);
					if (dropdownTextOverlayControlProperty != null) {
						advancedControllableProperties.add(dropdownTextOverlayControlProperty);
					}
					break;
				case TEXT_OVERLAY_SIZE:
					String[] dropdownTextOverlaySize = TextOverlaySizeDropdown.names();
					AdvancedControllableProperty dropdownTextOverlaySizeControlProperty = controlDropdown(control, stats, dropdownTextOverlaySize, AxisControllingMetric.TEXT_OVERLAY_SIZE);
					if (dropdownTextOverlaySizeControlProperty != null) {
						advancedControllableProperties.add(dropdownTextOverlaySizeControlProperty);
					}
					break;
				case TEXT_OVERLAY_CONTENT:
					advancedControllableProperties.add(controlPropertiesByTextOverlayContent(control, stats));
					break;
				case TEXT_OVERLAY:
					advancedControllableProperties.add(controlSwitch(control, stats, AxisControllingMetric.TEXT_OVERLAY));
					break;
				case WIDE_DYNAMIC_RANGE:
					advancedControllableProperties.add(controlSwitch(control, stats, AxisControllingMetric.WIDE_DYNAMIC_RANGE));
					break;
				case BRIGHTNESS:
					advancedControllableProperties.add(createControlSlider(control, stats, AxisControllingMetric.BRIGHTNESS));
					break;
				case CONTRAST:
					advancedControllableProperties.add(createControlSlider(control, stats, AxisControllingMetric.CONTRAST));
					break;
				case SATURATION:
					advancedControllableProperties.add(createControlSlider(control, stats, AxisControllingMetric.SATURATION));
					break;
				case SHARPNESS:
					advancedControllableProperties.add(createControlSlider(control, stats, AxisControllingMetric.SHARPNESS));
					break;
				default:
					return Collections.emptyList();
			}
		}
		return advancedControllableProperties;
	}

	/**
	 * Add switch is control property for the metric
	 *
	 * @param control list control property
	 * @return AdvancedControllableProperty switch instance if add switch success else will is null
	 */
	private AdvancedControllableProperty controlSwitch(Map<String, String> control, Map<String, String> stats, AxisControllingMetric axisControllingMetric) {
		stats.put(axisControllingMetric.getName(), control.get(axisControllingMetric.getName()));
		if (control.get(axisControllingMetric.getName()).equals(AxisConstant.YES)) {
			return createSwitch(axisControllingMetric.getName(), 1);
		}
		return createSwitch(axisControllingMetric.getName(), 0);
	}

	/**
	 * Create control slider is control property for the metric
	 *
	 * @param name the name of the metric
	 * @param value the value of the metric
	 * @return AdvancedControllableProperty slider instance
	 */
	private AdvancedControllableProperty createSlider(String name, Float value) {
		AdvancedControllableProperty.Slider slider = new AdvancedControllableProperty.Slider();
		slider.setLabelEnd(String.valueOf(AxisConstant.SLIDER_RANGE_END));
		slider.setLabelStart(String.valueOf(AxisConstant.SLIDER_RANGE_START));
		slider.setRangeEnd(Float.valueOf(AxisConstant.SLIDER_RANGE_END));
		slider.setRangeStart(Float.valueOf(AxisConstant.SLIDER_RANGE_START));

		return new AdvancedControllableProperty(name, new Date(), slider, value);
	}

	/**
	 * Create control slider is control property for the metric
	 *
	 * @param control list control property
	 * @param stats list statistics
	 * @param axisControllingMetric instance of AxisControllingMetric
	 * @return AdvancedControllableProperty slider instance if add slider success else will is null
	 */
	private AdvancedControllableProperty createControlSlider(Map<String, String> control, Map<String, String> stats, AxisControllingMetric axisControllingMetric) {
		stats.put(axisControllingMetric.getName(), control.get(axisControllingMetric.getName()));
		if (!control.get(axisControllingMetric.getName()).equals(AxisConstant.NONE)) {
			return createSlider(axisControllingMetric.getName(), Float.valueOf(control.get(axisControllingMetric.getName())));
		}
		return createSlider(axisControllingMetric.getName(), 0f);
	}

	/**
	 * Add text is control property for metric text overlay content
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
	 * Add dropdown is control property for metric
	 *
	 * @param control list control property
	 * @param stats list statistic
	 * @param options list select
	 * @param axisControllingMetric String name of metric
	 * @return AdvancedControllableProperty dropdown instance if add dropdown success else will is null
	 */
	private AdvancedControllableProperty controlDropdown(Map<String, String> control, Map<String, String> stats, String[] options, AxisControllingMetric axisControllingMetric) {
		stats.put(axisControllingMetric.getName(), control.get(axisControllingMetric.getName()));
		if (!control.get(axisControllingMetric.getName()).equals(AxisConstant.NONE)) {
			return createDropdown(axisControllingMetric.getName(), options, control.get(axisControllingMetric.getName()));
		}
		return null;
	}

	/**
	 * Create text is control property for metric
	 *
	 * @param name the name of the property
	 * @param stringValue character string
	 * @return AdvancedControllableProperty Text instance
	 */
	private AdvancedControllableProperty createText(String name, String stringValue) {
		AdvancedControllableProperty.Text text = new AdvancedControllableProperty.Text();
		return new AdvancedControllableProperty(name, new Date(), text, stringValue);
	}

	/**
	 * Create switch is control property for metric
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
	 * Create dropdown advanced controllable property
	 *
	 * @param name the name of the control
	 * @param initialValue initial value of the control
	 * @return AdvancedControllableProperty dropdown instance
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
	 * @param dynamicStatistic list dynamic statistics
	 * @param control list statistics property
	 */
	private void populateAxisStatisticsMetric(Map<String, String> stats, Map<String, String> dynamicStatistic, Map<String, String> control) {
		Objects.requireNonNull(stats);
		Objects.requireNonNull(dynamicStatistic);
		Objects.requireNonNull(control);
		retrieveProtocol();

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

	/**
	 * Check device information before getting data the first time
	 *
	 * @return true if localExtendedStatistics is null or device information not null else is false
	 */
	private boolean isRetrieveDeviceInfoFirstTime() {
		Map<String, String> stats = localExtendedStatistics.getStatistics();
		return stats == null || stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER).equals(AxisConstant.NONE)
				|| stats.get(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION).equals(AxisConstant.NONE);
	}

	/**
	 * Retrieve data from the device
	 *
	 * @param metric list metric of device
	 * @return data from metric of device or none if metric does not exist
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
	 * Retrieve data from the device
	 *
	 * @param metric list metric of device
	 * @return data from metric of device or none if metric does not exist
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
			case TEXT_OVERLAY:
				return retrieveTextOverlayEnable();
			case BRIGHTNESS:
				return retrieveMultipleMetric(metric, AxisPayloadBody.GET_BRIGHTNESS);
			case CONTRAST:
				return retrieveMultipleMetric(metric, AxisPayloadBody.GET_CONTRAST);
			case SATURATION:
				return retrieveMultipleMetric(metric, AxisPayloadBody.GET_SATURATION);
			case SHARPNESS:
				return retrieveMultipleMetric(metric, AxisPayloadBody.GET_SHARPNESS);
			case WIDE_DYNAMIC_RANGE:
				return retrieveWideDynamicRange();
			case WHITE_BALANCE:
				return retrieveWhiteBalance();
			case IR_CUT_FILTER:
				return retrieveIRCutFilter();
			case TEXT_OVERLAY_APPEARANCE:
				return retrieveTextOverlayAppearance();
			case TEXT_OVERLAY_SIZE:
				return retrieveTextOverlaySize();
			default:
				return AxisConstant.NONE;
		}
	}

	/**
	 * Build full URL for the device
	 *
	 * @param path URL of the request
	 * @return String full path of the device
	 */
	private String buildDeviceFullPath(String path) {
		Objects.requireNonNull(path);

		String login = getLogin();
		String password = getPassword();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(axisProtocol);
		if (!StringUtils.isNullOrEmpty(login) && !StringUtils.isNullOrEmpty(password)) {
			stringBuilder.append(login);
			stringBuilder.append(AxisConstant.COLON);
			stringBuilder.append(password);
			stringBuilder.append(AxisConstant.AT);
		}
		stringBuilder.append(getHost());
		stringBuilder.append(AxisConstant.COLON);
		stringBuilder.append(getPort());
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
				ObjectMapper mapper = new ObjectMapper();
				DeviceInfo deviceInfo = mapper.readerFor(DeviceInfo.class).readValue(responseData.get(DeviceInfo.DATA).get(DeviceInfo.PROPERTIES).toString());

				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND, checkNoneData(deviceInfo.getBrand()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE, checkNoneData(deviceInfo.getBuildDate()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID, checkNoneData(deviceInfo.getHardwareID()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME, checkNoneData(deviceInfo.getProdFullName()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER, checkNoneData(deviceInfo.getSerialNumber()));
				stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION, checkNoneData(deviceInfo.getVersion()));
			} else {
				contributeNoneValueForDeviceStatistics(stats);
			}
		} catch (Exception e) {
			contributeNoneValueForDeviceStatistics(stats);
		}
	}

	/**
	 * Value of list statistics property of device info is none
	 *
	 * @param stats list statistics
	 */
	private void contributeNoneValueForDeviceStatistics(Map<String, String> stats) {
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BRAND, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.BUILD_DATE, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.HARD_WARE_ID, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.DEVICE_NAME, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.SERIAL_NUMBER, AxisConstant.NONE);
		stats.put(AxisMonitoringMetric.DEVICE_INFO.getName() + AxisConstant.VERSION, AxisConstant.NONE);
	}

	/**
	 * Retrieve the protocol from the device, try with http first, if any failed, continue try with https
	 *
	 * set the axisProtocol information if retrieving success else is none
	 */
	private void retrieveProtocol() {
		try {
			axisProtocol = getAxisProtocol();
		} catch (Exception e) {
			axisProtocol = AxisConstant.HTTPS;
			try {
				axisProtocol = getAxisProtocol();
			} catch (Exception ex) {
				throw new ResourceNotReachableException(AxisConstant.ERR_PROTOCOL);
			}
		}
	}

	/**
	 * Retrieve the protocol configuration of the device (HTTP, HTTPs or both)
	 *
	 * @return the protocol information if retrieving success else is none
	 */
	public String getAxisProtocol() {
		try {
			SystemParameterDefinitions response = doGet(buildDeviceFullPath(AxisURL.URL_PARAM_CGI) + AxisConstant.QUESTION_MARK + AxisPayloadBody.ACTION + AxisPayloadBody.GET_PROTOCOL,
					SystemParameterDefinitions.class);
			SystemChildGroupList[] systemChildGroup = response.getGroup().getGroupChildren().getSystemChildGroupLists();
			for (int i = 0; i < systemChildGroup.length; i++) {
				SystemChildGroupList systemChildGroupItem = systemChildGroup[i];
				if (AxisConstant.BOA_GROUP_POLICY.equals(systemChildGroupItem.getName())) {
					SystemParameter[] paramSystems = systemChildGroupItem.getSystemParameters();
					String deviceAdminProtocol = getAdminProtocol(paramSystems);
					if (deviceAdminProtocol == null) {
						throw new IllegalStateException("Protocol not found");
					}
					if (AxisConstant.BOTH.equals(deviceAdminProtocol)) {
						return AxisConstant.HTTP;
					} else {
						return deviceAdminProtocol + AxisConstant.PROTOCOL_PREFIX;
					}
				}
			}
			throw new ResourceNotReachableException("Error when getting protocol for " + axisProtocol);
		} catch (Exception e) {
			throw new ResourceNotReachableException("Error when getting protocol for " + axisProtocol);
		}
	}

	/**
	 * Retrieve the protocol for admin only
	 *
	 * @return the protocol information if retrieving success else is none
	 */
	private String getAdminProtocol(SystemParameter[] paramSystems) {
		for (int i = 0; i < paramSystems.length; i++) {
			SystemParameter paramSystem = paramSystems[i];
			if (AxisConstant.ADMIN.equals(paramSystem.getName())) {
				return paramSystem.getValue();
			}
		}
		return null;
	}

	/**
	 * Retrieve schema version information
	 *
	 * @return schema version information if retrieving success else is none
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
	 * @param value value of device information
	 * @return String (none/value)
	 */
	private String checkNoneData(String value) {
		return value.equals("") ? AxisConstant.NONE : value;
	}

	/**
	 * Retrieve rotation information
	 *
	 * @return rotation degrees information if retrieving success else is none
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
	 * @return text overlay enable is boolean if retrieving success else is none
	 */
	private String retrieveTextOverlayEnable() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY)),
					AxisPayloadBody.ACTION + AxisPayloadBody.GET_TEXT_OVERLAY_ENABLE,
					ParameterDefinitions.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			ChildParameterItem parameter = responseData.getGroup().getGroupChild().getChildGroupItem().getParameter();
			return checkNoneData(parameter.getValue());
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve text overlay information
	 *
	 * @return the text overlay information if retrieving success else is none
	 */
	private String retrieveTextOverlay() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_CONTENT)),
					AxisPayloadBody.ACTION + AxisPayloadBody.GET_TEXT_OVERLAY_CONTENT,
					ParameterDefinitions.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			ChildParameterItem parameter = responseData.getGroup().getGroupChild().getChildGroupItem().getParameter();
			return parameter.getValue();
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve mirroring information
	 *
	 * @return mirroring enable information if retrieving success else is none
	 */
	private String retrieveMirroring() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.MIRRORING)), AxisPayloadBody.ACTION + AxisPayloadBody.GET_MIRRORING_ENABLE,
					ParameterDefinitions.class);
			if (responseData == null) {
				return AxisConstant.NONE;
			}
			ChildParameterItem parameter = responseData.getGroup().getGroupChild().getChildGroupItem().getParameter();
			return parameter.getValue();
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve video input frame rate information
	 *
	 * @return video input frame rate information if retrieving success else is none
	 */
	private String retrieveVideoFrameRate() {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_FRAME_RATE)), AxisPayloadBody.GET_VIDEO_FRAME_RATE);
			if (responseData == null) {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_FRAME_RATE.getName(), AxisConstant.NO_RESPONSE_ERR);
				return AxisConstant.NONE;
			}
			String fpsValue = checkNoneData(responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1));
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(fpsValue);
			if (!AxisConstant.NONE.equals(fpsValue)) {
				stringBuilder.append(AxisConstant.FPS);
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			failedMonitor.put(AxisMonitoringMetric.VIDEO_FRAME_RATE.getName(), e.getMessage());
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve resolution information
	 *
	 * @return resolution information if retrieving success else is none
	 */
	private String retrieveResolution() {
		try {
			ParameterDefinitions responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getMonitorURL(AxisMonitoringMetric.VIDEO_RESOLUTION)), AxisPayloadBody.ACTION + AxisPayloadBody.GET_RESOLUTION,
					ParameterDefinitions.class);
			if (responseData == null) {
				failedMonitor.put(AxisMonitoringMetric.VIDEO_RESOLUTION.getName(), AxisConstant.NO_RESPONSE_ERR);
				return AxisConstant.NONE;
			}
			ChildParameterItem parameter = responseData.getGroup().getGroupChild().getChildGroupItem().getParameter();
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
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private void setControlTextOverlay(String value) {
		if (!StringUtils.isNullOrEmpty(value) && value.getBytes(StandardCharsets.UTF_8).length > AxisConstant.MAX_TEXT_LENGTH) {
			throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName() + AxisConstant.TEXT_LENGTH_EXCEEDS_80.replaceAll(
					AxisConstant.NUM_OF_BYTES, String.valueOf(value.length())));
		}
		try {
			String responseData = doGet(buildDeviceFullPath(
					AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_CONTENT) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_TEXT_OVERLAY_CONTENT + AxisConstant.EQUALS_SIGN
							+ value));
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(
					AxisConstant.ERR_SET_CONTROL + AxisControllingMetric.TEXT_OVERLAY_CONTENT.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * Set text for text overlay enable
	 *
	 * @param value this is the value to set for text overlay enable
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private void setControlTextOverlayEnable(int value) {
		try {
			String param = value == 1 ? AxisConstant.YES : AxisConstant.NO;
			String responseData = doGet(
					buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_TEXT_ENABLE + AxisConstant.EQUALS_SIGN
							+ param));
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + AxisControllingMetric.TEXT_OVERLAY.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(
					AxisConstant.ERR_SET_CONTROL + AxisControllingMetric.TEXT_OVERLAY.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * Set mirroring
	 *
	 * @param value this is the value to set for mirroring
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private void setControlMirroring(int value) {
		try {
			String param = value == 1 ? AxisConstant.YES : AxisConstant.NO;
			String responseData = doGet(
					buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.MIRRORING)) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_MIRRORING_ENABLE + AxisConstant.EQUALS_SIGN
							+ param);
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + AxisControllingMetric.MIRRORING.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(
					AxisConstant.ERR_SET_CONTROL + AxisControllingMetric.MIRRORING.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * Set rotation
	 *
	 * @param value this is the value to set for rotation
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private void setControlRotation(String value) {
		try {
			String responseData = doGet(
					buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.ROTATION)) + AxisConstant.QUESTION_MARK + AxisPayloadBody.SET_ROTATION + AxisConstant.EQUALS_SIGN + value);
			if (!AxisConstant.OKE.equals(responseData)) {
				throw new ResourceNotReachableException(AxisConstant.NO_SET_ERR + AxisControllingMetric.ROTATION.getName());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(
					AxisConstant.ERR_SET_CONTROL + AxisControllingMetric.ROTATION.getName() + AxisConstant.WITH_PROPERTY + value + AxisConstant.ERROR_MESSAGE + e.getMessage());
		}
	}

	/**
	 * Retrieve multiple information like saturation, contrast, brightness, sharpness
	 *
	 * @return saturation, contrast, brightness, sharpness information
	 * @throws Exception if device set the value fails or is device can't call URL
	 */
	private String retrieveMultipleMetric(AxisControllingMetric axisControllingMetric, String payloadBody) {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(axisControllingMetric)), payloadBody);
			if (responseData == null || responseData.contains(AxisConstant.ERROR)) {
				return AxisConstant.NONE;
			}
			return checkNoneData(responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1, responseData.length() - 1));
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve wide dynamic range information
	 *
	 * @return wide dynamic range information if retrieving success else is none
	 */
	private String retrieveWideDynamicRange() {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.WIDE_DYNAMIC_RANGE)), AxisPayloadBody.GET_WIDE_DYNAMIC_RANGE);
			if (responseData == null || responseData.contains(AxisConstant.ERROR)) {
				return AxisConstant.NONE;
			}
			String wideDynamicRange = responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1, responseData.length() - 1);
			String result = AxisConstant.NONE;
			if (AxisConstant.ON.equals(wideDynamicRange)) {
				result = AxisConstant.YES;
			}
			if (AxisConstant.OFF.equals(wideDynamicRange)) {
				result = AxisConstant.NO;
			}
			return result;
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve white balance information
	 *
	 * @return white balance information if retrieving success else is none
	 */
	private String retrieveWhiteBalance() {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.WHITE_BALANCE)), AxisPayloadBody.GET_WHITE_BALANCE);
			if (responseData == null || responseData.contains(AxisConstant.ERROR)) {
				return AxisConstant.NONE;
			}
			String whiteBalanceName = responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1, responseData.length() - 1);
			Map<String, String> mapName = WhiteBalanceDropdown.getValueToNameMap();
			return mapName.get(whiteBalanceName);
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve IR-cut filter information
	 *
	 * @return IR-cut filter information if retrieving success else is none
	 */
	private String retrieveIRCutFilter() {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.IR_CUT_FILTER)), AxisPayloadBody.GET_IR_CUT_FILTER);
			if (responseData == null || responseData.contains(AxisConstant.ERROR)) {
				return AxisConstant.NONE;
			}
			String iRCutFilterName = responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1, responseData.length() - 1);
			Map<String, String> mapName = IRCutFilterDropdown.getValueToNameMap();
			return mapName.get(iRCutFilterName);
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve text overlay size information
	 *
	 * @return text overlay size information if retrieving success else is none
	 */
	private String retrieveTextOverlaySize() {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_SIZE)), AxisPayloadBody.GET_TEXT_OVERLAY_SIZE);
			if (responseData == null || responseData.contains(AxisConstant.ERROR)) {
				return AxisConstant.NONE;
			}
			String textOverlayName = responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1, responseData.length() - 1);
			Map<String, String> mapName = TextOverlaySizeDropdown.getValueToNameMap();
			return mapName.get(textOverlayName);
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}

	/**
	 * Retrieve text overlay appearance information
	 *
	 * @return text overlay appearance information if retrieving success else is none
	 */
	private String retrieveTextOverlayAppearance() {
		try {
			String responseData = doPost(buildDeviceFullPath(AxisStatisticsUtil.getControlURL(AxisControllingMetric.TEXT_OVERLAY_APPEARANCE)), AxisPayloadBody.GET_TEXT_OVERLAY_APPEARANCE);
			if (responseData == null || responseData.contains(AxisConstant.ERROR)) {
				return AxisConstant.NONE;
			}
			String nameColor = responseData.substring(responseData.indexOf(AxisConstant.EQUALS_SIGN) + 1, responseData.indexOf(AxisConstant.NEWLINE));
			String bgColor = responseData.substring(responseData.lastIndexOf(AxisConstant.EQUALS_SIGN) + 1, responseData.lastIndexOf(AxisConstant.NEWLINE));
			Map<String, String> mapName = TextOverlayAppearanceDropdown.getValueToNameMap();
			return mapName.get(nameColor + AxisConstant.UNDERSCORE + bgColor);
		} catch (Exception e) {
			return AxisConstant.NONE;
		}
	}
}
