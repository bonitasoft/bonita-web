package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.io.File;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BonitaHomeFolderAccessorTest {

    @Mock
    private WebBonitaConstantsUtils webBonitaConstantsUtils;

    @Spy
    private final BonitaHomeFolderAccessor tenantFolder = new BonitaHomeFolderAccessor();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void should_authorized_a_file_in_temp_folder() throws Exception {
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("." + File.separator + "tempFolder"));

        final File file = new File(webBonitaConstantsUtils.getTempFolder().getAbsolutePath(), "" + File.separator + ".." + File.separator + "tempFolder"
                + File.separator + "fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInTempFolder(file, webBonitaConstantsUtils);

        assertTrue(isInTempFolder);
    }

    @Test
    public void should_unauthorized_a_file_not_in_temp_folder() throws Exception {
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("." + File.separator + "tempFolder"));

        final File file = new File(webBonitaConstantsUtils.getTempFolder().getAbsolutePath(), "" + File.separator + ".." + File.separator + ".."
                + File.separator + ".." + File.separator + "fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInTempFolder(file, webBonitaConstantsUtils);

        assertFalse(isInTempFolder);
    }

    @Test
    public void should_authorized_a_file_in_a_specific_folder() throws Exception {

        final File folder = new File("." + File.separator + "anyFolder");

        final File file = new File("." + File.separator + "anyFolder" + File.separator + ".." + File.separator + "anyFolder" + File.separator + "fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInFolder(file, folder);

        assertTrue(isInTempFolder);
    }

    @Test
    public void should_unauthorized_a_file_not_in_a_specific_folder() throws Exception {

        final File folder = new File("." + File.separator + "anyFolder");

        final File file = new File("." + File.separator + "anyFolder" + File.separator + ".." + File.separator + ".." + File.separator + "fileName.txt");

        final boolean isInTempFolder = tenantFolder.isInFolder(file, folder);

        assertFalse(isInTempFolder);
    }

    @Test
    public void should_complete_file_path() throws Exception {
        final String fileName = "fileName.txt";

        given(tenantFolder.getBonitaConstantUtil(1L)).willReturn(webBonitaConstantsUtils);
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("." + File.separator + "tempFolder"));

        final String completedPath = tenantFolder.getCompleteTempFilePath(fileName, 1L);

        assertThat(new File(completedPath).getCanonicalPath()).isEqualTo(
                new File("." + File.separator + "tempFolder" + File.separator + "fileName.txt").getCanonicalPath());
    }

    @Test
    public void should_verifyAuthorization_file_path() throws Exception {
        final String fileName = "c:" + File.separator + "tempFolder" + File.separator + "fileName.txt";

        given(tenantFolder.getBonitaConstantUtil(1L)).willReturn(webBonitaConstantsUtils);
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("c:" + File.separator + "tempFolder"));

        final String completedPath = tenantFolder.getCompleteTempFilePath(fileName, 1L);

        assertThat(completedPath).isEqualTo("c:" + File.separator + "tempFolder" + File.separator + "fileName.txt");
    }

    @Test(expected = UnauthorizedFolderException.class)
    public void should_UnauthorizedFolder() throws Exception {
        final String fileName = "c:" + File.separator + "UnauthorizedFolder" + File.separator + "tempFolder" + File.separator + "fileName.txt";

        given(tenantFolder.getBonitaConstantUtil(1L)).willReturn(webBonitaConstantsUtils);
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("c:" + File.separator + "tempFolder"));

        tenantFolder.getCompleteTempFilePath(fileName, 1L);
    }

    @Test
    public void should_return_completed_temp_file() throws Exception {
        final String fileName = "fileName.txt";

        given(tenantFolder.getBonitaConstantUtil(1L)).willReturn(webBonitaConstantsUtils);
        given(webBonitaConstantsUtils.getTempFolder()).willReturn(new File("." + File.separator + "tempFolder"));

        final File completedFile = tenantFolder.getTempFile(fileName, 1L);

        assertThat(completedFile.getCanonicalPath()).isEqualTo(
                new File("." + File.separator + "tempFolder" + File.separator + "fileName.txt").getCanonicalPath());
    }
}
