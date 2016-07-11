package org.bonitasoft.forms.client.model;

import java.io.Serializable;

public class RedirectionURL implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -3412377355845715867L;

    private Expression urlExpression;

    private ReducedRedirectionURL reducedRedirectionURL;

    /**
     * Constructor
     * 
     * @param transmitSubmissionURL
     */
    public RedirectionURL(final boolean transmitSubmitURL) {
        super();
        this.reducedRedirectionURL = new ReducedRedirectionURL(transmitSubmitURL);
    }

    /**
     * Default Constructor
     * Mandatory for serialization
     */
    public RedirectionURL() {
        super();
        reducedRedirectionURL = new ReducedRedirectionURL();
    }

    public String getUrl() {
        return this.reducedRedirectionURL.getUrl();
    }

    public void setUrl(final String url) {
        this.reducedRedirectionURL.setUrl(url);
    }

    public boolean isTransmitSubmitURL() {
        return this.reducedRedirectionURL.isTransmitSubmitURL();
    }

    public void setTransmitSubmitURL(final boolean transmitSubmitURL) {
        this.reducedRedirectionURL.setTransmitSubmitURL(transmitSubmitURL);
    }

    public boolean isEditMode() {
        return this.reducedRedirectionURL.isEditMode();
    }

    public void setEditMode(final boolean isEditMode) {
        this.reducedRedirectionURL.setEditMode(isEditMode);
    }

    public ReducedRedirectionURL getReducedRedirectionURL() {
        return reducedRedirectionURL;
    }

    public void setReducedRedirectionURL(ReducedRedirectionURL reducedRedirectionURL) {
        this.reducedRedirectionURL = reducedRedirectionURL;
    }

    public Expression getUrlExpression() {
        return urlExpression;
    }

    public void setUrlExpression(Expression urlExpression) {
        this.urlExpression = urlExpression;
    }
    
    

}
