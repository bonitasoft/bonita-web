package org.bonitasoft.console.common.server.form;

import java.io.Serializable;


public class FormReference implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -1692145871057019847L;

    private String reference;

    private boolean external;

    protected FormReference(final String reference, final boolean external) {
        this.reference = reference;
        this.external = external;
    }


    public String getReference() {
        return reference;
    }


    public void setReference(final String reference) {
        this.reference = reference;
    }


    public boolean isExternal() {
        return external;
    }


    public void setExternal(final boolean external) {
        this.external = external;
    }



}
