package org.bonitasoft.console.client.admin.organization.users.view;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.identity.CustomUserInfoValue;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoValueDefinition;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APIUpdateRequest;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationModel {

    private String userId;

    public CustomUserInformationModel(String userId) {
        this.userId = userId;
    }

    public void update(CustomUserInfoItem item, String value) {

        CustomUserInfoItem update = new CustomUserInfoItem();
        update.setValue(value);

        APIUpdateRequest request = new APIUpdateRequest(CustomUserInfoValueDefinition.get());
        request.setId(item.getId());
        request.setItem(update);
        request.run();
    }

    public static abstract class Callback extends APICallback {

        @Override
        public void onSuccess(int httpStatusCode, String response, Map<String, String> headers) {
            onSuccess(JSonItemReader.parseItems(response, CustomUserInfoDefinition.get()));
        }

        abstract void onSuccess(List<CustomUserInfoItem> information);
    }

    public void search(final int page, final int size, Callback callback) {
        APISearchRequest request = new APISearchRequest(CustomUserInfoDefinition.get());
        request.setPage(page);
        request.setResultsPerPage(size);
        request.addFilter(CustomUserInfoItem.ATTRIBUTE_USER_ID, userId);
        request.setCallback(callback);
        request.run();
    }
}
