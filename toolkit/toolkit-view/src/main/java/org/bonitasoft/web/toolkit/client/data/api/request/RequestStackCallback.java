package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.LinkedHashMap;

public abstract class RequestStackCallback {

    public abstract void onSuccess(LinkedHashMap<String, String> responses);

    public abstract void onError(LinkedHashMap<String, String> messages);
}
