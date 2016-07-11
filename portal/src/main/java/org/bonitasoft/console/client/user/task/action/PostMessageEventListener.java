package org.bonitasoft.console.client.user.task.action;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


public abstract class PostMessageEventListener {

    public void onMessageEvent(final String eventData) {
        try {
            final JSONValue root = JSONParser.parseStrict(eventData);
            final JSONObject fullJSONMessage = root.isObject();
            if (isActionWatched(fullJSONMessage)) {
                handleJSONMessage(fullJSONMessage);
            }
        } catch (final Exception e) {
            GWT.log("Error while parsing content of message", e);
        }
    }

    protected boolean isActionWatched(final JSONObject fullJSONMessage) {
        final JSONValue actionValue = fullJSONMessage.get("action");
        if (actionValue != null) {
            final String action = actionValue.isString().stringValue();
            if (getActionToWatch().equals(action)) {
                return true;
            }
        } else {
            GWT.log("action attribute is missing from the message");
        }
        return false;
    }

    protected void handleJSONMessage(final JSONObject fullJSONMessage) {
        final JSONValue messageValue = fullJSONMessage.get("message");
        if (messageValue != null) {
            final String message = messageValue.isString().stringValue();
            if ("success".equals(message)) {
                onSuccess(fullJSONMessage);
            } else {
                onError(fullJSONMessage);
            }
        } else {
            GWT.log("message attribute is missing from the message");
        }
    }

    public abstract String getActionToWatch();

    protected void onSuccess(final JSONObject fullJSONMessage) {
        final JSONValue dataFromSuccessValue = fullJSONMessage.get("dataFromSuccess");
        String dataFromSuccess = null;
        if (dataFromSuccessValue != null) {
            final JSONObject dataFromSuccesObject = dataFromSuccessValue.isObject();
            if (dataFromSuccesObject != null) {
                dataFromSuccess = dataFromSuccesObject.toString();
            } else {
                dataFromSuccess = dataFromSuccessValue.isString().stringValue();
            }
        }
        onSuccess(dataFromSuccess);
    };

    protected abstract void onSuccess(String dataFromSuccess);

    protected void onError(final JSONObject fullJSONMessage) {
        final JSONValue dataFromErrorValue = fullJSONMessage.get("dataFromError");
        String dataFromError = null;
        if (dataFromErrorValue != null) {
            dataFromError = dataFromErrorValue.isString().stringValue();
        }
        final JSONValue errorCodeValue = fullJSONMessage.get("status");
        double errorCode;
        if (errorCodeValue != null) {
            errorCode = errorCodeValue.isNumber().doubleValue();
        } else {
            GWT.log("status attribute is not set in the message. Setting it to 500 by default");
            errorCode = 500;
        }
        onError(dataFromError, (int) errorCode);
    }

    protected abstract void onError(String dataFromError, int errorCode);

}
