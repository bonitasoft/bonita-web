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
package org.bonitasoft.web.rest.server.framework.utils.converter;

import static org.junit.Assert.assertTrue;

import javax.servlet.Servlet;

import org.bonitasoft.web.rest.server.framework.utils.converter.typed.BooleanConverter;
import org.bonitasoft.web.rest.server.framework.utils.converter.typed.DoubleConverter;
import org.bonitasoft.web.rest.server.framework.utils.converter.typed.IntegerConverter;
import org.bonitasoft.web.rest.server.framework.utils.converter.typed.LongConverter;
import org.bonitasoft.web.rest.server.framework.utils.converter.typed.StringConverter;
import org.junit.Before;
import org.junit.Test;



/**
 * @author Colin PUY
 *
 */
public class ConverterFactoryTest {

    private ConverterFactory factory;

    @Before
    public void initFactory() {
        factory = new ConverterFactory();
    }
    
    @Test
    public void factoryCreateABooleanConverterForBooleanClassName() {
        
        Converter<?> createConverter = factory.createConverter(Boolean.class.getName());
        
        assertTrue(createConverter instanceof BooleanConverter);
    }

    @Test
    public void factoryCreateADoubleConverterForDoubleClassName() {
        
        Converter<?> createConverter = factory.createConverter(Double.class.getName());
        
        assertTrue(createConverter instanceof DoubleConverter);
    }
    
    @Test
    public void factoryCreateALongConverterForLongClassName() {
        
        Converter<?> createConverter = factory.createConverter(Long.class.getName());
        
        assertTrue(createConverter instanceof LongConverter);
    }
    
    @Test
    public void factoryCreateAStringConverterForStringClassName() {
        
        Converter<?> createConverter = factory.createConverter(String.class.getName());
        
        assertTrue(createConverter instanceof StringConverter);
    }
    
    @Test
    public void factoryCreateAnIntegerConverterForIntegerClassName() {
        
        Converter<?> createConverter = factory.createConverter(Integer.class.getName());
        
        assertTrue(createConverter instanceof IntegerConverter);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void factoryThrowExceptionForUnsuportedConverter() {
        factory.createConverter(Servlet.class.getName());
    }

}
