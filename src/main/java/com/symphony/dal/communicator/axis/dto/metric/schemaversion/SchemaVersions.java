package com.symphony.dal.communicator.axis.dto.metric.schemaversion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class SchemaVersions {

    @XmlElement(name = "SchemaVersion" )
    private SchemaVersionChild schemaVersion;

    public void setSchemaVersion(SchemaVersionChild schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public SchemaVersionChild getSchemaVersion() {
        return schemaVersion;
    }
}
