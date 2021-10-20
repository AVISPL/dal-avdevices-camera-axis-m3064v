package com.symphony.dal.communicator.axis.dto;

/**
 * All properties provided by the BDI (Basic device information) service.
 * */
public class DeviceInfo {

    public static final String DATA = "data";
    public static final String PROPERTIES = "properties";
    private String apiVersion;
    private String context;
    private String data;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
