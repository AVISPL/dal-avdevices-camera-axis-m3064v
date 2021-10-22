/*
 *  * Copyright (c) 2015-2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.symphony.dal.communicator.axis.dto;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

/**
 * All properties provided by the BDI (Basic device information) service.
 */
public class DeviceInfo {

	public static final String DATA = "data";
	public static final String PROPERTIES = "propertyList";

	@SerializedName("Archtecture")
	private String architecture;

	@SerializedName("Brand")
	private String brand;

	@SerializedName("BuildDate")
	private String buildDate;

	@SerializedName("HardwareID")
	private String hardwareID;

	@SerializedName("ProdFullName")
	private String prodFullName;

	@SerializedName("ProdNbr")
	private String prodNbr;

	@SerializedName("ProdShortName")
	private String prodShortName;

	@SerializedName("ProdType")
	private String prodType;

	@SerializedName("SerialNumber")
	private String serialNumber;

	@SerializedName("Soc")
	private String soc;

	@SerializedName("SocSerialNumber")
	private String socSerialNumber;

	@SerializedName("Version")
	private String version;

	@SerializedName("WebURL")
	private String webURL;

	public DeviceInfo() {
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
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

	public String getProdNbr() {
		return prodNbr;
	}

	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}

	public String getProdShortName() {
		return prodShortName;
	}

	public void setProdShortName(String prodShortName) {
		this.prodShortName = prodShortName;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSoc() {
		return soc;
	}

	public void setSoc(String soc) {
		this.soc = soc;
	}

	public String getSocSerialNumber() {
		return socSerialNumber;
	}

	public void setSocSerialNumber(String socSerialNumber) {
		this.socSerialNumber = socSerialNumber;
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
		return Objects.equals(architecture, that.architecture)
				&& Objects.equals(brand, that.brand)
				&& Objects.equals(buildDate, that.buildDate)
				&& Objects.equals(hardwareID, that.hardwareID)
				&& Objects.equals(prodFullName, that.prodFullName)
				&& Objects.equals(prodNbr, that.prodNbr)
				&& Objects.equals(prodShortName, that.prodShortName)
				&& Objects.equals(prodType, that.prodType)
				&& Objects.equals(serialNumber, that.serialNumber)
				&& Objects.equals(soc, that.soc)
				&& Objects.equals(socSerialNumber, that.socSerialNumber)
				&& Objects.equals(version, that.version)
				&& Objects.equals(webURL, that.webURL);
	}

	@Override
	public int hashCode() {
		return Objects.hash(architecture, brand, buildDate, hardwareID, prodFullName, prodNbr, prodShortName,
				prodType, serialNumber, soc, socSerialNumber, version, webURL);
	}

	@Override
	public String toString() {
		return "DeviceInfo{" +
				"architecture='" + architecture + '\'' +
				", brand='" + brand + '\'' +
				", buildDate='" + buildDate + '\'' +
				", hardwareID='" + hardwareID + '\'' +
				", prodFullName='" + prodFullName + '\'' +
				", prodNbr='" + prodNbr + '\'' +
				", prodShortName='" + prodShortName + '\'' +
				", prodType='" + prodType + '\'' +
				", serialNumber='" + serialNumber + '\'' +
				", soc='" + soc + '\'' +
				", socSerialNumber='" + socSerialNumber + '\'' +
				", version='" + version + '\'' +
				", webURL='" + webURL + '\'' +
				'}';
	}
}
