package org.bonitasoft.console.client.user.task.view;

import org.bonitasoft.console.client.user.task.action.CheckFormMappingAndDisplayPerformTaskPageAction;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;

import java.util.HashMap;
import java.util.Map;

public class AssignAndPerformHumanTaskFormAction extends FormAction {

    private final HumanTaskItem humanTaskItem;

    public AssignAndPerformHumanTaskFormAction(final HumanTaskItem humanTaskItem) {
        super();
        this.humanTaskItem = humanTaskItem;
    }

    @Override
    public void execute() {
        new APICaller<HumanTaskItem>(HumanTaskDefinition.get()).get(humanTaskItem.getId(), new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {

                final HumanTaskItem updatedHumanTaskItem = (HumanTaskItem) JSonItemReader.parseItem(response, humanTaskItem.getItemDefinition());
                final PerformTaskPage performTaskPage = new PerformTaskPage(humanTaskItem.getId());
                final Action displayPerformTaskPageAction = new CheckFormMappingAndDisplayPerformTaskPageAction(humanTaskItem.getProcessId().toString(),
                        humanTaskItem.getRootCaseId().toString(), humanTaskItem.getName(), humanTaskItem.getDisplayName(), performTaskPage);
                if (updatedHumanTaskItem.getAssignedId() == null) {
                    // assign the task to the logged user
                    final HashMap<String, String> attributesToUpdate = new HashMap<String, String>();
                    attributesToUpdate.put(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, Session.getUserId().toString());
                    new APICaller<HumanTaskItem>(HumanTaskDefinition.get()).update(humanTaskItem.getId(), attributesToUpdate, new APICallback() {

                        @Override
                        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                            displayPerformTaskPageAction.execute();
                        }
                    });
                } else {
                    displayPerformTaskPageAction.execute();

                }
            }
        });
    }
}