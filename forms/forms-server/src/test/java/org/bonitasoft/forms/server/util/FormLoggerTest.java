/*
 *
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
 *
 */
package org.bonitasoft.forms.server.util;


import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Baptiste Mesta
 */
public class FormLoggerTest {

    FormLogger formLogger;

    @Before
    public void before(){
        formLogger = spy(new FormLogger("theclass"));

    }

    @Test
    public void log_with_message(){
        //given
        doReturn(true).when(formLogger).isLoggable(Level.INFO);
        doNothing().when(formLogger).internalLog(any(Level.class), anyString(), any(Exception.class));
        //when
        formLogger.log(Level.INFO,"My message", Collections.<String, Object>emptyMap());
        //then
        verify(formLogger).internalLog(Level.INFO,"My message",null);
    }

    @Test
    public void log_with_INFO_when_not_loggable(){
        //given
        doReturn(false).when(formLogger).isLoggable(Level.INFO);
        doNothing().when(formLogger).internalLog(any(Level.class), anyString(), any(Exception.class));
        //when
        formLogger.log(Level.INFO,"My message", Collections.<String, Object>emptyMap());
        //then
        verify(formLogger,never()).internalLog(Level.INFO, "My message", null);
    }

    @Test
    public void log_with_exception(){
        //given
        doReturn(true).when(formLogger).isLoggable(Level.INFO);
        doNothing().when(formLogger).internalLog(any(Level.class), anyString(), any(Exception.class));
        //when
        IllegalStateException exception = new IllegalStateException();
        formLogger.log(Level.INFO,"My message", exception, Collections.<String, Object>emptyMap());
        //then
        verify(formLogger).internalLog(Level.INFO,"My message",exception);
    }

    @Test
    public void log_with_FINEST(){
        //given
        doReturn(true).when(formLogger).isLoggable(Level.FINEST);
        doNothing().when(formLogger).internalLog(any(Level.class), anyString(), any(Exception.class));
        //when
        IllegalStateException exception = new IllegalStateException();
        formLogger.log(Level.FINEST,"My message", exception, Collections.<String, Object>emptyMap());
        //then
        verify(formLogger).internalLog(Level.FINEST,"My message",exception);

    }

    @Test
    public void log_with_SEVERE(){
        //given
        doReturn(true).when(formLogger).isLoggable(Level.FINEST);
        doNothing().when(formLogger).internalLog(any(Level.class), anyString(), any(Exception.class));
        //when
        IllegalStateException exception = new IllegalStateException();
        formLogger.log(Level.SEVERE,"My message", exception, Collections.<String, Object>emptyMap());
        //then
        verify(formLogger).internalLog(Level.SEVERE,"My message",exception);

    }

}
