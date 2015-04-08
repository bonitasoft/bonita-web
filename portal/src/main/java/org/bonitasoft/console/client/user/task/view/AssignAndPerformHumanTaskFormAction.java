package org.bonitasoft.console.client.user.task.view;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.client.common.view.PerformTaskPage;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;

public class AssignAndPerformHumanTaskFormAction extends FormAction {

    private final HumanTaskItem humanTaskItem;

    public AssignAndPerformHumanTaskFormAction(final HumanTaskItem humanTaskItem) {
        super();
        this.humanTaskItem = humanTaskItem;
    }

    @Override
    public void execute() {
        // assign the task to the logged user
        final HashMap<String, String> attributesToUpdate = new HashMap<String, String>();
        attributesToUpdate.put(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, Session.getUserId().toString());
        new APICaller<HumanTaskItem>(HumanTaskDefinition.get()).update(humanTaskItem.getId(), attributesToUpdate, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.showView(new PerformTaskPage(humanTaskItem.getId()));
            }
        });

    }
}