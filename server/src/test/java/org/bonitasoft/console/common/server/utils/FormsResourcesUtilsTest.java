package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.bonitasoft.engine.session.APISession;
import org.junit.Test;
import org.mockito.Mock;

public class FormsResourcesUtilsTest {

    @Mock
    APISession apiSession;

    @Mock
    Long processDefinitionID;

    @Mock
    Map<Long, ClassLoader> PROCESS_CLASSLOADERS;

    @Test
    public void setCorrectHierarchicalClassLoaderShouldsetParentClassloaderIfCLIsNull() throws Exception {
        final ClassLoader processClassLoader = null;
        final ClassLoader parentClassLoader = mock(ClassLoader.class);
        final ClassLoader realCL = FormsResourcesUtils.setCorrectHierarchicalClassLoader(processClassLoader, parentClassLoader);
        assertThat(realCL).isEqualTo(parentClassLoader);
    }
}
