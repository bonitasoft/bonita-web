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

import java.io.IOException;

import org.bonitasoft.console.common.server.themes.exception.LessCompilationException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

public class CompilableFile {

    private String input;

    private String output;

    public static final CompilableFile[] ALWAYS_COMPILED_FILES = new CompilableFile[] {
            new CompilableFile("skin/bootstrap/portal/main.less", "bonita-skin.css"),
            new CompilableFile("skin/bootstrap/applications/main.less", "applications-skin.css")
    };

    public CompilableFile(String input, String output) {
        this.input = input;
        this.output = output;
    }

    public byte[] compile(ThemeArchive.ThemeModifier modifier) {
        try {
            byte[] compilation = new LessCompiler().compile(modifier.resolve(input)).getBytes();
            modifier.add(output, compilation);
            return compilation;
        } catch (LessException e) {
            throw new LessCompilationException("Failed to compile " + input, e);
        } catch (IOException e) {
            throw new LessCompilationException("Failed to compile " + input, e);
        }
    }
}