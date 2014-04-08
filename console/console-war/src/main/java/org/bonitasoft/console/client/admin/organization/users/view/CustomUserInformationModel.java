package org.bonitasoft.console.client.admin.organization.users.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationModel extends ObservableModel<CustomUserInformationModel> {

    private List<CustomUserInfoItem> items = new ArrayList<CustomUserInfoItem>();

    private String userId;

    private int page;

    private int size;

    public CustomUserInformationModel(String userId, int page, int size) {
        this.userId = userId;
        this.page = page;
        this.size = size;
        load(page, size, userId);
    }

    public List<CustomUserInfoItem> getInformation() {
        return items;
    }

    public void next() {
        load(++page, size, userId);
    }

    public void previous() {
        if(page > 0) {
            load(--page, size, userId);
        }
    }

    private void load(int page, int size, String userId) {
        APISearchRequest request = new APISearchRequest(CustomUserInfoDefinition.get());
        request.setPage(page);
        request.setResultsPerPage(size);
        request.addFilter(CustomUserInfoItem.ATTRIBUTE_USER_ID, userId);
        request.setCallback(new APICallback() {

            @Override
            public void onSuccess(int httpStatusCode, String response, Map<String, String> headers) {
                items.clear();
                items.addAll(JSonItemReader.parseItems(response, CustomUserInfoDefinition.get()));
                notifyChange(CustomUserInformationModel.this);
            }
        });
        request.run();
    }

    public void update(int index, String value) {
        items.get(index).setValue(value);
        notifyChange(this);
    }
}
