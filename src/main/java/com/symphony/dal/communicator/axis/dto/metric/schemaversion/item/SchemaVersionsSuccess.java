package com.symphony.dal.communicator.axis.dto.metric.schemaversion.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Success")
@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersionsSuccess {

    @XmlElement(name = "GetSchemaVersionsSuccess")
    private SchemaVersion schemaVersion;

    public SchemaVersion getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(SchemaVersion schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}
