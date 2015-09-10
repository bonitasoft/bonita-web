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

    private final String input;

    private final String output;

    public static final CompilableFile[] ALWAYS_COMPILED_FILES = new CompilableFile[] {
            new CompilableFile("skin/bootstrap/portal/main.less", "bonita-skin.css"),
    };

    public CompilableFile(final String input, final String output) {
        this.input = input;
        this.output = output;
    }

    public byte[] compile(final ThemeArchive.ThemeModifier modifier) {
        try {
            final LessCompiler lessCompiler = new LessCompiler();
            lessCompiler.setEncoding("UTF-8");
            final byte[] compilation = lessCompiler.compile(modifier.resolve(input)).getBytes("UTF-8");
            modifier.add(output, compilation);
            return compilation;
        } catch (final LessException e) {
            throw new LessCompilationException("Failed to compile " + input, e);
        } catch (final IOException e) {
            throw new LessCompilationException("Failed to compile " + input, e);
        }
    }
}