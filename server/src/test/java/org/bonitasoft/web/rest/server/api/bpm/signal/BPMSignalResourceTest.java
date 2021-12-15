package org.bonitasoft.web.rest.server.api.bpm.signal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.web.rest.server.BonitaRestletApplication;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class BPMSignalResourceTest extends RestletTest {

	@Mock
	ProcessAPI processAPI;

	@InjectMocks
	@Spy
	BPMSignalResource restResource;

	@Override
	protected ServerResource configureResource() {
		return new BPMSignalResource(processAPI);
	}

	@Test
	public void should_broadcast_signal_of_a_given_name() throws Exception {
		restResource.broadcast(BPMSignal.create("signalName"));

		verify(processAPI).sendSignal("signalName");
	}

	@Test
	public void should_throw_illegal_argument_exception_if_name_not_set() throws Exception {
		var signal = BPMSignal.create(null);
		
		assertThrows(IllegalArgumentException.class, () -> restResource.broadcast(signal));
	}
	
	@Test
	public void should_throw_illegal_argument_exception_if_signal_not_set() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> restResource.broadcast(null));
	}

	@Test
	public void should_post_request_return_204_status() throws Exception {
		Response response = request(BonitaRestletApplication.BPM_SIGNAL_URL).post("{\"name\" : \"hello\"}");

		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
	}

	@Test
	public void should_support_encoded_characters_for_signal_name() throws Exception {
		Response response = request(BonitaRestletApplication.BPM_SIGNAL_URL).post("{\"name\" : \"My Signàl\"}");

		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
		verify(processAPI).sendSignal("My Signàl");
	}


}
