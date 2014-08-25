/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.server.rest.exception.mapper;

import static org.bonitasoft.web.server.rest.assertions.ResponseAssert.assertThat;
import static org.mockito.Mockito.doThrow;

import javax.ws.rs.core.Response;

import org.bonitasoft.engine.exception.BonitaRuntimeException;
import org.bonitasoft.web.server.rest.utils.ExceptionMapperTest;
import org.junit.Test;

public class BonitaRuntimeExceptionMapperTest extends ExceptionMapperTest {
    
    @Test
    public void should_respond_500_internal_error_on_BonitaRuntimeException() throws Exception {
        doThrow(new BonitaRuntimeException("a bonita runtime exception")).when(fakeService).doSomething();
        
        Response response = target("fake/resource").request().get();
        
        assertThat(response).hasStatus(500);
        assertThat(response).hasJsonBodyEqual(readFile("bonita_runtime_exception.json"));
    }
    
}
