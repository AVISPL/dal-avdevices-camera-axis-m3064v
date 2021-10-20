package com.symphony.dal.communicator.axis.dto.metric.schemaversion.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Success {
    @XmlElement(name = "GetSchemaVersionsSuccess")
  private Schema schema;

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}