/**
 * Copyright (C) 2019 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.page;

import java.io.File;

import org.bonitasoft.console.common.server.utils.UnzipUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CustomPageContentValidatorTest {

    private CustomPageContentValidator customPageContentValidator;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        customPageContentValidator = new CustomPageContentValidator();
    }
    @Test
    public void zip_with_index_in_resources_should_be_valid() throws Exception {
        final File zipFileResource = new File(getClass().getResource("/pageWithIndexInResources.zip").toURI());
        UnzipUtil.unzip(zipFileResource, new File("target" + File.separator + "pageWithIndexInResources").getPath(), false);
        final File unzipFolder = new File("target" + File.separator + "pageWithIndexInResources");

        customPageContentValidator.validate(unzipFolder);
    }
    
    @Test
    public void should_throw_exception_when_theme_css_not_found() throws Exception {
        final File invalidThemePage = new File(getClass().getResource("/invalidThemePage").toURI());
        
        expectedException.expect(InvalidPageZipContentException.class);
        expectedException.expectMessage("theme.css is missing.");
        
        customPageContentValidator.validate(invalidThemePage);
    }
    
    @Test
    public void should_throw_exception_when_page_properties_not_found() throws Exception {
        final File invalidCustomPage = new File(getClass().getResource("/invalidCustomPage").toURI());
        
        expectedException.expect(InvalidPageZipContentException.class);
        expectedException.expectMessage("page.properties descriptor is missing.");
        
        customPageContentValidator.validate(invalidCustomPage);
    }
    
    @Test
    public void should_throw_exception_when_index_not_found() throws Exception {
        final File invalidFormPage = new File(getClass().getResource("/invalidFormPage").toURI());
        
        expectedException.expect(InvalidPageZipContentException.class);
        expectedException.expectMessage("index.html or Index.groovy is missing.");
        
        customPageContentValidator.validate(invalidFormPage);
    }
    
}
