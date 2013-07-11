package org.bonitasoft.test.toolkit.api.json;

import org.bonitasoft.test.toolkit.api.APIHelper.ProcessActivationState;

/**
 * JSON builder for setProcessState request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class SetProcessState extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "setProcessState.json";

    private ProcessActivationState activationState;

    public SetProcessState(final String pId, final ProcessActivationState pState) {
        super(JSON_RESOURCE);
        setState(pState);
    }

    public ProcessActivationState getState() {
        return this.activationState;
    }

    public void setState(final ProcessActivationState pState) {
        this.activationState = pState;
        this.jsonObject.put("activationState", pState.toString());
    }

    // CHECKSTYLE:ON

}
