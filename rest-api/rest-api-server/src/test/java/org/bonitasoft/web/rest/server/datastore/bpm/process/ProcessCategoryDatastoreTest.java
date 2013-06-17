/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.bpm.process;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.rest.server.model.builder.bpm.process.ProcessCategoryItemBuilder.aProcessCategory;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessCategoryItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessCategoryDatastore;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Colin PUY
 * 
 */
public class ProcessCategoryDatastoreTest extends APITestWithMock {

    @Mock
    private ProcessAPI processAPI;

    private ProcessCategoryDatastore processCategoryDatastore;

    @Before
    public void initializeMocks() {
        initMocks(this);

        processCategoryDatastore = spy(new ProcessCategoryDatastore(null));

        doReturn(this.processAPI).when(processCategoryDatastore).getProcessAPI();
    }

    @Test(expected = APIForbiddenException.class)
    public void addingTwiceSameCategoryOnProcessIsForbidden() throws Exception {
        ProcessCategoryItem processCategory = aProcessCategory().build();
        doThrow(AlreadyExistsException.class).when(processAPI)
                .addCategoriesToProcess(processCategory.getProcessId().toLong(), asList(processCategory.getCategoryId().toLong()));

        processCategoryDatastore.add(processCategory);
    }

}
