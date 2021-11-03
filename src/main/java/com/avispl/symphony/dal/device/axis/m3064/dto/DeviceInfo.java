/*
 *  * Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.device.axis.m3064.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * All properties provided by the BDI (Basic device information) service.
 *
 * @author Ivan
 * @version 1.0
 * @since 1.0
 */
@JsonIgnoreProperties( { "Architecture", "ProdNbr", "ProdShortName", "ProdType", "ProdVariant", "Soc", "SocSerialNumber" })
public class DeviceInfo {

	public static final String DATA = "data";
	public static final String PROPERTIES = "propertyList";

	@JsonAlias("Brand")
	private String brand;

	@JsonAlias("BuildDate")
	private String buildDate;

	@JsonAlias("HardwareID")
	private String hardwareID;

	@JsonAlias("ProdFullName")
	private String prodFullName;

	@JsonAlias("SerialNumber")
	private String serialNumber;

	@JsonAlias("Version")
	private String version;

	@JsonAlias("WebURL")
	private String webURL;

	/**
	 * DeviceInfo instantiation
	 */
	public DeviceInfo() {
	}

	/**
	 * Retrieves {@code {@link #brand}}
	 *
	 * @return value of {@link #brand}
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * Sets {@code brand}
	 *
	 * @param brand the {@code java.lang.String} field
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * Retrieves {@code {@link #buildDate}}
	 *
	 * @return value of {@link #buildDate}
	 */
	public String getBuildDate() {
		return buildDate;
	}

	/**
	 * Sets {@code buildDate}
	 *
	 * @param buildDate the {@code java.lang.String} field
	 */
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	/**
	 * Retrieves {@code {@link #hardwareID}}
	 *
	 * @return value of {@link #hardwareID}
	 */
	public String getHardwareID() {
		return hardwareID;
	}

	/**
	 * Sets {@code hardwareID}
	 *
	 * @param hardwareID the {@code java.lang.String} field
	 */
	public void setHardwareID(String hardwareID) {
		this.hardwareID = hardwareID;
	}

	/**
	 * Retrieves {@code {@link #prodFullName}}
	 *
	 * @return value of {@link #prodFullName}
	 */
	public String getProdFullName() {
		return prodFullName;
	}

	/**
	 * Sets {@code prodFullName}
	 *
	 * @param prodFullName the {@code java.lang.String} field
	 */
	public void setProdFullName(String prodFullName) {
		this.prodFullName = prodFullName;
	}

	/**
	 * Retrieves {@code {@link #serialNumber}}
	 *
	 * @return value of {@link #serialNumber}
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets {@code serialNumber}
	 *
	 * @param serialNumber the {@code java.lang.String} field
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * Retrieves {@code {@link #version}}
	 *
	 * @return value of {@link #version}
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets {@code version}
	 *
	 * @param version the {@code java.lang.String} field
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Retrieves {@code {@link #webURL}}
	 *
	 * @return value of {@link #webURL}
	 */
	public String getWebURL() {
		return webURL;
	}

	/**
	 * Sets {@code webURL}
	 *
	 * @param webURL the {@code java.lang.String} field
	 */
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
