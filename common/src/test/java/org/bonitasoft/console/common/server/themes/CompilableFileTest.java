/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.themes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.themes.exception.LessCompilationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompilableFileTest {

    @Rule
    public TemporaryFolder directory = new TemporaryFolder();

    @Mock
    ThemeArchive.ThemeModifier modifier;

    @Test
    public void should_compile_less_into_css() throws Exception {
        File lessStyle = directory.newFile("style.less");
        FileUtils.writeStringToFile(lessStyle, "body{width:1px}");
        given(modifier.resolve("style.less")).willReturn(lessStyle);

        byte[] compilation = new CompilableFile("style.less", "style.css").compile(modifier);

        assertThat(new String(compilation, "UTF-8")).isEqualTo("body {\n  width: 1px;\n}\n");
    }

    @Test
    public void should_add_compiled_file_to_compile_less_into_css() throws Exception {
        File lessStyle = directory.newFile("style.less");
        given(modifier.resolve("style.less")).willReturn(lessStyle);

        new CompilableFile("style.less", "style.css").compile(modifier);

        verify(modifier).add("style.css", new byte[0]);
    }

    @Test(expected = LessCompilationException.class)
    public void should_throw_LessCompilationException_when_fail_to_resolve_input_file() throws Exception {
        given(modifier.resolve("style.less")).willReturn(new File("style.less"));

        new CompilableFile("style.less", "style.css").compile(modifier);
    }

    @Test(expected = LessCompilationException.class)
    public void should_throw_LessCompilationException_when_fail_to_compile_input_file() throws Exception {
        File lessStyle = directory.newFile("style.less");
        FileUtils.writeStringToFile(lessStyle, "not compilable content");
        given(modifier.resolve("style.less")).willReturn(lessStyle);

        new CompilableFile("style.less", "style.css").compile(modifier);
    }
}