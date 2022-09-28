package org.bonitasoft.web.rest.server;

import static org.mockito.Mockito.times;

import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.api.system.I18nTranslationResource;
import org.bonitasoft.web.rest.server.utils.BonitaJacksonConverter;
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
    BonitaJacksonConverter bonitaJacksonConverter;

    @Test
    public void should_application_register_bdm_resources() throws Exception {
        //given
        final BonitaRestletApplication bonitaRestletApplication = new BonitaRestletApplication(finderFactory, bonitaJacksonConverter);

        //when
        bonitaRestletApplication.buildRouter();

        //then
        Mockito.verify(finderFactory).create(BusinessDataQueryResource.class);
        Mockito.verify(finderFactory).create(BusinessDataReferenceResource.class);
        Mockito.verify(finderFactory).create(BusinessDataReferencesResource.class);
        Mockito.verify(finderFactory, times(2)).create(BusinessDataResource.class);
    }

    @Test
    public void should_application_register_extension_resources() throws Exception {
        //given
        final BonitaRestletApplication bonitaRestletApplication = new BonitaRestletApplication(finderFactory, bonitaJacksonConverter);

        //when
        bonitaRestletApplication.buildRouter();

        //then
        Mockito.verify(finderFactory).createExtensionResource();
    }


    @Test
    public void application_should_register_i18n_resources() throws Exception {
        //given
        final BonitaRestletApplication bonitaRestletApplication = new BonitaRestletApplication(finderFactory, bonitaJacksonConverter);

        //when
        bonitaRestletApplication.buildRouter();

        //then
        Mockito.verify(finderFactory).create(I18nTranslationResource.class);
    }

}
