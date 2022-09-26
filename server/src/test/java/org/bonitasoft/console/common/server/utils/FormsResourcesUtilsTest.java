package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class FormsResourcesUtilsTest {

    @Test
    public void setCorrectHierarchicalClassLoaderShouldSetParentClassloaderIfCLIsNull() {
        final ClassLoader parentClassLoader = mock(ClassLoader.class);
        final ClassLoader realCL = FormsResourcesUtils.setCorrectHierarchicalClassLoader(null, parentClassLoader);
        assertThat(realCL).isEqualTo(parentClassLoader);
    }
}
