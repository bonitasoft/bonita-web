package org.bonitasoft.web.rest.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BonitaRestletApplicationTest {

    @Mock
    FinderFactory finderFactory;

    @Test
    public void should_application_register_bdm_resources() throws Exception {
        //given
        final BonitaRestletApplication bonitaSPRestletApplication = new BonitaRestletApplication(finderFactory);

        //when
        bonitaSPRestletApplication.buildRouter();

        //then
        //        Mockito.verify(finderFactory).create(BusinessDataQueryResource.class);
        //        Mockito.verify(finderFactory).create(BusinessDataReferenceResource.class);
        //        Mockito.verify(finderFactory).create(BusinessDataReferencesResource.class);
        //        Mockito.verify(finderFactory, times(2)).create(BusinessDataResource.class);
    }
}
