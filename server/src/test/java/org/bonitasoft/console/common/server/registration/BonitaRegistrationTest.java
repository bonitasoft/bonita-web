/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URLEncoder;

import org.bonitasoft.console.common.server.preferences.properties.SimpleProperties;
import org.junit.Before;
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

    @Mock
    SimpleProperties simpleProperties;

    @Spy
    @InjectMocks
    BonitaRegistration bonitaRegistration;

    @Before
    public void beforeEach() throws Exception {
        doReturn(simpleProperties).when(bonitaRegistration).getPlatformPreferences();
    }

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

        verify(systemInfoSender).call(contains(URLEncoder.encode("<value key=\"origin\">living-app</value>", "UTF-8")),
                eq("email=" + URLEncoder.encode(BonitaRegistration.DEFAULT_EMAIL, "UTF-8")));
    }

    @Test
    public void sendUserInfo_should_not_call_service_when_data_is_invalid() throws Exception {
        when(bonitaRegistration.isDataValidToSend(anyString(), anyString())).thenReturn(false);

        bonitaRegistration.sendUserInfo();

        verify(systemInfoSender, never()).call(anyString(), anyString());
    }

    @Test
    public void sendUserInfoIfNotSent_should_call_service() throws Exception {
            bonitaRegistration.sendUserInfoIfNotSent();

            verify(bonitaRegistration).sendUserInfo();
    }

    @Test
    public void sendUserInfoIfNotSent_should_not_call_service_when_info_already_sent() throws Exception {
            when(simpleProperties.getProperty(BonitaRegistration.BONITA_INFO_SENT)).thenReturn("1");

            bonitaRegistration.sendUserInfoIfNotSent();

            verify(bonitaRegistration, never()).sendUserInfo();
    }

    @Test
    public void sendUserInfoIfNotSent_should_not_call_service_when_max_try_reached() throws Exception {
            when(simpleProperties.getProperty(BonitaRegistration.BONITA_USER_REGISTER_TRY)).thenReturn(
                    Integer.toString(BonitaRegistration.BONITA_USER_REGISTER_MAXTRY + 1));

            bonitaRegistration.sendUserInfoIfNotSent();

            verify(bonitaRegistration, never()).sendUserInfo();
    }
}
