/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.utils.converter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bonitasoft.web.rest.server.utils.converter.Converter;
import org.bonitasoft.web.rest.server.utils.converter.ConverterFactory;
import org.bonitasoft.web.rest.server.utils.converter.TypeConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Colin PUY
 * 
 */
public class TypeConverterTest {

    private TypeConverter converter;

    @Mock
    private ConverterFactory factory;

    @Before
    public void initConverter() {
        MockitoAnnotations.initMocks(this);

        converter = new TypeConverter(factory);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void convertCreateAFactoryAndConvertStringValueToSerializableObject() throws Exception {
        Converter typedConverter = mock(Converter.class);
        when(factory.createConverter(anyString())).thenReturn(typedConverter);

        converter.convert("aClassName", "somethingToconvert");

        verify(typedConverter).convert("somethingToconvert");
    }

}
