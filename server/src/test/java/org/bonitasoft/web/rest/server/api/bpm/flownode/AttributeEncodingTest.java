package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.logging.Level;

import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
import org.restlet.engine.Engine;
import org.restlet.resource.Get;
import org.restlet.routing.Router;


public class AttributeEncodingTest {

    private static Component component;
    private static Server server;
    private static String serverUrl;

    @BeforeClass
    public static void start() throws Exception {
        Engine.setLogLevel(Level.WARNING);
        component = new Component();
        server = component.getServers().add(Protocol.HTTP, 0);
        final Application application = createApplication();
        component.getDefaultHost().attach(application);
        component.start();
        serverUrl = "http://localhost:" + server.getEphemeralPort();
    }

    @AfterClass
    public static void stop() throws Exception {
        if (component != null && component.isStarted()) {
            component.stop();
        }
        component = null;
    }

    public static class GetTestResource extends CommonResource {

        public static final String TEST_URL = "/test/{param}";

        @Get
        public void getEncodedParam() {
            // Fails if the given input parameter is not correctly encoded:
            assertThat(getAttribute("param")).isEqualTo("varname_カキクケコ");
        }
    }

    @Test
    public void callResourceWithEncoding() throws Exception {
        final Request request = new Request(Method.GET, serverUrl + "/test/varname_カキクケコ");
        final Client c = new Client(Protocol.HTTP);
        final Response r = c.handle(request);
        assertThat(r.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
        c.stop();
    }

    public static class EncodedJsonTestResource extends CommonResource {

        public static final String ENCODED_JSON_URL = "/test/";

        @Get("json")
        public Dummy paramEncoded() {
            // Returns Japanese characters that should be encoded correctly in output:
            return new Dummy("カキクケコ");
        }
    }

    @Test
    public void callResourceReturnsAnEncodedJson() throws Exception {
        final Request request = new Request(Method.GET, serverUrl + "/test/");
        final Client c = new Client(Protocol.HTTP);
        final Response r = c.handle(request);
        assertThat(r.getStatus()).isEqualTo(Status.SUCCESS_OK);
        final String entityAsText = r.getEntityAsText();
        System.out.println(entityAsText);
        assertThat(entityAsText).isEqualTo("{\"field\":\"カキクケコ\"}");
        c.stop();
    }

    protected static Application createApplication() {
        final Application application = new Application() {

            @Override
            public Restlet createInboundRoot() {
                final Router router = new Router(getContext());
                router.attach(GetTestResource.TEST_URL, GetTestResource.class);
                router.attach(EncodedJsonTestResource.ENCODED_JSON_URL, EncodedJsonTestResource.class);
                return router;
            }
        };
        return application;
    }
}
