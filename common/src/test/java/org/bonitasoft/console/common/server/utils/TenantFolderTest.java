package org.bonitasoft.console.common.server.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.io.File;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TenantFolderTest {

    @Mock
    private WebBonitaConstantsUtils webBonitaConstantsUtils;

    private final TenantFolder tenantFolder = new TenantFolder();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void should_authorized_a_file_in_temp_folder() throws Exception {
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("./tempFolder"));

        final File file = new File(webBonitaConstantsUtils.getTempFolder().getAbsolutePath(), "/../tempFolder/fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInTempFolder(file, webBonitaConstantsUtils);

        assertTrue(isInTempFolder);
    }

    @Test
    public void should_unauthorized_a_file_not_in_temp_folder() throws Exception {
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("./tempFolder"));

        final File file = new File(webBonitaConstantsUtils.getTempFolder().getAbsolutePath(), "/../../../fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInTempFolder(file, webBonitaConstantsUtils);

        assertFalse(isInTempFolder);
    }

    @Test
    public void should_authorized_a_file_in_a_specific_folder() throws Exception {

        final File folder = new File("./anyFolder");

        final File file = new File("./anyFolder/../anyFolder/fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInFolder(file, folder);

        assertTrue(isInTempFolder);
    }

    @Test
    public void should_unauthorized_a_file_not_in_a_specific_folder() throws Exception {

        final File folder = new File("./anyFolder");

        final File file = new File("./anyFolder/../../fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInFolder(file, folder);

        assertFalse(isInTempFolder);
    }


}
