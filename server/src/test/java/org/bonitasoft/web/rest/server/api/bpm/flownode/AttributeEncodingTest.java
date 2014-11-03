package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.routing.Router;


public class AttributeEncodingTest {

    public static class GetTestResource extends CommonResource {

        @Get
        public void paramEncoded() {
            // given
            final String paramName = "procVarJapanカキクケコ";
            // when
            final String encodedAttribute = getAttribute("param");

            //then
            assertThat(encodedAttribute).isEqualTo(paramName);
        }
    }

    public static class EncodedJsonTestResource extends CommonResource {

        @Get("json;charset=utf-8")
        public Dummy paramEncoded() {
            return new Dummy();
        }
    }

    private Component component;
    private Server server;

    @Before
    public void start() throws Exception {
        component = new Component();
        server = component.getServers().add(Protocol.HTTP, 0);
        // server.getContext().getParameters().add("tracing", "true");
        final Application application = createApplication();
        component.getDefaultHost().attach(application);
        component.start();
    }

    @After
    public void stop() throws Exception {
        if (component != null && component.isStarted()) {
            component.stop();
        }
        component = null;
    }
    @Test
    public void callResourceWithEncoding() throws Exception {
        final Request request = new Request(Method.GET, "http://localhost:" + server.getEphemeralPort() + "/test/procVarJapanカキクケコ");
        final Client c = new Client(Protocol.HTTP);
        final Response r = c.handle(request);
        assertThat(r.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
        c.stop();
    }

    @Test
    public void callResourceReturnsAnEncodedJson() throws Exception {
        final Request request = new Request(Method.GET, "http://localhost:" + server.getEphemeralPort() + "/test/");
        final Client c = new Client(Protocol.HTTP);
        final Response r = c.handle(request);
        assertThat(r.getStatus()).isEqualTo(Status.SUCCESS_OK);
        final String entityAsText = r.getEntityAsText();
        System.out.println(entityAsText);
        assertThat(entityAsText).isEqualTo("{\"field\":\"procVarJapanカキクケコ\"}");
        c.stop();
    }

    protected Application createApplication() {
        final Application application = new Application() {

            @Override
            public Restlet createInboundRoot() {
                final Router router = new Router(getContext());
                router.attach("/test/{param}", GetTestResource.class);
                router.attach("/test/", EncodedJsonTestResource.class);
                return router;
            }
        };
        return application;
    }
}
