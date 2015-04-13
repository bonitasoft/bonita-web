package org.bonitasoft.web.rest.server;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

import java.util.Arrays;

import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.api.extension.ResourceExtensionDescriptor;
import org.bonitasoft.web.rest.server.api.extension.TenantSpringBeanAccessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BonitaRestletApplicationTest {

    @Mock
    FinderFactory finderFactory;

    @Mock
    private ResourceExtensionDescriptor resourceExtensionDescriptor;

    @Mock
    TenantSpringBeanAccessor springPlatformFileSystemBeanAccessor;

    @Test
    public void should_application_register_bdm_resources() throws Exception {
        //given
        doReturn(Arrays.asList(resourceExtensionDescriptor)).when(springPlatformFileSystemBeanAccessor).getResourceExtensionConfiguration();

        final BonitaRestletApplication bonitaSPRestletApplication = new BonitaRestletApplication(finderFactory,springPlatformFileSystemBeanAccessor );

        //when
        bonitaSPRestletApplication.buildRouter();

        //then
        Mockito.verify(finderFactory).create(BusinessDataQueryResource.class);
        Mockito.verify(finderFactory).create(BusinessDataReferenceResource.class);
        Mockito.verify(finderFactory).create(BusinessDataReferencesResource.class);
        Mockito.verify(finderFactory, times(2)).create(BusinessDataResource.class);

        Mockito.verify(finderFactory ).createExtensionResource(resourceExtensionDescriptor);


    }
}
