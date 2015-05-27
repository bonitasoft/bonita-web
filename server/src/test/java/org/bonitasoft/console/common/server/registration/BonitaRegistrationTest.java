package org.bonitasoft.console.common.server.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URLEncoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BonitaRegistrationTest {

    @Mock
    SystemInfoSender systemInfoSender;

    @Spy
    @InjectMocks
    BonitaRegistration bonitaRegistration;

    @Test
    public void sendUserInfo_should_return_false_when_service_call_fails() throws Exception {
        when(systemInfoSender.call(anyString(), anyString())).thenReturn(false);

        assertThat(bonitaRegistration.sendUserInfo()).isFalse();
    }

    @Test
    public void sendUserInfo_should_return_true_when_service_call_succeeds() throws Exception {
        when(systemInfoSender.call(anyString(), anyString())).thenReturn(true);

        assertThat(bonitaRegistration.sendUserInfo()).isTrue();
    }

    @Test
    public void sendUserInfo_should_send_origin() throws Exception {
        when(systemInfoSender.call(anyString(), anyString())).thenReturn(true);

        bonitaRegistration.sendUserInfo();

        verify(systemInfoSender).call(contains(URLEncoder.encode("<value key=\"origin\">business-app</value>", "UTF-8")),
                eq("email=" + URLEncoder.encode(BonitaRegistration.DEFAULT_EMAIL, "UTF-8")));
    }

    @Test
    public void sendUserInfo_should_not_call_service_when_data_is_invalid() throws Exception {
        when(bonitaRegistration.isDataValidToSend(anyString(), anyString())).thenReturn(false);

        bonitaRegistration.sendUserInfo();

        verify(systemInfoSender, never()).call(anyString(), anyString());
    }

}
