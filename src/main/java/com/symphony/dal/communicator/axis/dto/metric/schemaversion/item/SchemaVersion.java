package com.symphony.dal.communicator.axis.dto.metric.schemaversion.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersion {

    @XmlElement(name = "VersionNumber")
    private String versionNumber;
    @XmlElement(name = "Deprecated")
    private String deprecated;

    public void setDeprecated(String deprecated) {
        this.deprecated = deprecated;
    }

    public String getDeprecated() {
        return deprecated;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionNumber() {
        return versionNumber;
    }
}
