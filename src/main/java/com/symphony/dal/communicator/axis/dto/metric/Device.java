package com.symphony.dal.communicator.axis.dto.metric;


import java.util.Objects;

/**
 * All properties provided by the BDI (Basic device information) service.
 */
public class Device {

    private String Architecture;

    private String Brand;

    private String BuildDate;

    private String HardwareID;

    private String ProdFullName;

    private String ProdNbr;

    private String ProdShortName;

    private String ProdType;

    private String ProdVariant;

    private String SerialNumber;

    private String Soc;

    private String SocSerialNumber;

    private String Version;

    private String WebURL;

    public Device() {
    }

    public String getArchitecture() {
        return Architecture;
    }

    public void setArchitecture(String architecture) {
        this.Architecture = architecture;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        this.Brand = brand;
    }

    public String getBuildDate() {
        return BuildDate;
    }

    public void setBuildDate(String buildDate) {
        this.BuildDate = buildDate;
    }

    public String getHardwareID() {
        return HardwareID;
    }

    public void setHardwareID(String hardwareID) {
        this.HardwareID = hardwareID;
    }

    public String getProdFullName() {
        return ProdFullName;
    }

    public void setProdFullName(String prodFullName) {
        this.ProdFullName = prodFullName;
    }

    public String getProdNbr() {
        return ProdNbr;
    }

    public void setProdNbr(String prodNbr) {
        this.ProdNbr = prodNbr;
    }

    public String getProdShortName() {
        return ProdShortName;
    }

    public void setProdShortName(String prodShortName) {
        this.ProdShortName = prodShortName;
    }

    public String getProdType() {
        return ProdType;
    }

    public void setProdType(String prodType) {
        this.ProdType = prodType;
    }

    public String getProdVariant() {
        return ProdVariant;
    }

    public void setProdVariant(String prodVariant) {
        this.ProdVariant = prodVariant;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.SerialNumber = serialNumber;
    }

    public String getSoc() {
        return Soc;
    }

    public void setSoc(String soc) {
        this.Soc = soc;
    }

    public String getSocSerialNumber() {
        return SocSerialNumber;
    }

    public void setSocSerialNumber(String socSerialNumber) {
        this.SocSerialNumber = socSerialNumber;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getWebURL() {
        return WebURL;
    }

    public void setWebURL(String webURL) {
        this.WebURL = webURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device that = (Device) o;
        return Objects.equals(Architecture, that.Architecture)
                && Objects.equals(Brand, that.Brand)
                && Objects.equals(BuildDate, that.BuildDate)
                && Objects.equals(HardwareID, that.HardwareID)
                && Objects.equals(ProdFullName, that.ProdFullName)
                && Objects.equals(ProdNbr, that.ProdNbr)
                && Objects.equals(ProdShortName, that.ProdShortName)
                && Objects.equals(ProdType, that.ProdType)
                && Objects.equals(ProdVariant, that.ProdVariant)
                && Objects.equals(SerialNumber, that.SerialNumber)
                && Objects.equals(Soc, that.Soc)
                && Objects.equals(SocSerialNumber, that.SocSerialNumber)
                && Objects.equals(Version, that.Version)
                && Objects.equals(WebURL, that.WebURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Architecture, Brand, BuildDate, HardwareID, ProdFullName, ProdNbr, ProdShortName,
                ProdType, ProdVariant, SerialNumber, Soc, SocSerialNumber, Version, WebURL);
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "architecture='" + Architecture + '\'' +
                ", brand='" + Brand + '\'' +
                ", buildDate='" + BuildDate + '\'' +
                ", hardwareID='" + HardwareID + '\'' +
                ", prodFullName='" + ProdFullName + '\'' +
                ", prodNbr='" + ProdNbr + '\'' +
                ", prodShortName='" + ProdShortName + '\'' +
                ", prodType='" + ProdType + '\'' +
                ", prodVariant='" + ProdVariant + '\'' +
                ", serialNumber='" + SerialNumber + '\'' +
                ", soc='" + Soc + '\'' +
                ", socSerialNumber='" + SocSerialNumber + '\'' +
                ", version='" + Version + '\'' +
                ", webURL='" + WebURL + '\'' +
                '}';
    }
}
