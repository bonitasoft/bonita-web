package org.bonitasoft.web.rest.server.api.tenant;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.web.rest.model.tenant.BusinessDataModelItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.engineclient.TenantManagementEngineClient;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIBusinessDataModelTest extends APITestWithMock {

    @Spy
    private final APIBusinessDataModel apiBusinessDataModel = new APIBusinessDataModel();

    @Mock
    private TenantManagementEngineClient tenantManagementEngineClient;

    @Before
    public void setUp() {
        doReturn(tenantManagementEngineClient).when(apiBusinessDataModel).getTenantManagementEngineClient();
    }

    @Test
    public void should_uninstall_bdm_before_installing_it() throws Exception {
        final File bdmFile = testBDMFile();
        final BusinessDataModelItem item = new BusinessDataModelItem();
        item.setAttribute(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH, bdmFile.getAbsolutePath());
        doReturn(bdmFile.getAbsolutePath()).when(apiBusinessDataModel).getCompleteTempFilePath(bdmFile.getAbsolutePath());


        apiBusinessDataModel.add(item);

        final InOrder inOrder = inOrder(tenantManagementEngineClient);
        inOrder.verify(tenantManagementEngineClient).uninstallBusinessDataModel();
        inOrder.verify(tenantManagementEngineClient).installBusinessDataModel(getContent(bdmFile));
    }

    private File testBDMFile() throws URISyntaxException {
        return new File(APIBusinessDataModelTest.class.getResource("bizdatamodel.zip").toURI());
    }

    private byte[] getContent(final File businessDataModelFile) throws IOException, FileNotFoundException {
        final FileReader input = new FileReader(businessDataModelFile);
        try {
            return IOUtils.toByteArray(input);
        } finally {
            input.close();
        }
    }

    @Test(expected = APIForbiddenException.class)
    public void should_throws_an_exception_adding_a_document_with_unauthorized_path() throws Exception {
        final BusinessDataModelItem item = new BusinessDataModelItem();
        item.setAttribute(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH, "../../../bdmFile.zip");
        doThrow(new APIForbiddenException("")).when(apiBusinessDataModel).getCompleteTempFilePath("../../../bdmFile.zip");

        apiBusinessDataModel.add(item);
    }

    @Test(expected = APIException.class)
    public void should_throws_an_exception_when_cannot_write_file_on_add() throws Exception {
        final BusinessDataModelItem item = new BusinessDataModelItem();
        item.setAttribute(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH, "../../../bdmFile.zip");
        doThrow(new APIException("")).when(apiBusinessDataModel).getCompleteTempFilePath("../../../bdmFile.zip");

        try {
            apiBusinessDataModel.add(item);
        } catch (final APIForbiddenException e) {
            fail("Not suppose to throw APIForbiddenException on IOException");
        }
    }


}
