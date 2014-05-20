package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Vincent Elcrin
 */
public class TaskFinder {

    private DatastoreHasGet<TaskItem> journal;

    private DatastoreHasGet<ArchivedTaskItem> archives;

    public TaskFinder(DatastoreHasGet<TaskItem> journal, DatastoreHasGet<ArchivedTaskItem> archives) {
        this.journal = journal;
        this.archives = archives;
    }

    public IItem find(APIID taskId) {
        try {
            return journal.get(taskId);
        } catch (APIItemNotFoundException e) {
            return archives.get(taskId);
        }
    }
}
