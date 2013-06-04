package org.bonitasoft.forms.client.model;

import java.io.Serializable;

public class ReducedRedirectionURL implements Serializable {


	/**
     * UID
     */
    private static final long serialVersionUID = 4758223515278873484L;

    private String url;
	
	private boolean transmitSubmitURL;
	
	private boolean isEditMode;
	
    /**
     * Constructor
     * 
     * @param transmitSubmissionURL
     */
    public ReducedRedirectionURL(final boolean transmitSubmitURL) {
		super();
		this.transmitSubmitURL = transmitSubmitURL;
	}

	/**
     * Default Constructor
     */
    public ReducedRedirectionURL(){
        super();
        // Mandatory for serialization
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isTransmitSubmitURL() {
		return transmitSubmitURL;
	}

	public void setTransmitSubmitURL(final boolean transmitSubmitURL) {
		this.transmitSubmitURL = transmitSubmitURL;
	}

	public boolean isEditMode() {
		return isEditMode;
	}

	public void setEditMode(final boolean isEditMode) {
		this.isEditMode = isEditMode;
	}
}
