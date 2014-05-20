package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.ArchivedTaskDatastore;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskFinderTest {

    @Mock
    private TaskDatastore journal;

    @Mock
    private ArchivedTaskDatastore archives;

    private TaskFinder taskFinder;

    private APIID id = APIID.makeAPIID(658L);

    @Before
    public void setUp() throws Exception {
        I18n.getInstance();
        ItemDefinitionFactory.setDefaultFactory(new ItemDefinitionFactory() {
            @Override
            public ItemDefinition<?> defineItemDefinitions(String token) {
                return null;
            }
        });
        taskFinder = new TaskFinder(journal, archives);
    }

    @Test
    public void should_return_task_from_the_journal_when_it_belong_to_the_journal() throws Exception {
        TaskItem task = new TaskItem();
        task.setId(id);
        when(journal.get(id)).thenReturn(task);

        IItem item = taskFinder.find(id);

        assertThat(item.getId()).isEqualTo(task.getId());
    }

    @Test
    public void should_return_task_from_the_archives_when_not_found_in_the_journal() throws Exception {
        ArchivedTaskItem task = new ArchivedTaskItem();
        task.setId(id);
        when(journal.get(id)).thenThrow(new APIItemNotFoundException("type", id));
        when(archives.get(id)).thenReturn(task);

        IItem item = taskFinder.find(id);

        assertThat(item.getId()).isEqualTo(task.getId());
    }

    @Test(expected = APIItemNotFoundException.class)
    public void should_throw_an_exception_when_the_task_does_not_exist() throws Exception {
        when(journal.get(id)).thenThrow(new APIItemNotFoundException("type", id));
        when(archives.get(id)).thenThrow(new APIItemNotFoundException("type", id));

        taskFinder.find(id);
    }
}


