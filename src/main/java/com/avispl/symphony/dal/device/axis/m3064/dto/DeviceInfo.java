/*
 *  * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

/**
 * All properties provided by the BDI (Basic device information) service.
 *
 * @author Ivan
 * @since 1.0
 */
public class DeviceInfo {

	public static final String DATA = "data";
	public static final String PROPERTIES = "propertyList";

	@SerializedName("Brand")
	private String brand;

	@SerializedName("BuildDate")
	private String buildDate;

	@SerializedName("HardwareID")
	private String hardwareID;

	@SerializedName("ProdFullName")
	private String prodFullName;

	@SerializedName("SerialNumber")
	private String serialNumber;

	@SerializedName("Version")
	private String version;

	@SerializedName("WebURL")
	private String webURL;

	public DeviceInfo() {
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getHardwareID() {
		return hardwareID;
	}

	public void setHardwareID(String hardwareID) {
		this.hardwareID = hardwareID;
	}

	public String getProdFullName() {
		return prodFullName;
	}

	public void setProdFullName(String prodFullName) {
		this.prodFullName = prodFullName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeviceInfo that = (DeviceInfo) o;
		return Objects.equals(brand, that.brand)
				&& Objects.equals(buildDate, that.buildDate)
				&& Objects.equals(hardwareID, that.hardwareID)
				&& Objects.equals(prodFullName, that.prodFullName)
				&& Objects.equals(version, that.version)
				&& Objects.equals(webURL, that.webURL);
	}

	@Override
	public int hashCode() {
		return Objects.hash(brand, buildDate, hardwareID, prodFullName, serialNumber, version, webURL);
	}

	@Override
	public String toString() {
		return "DeviceInfo{" + '\'' +
				", brand='" + brand + '\'' +
				", buildDate='" + buildDate + '\'' +
				", hardwareID='" + hardwareID + '\'' +
				", prodFullName='" + prodFullName + '\'' +
				", serialNumber='" + serialNumber + '\'' +
				", version='" + version + '\'' +
				", webURL='" + webURL + '\'' +
				'}';
	}
}
