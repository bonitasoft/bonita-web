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

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.themes.exception.LessCompilationException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

public class CompilableFile {

    private static final Logger LOGGER = Logger.getLogger(CompilableFile.class.getName());

    private final String input;

    private final String output;

    public static final CompilableFile[] ALWAYS_COMPILED_FILES = new CompilableFile[]{
            new CompilableFile("skin/bootstrap/portal/main.less", "bonita-skin.css"),
    };

    public CompilableFile(final String input, final String output) {
        this.input = input;
        this.output = output;
    }

    public byte[] compile(final ThemeArchive.ThemeModifier modifier) {
        final LessCompiler lessCompiler = new LessCompiler();
        lessCompiler.setEncoding("UTF-8");
        File file = modifier.resolve(input);
        if (!Files.exists(file.toPath())) {
            LOGGER.warning(format("Theme compilation failure. File <%s> not found", file));
            return new byte[0];
        }
        try {
            final byte[] compilation = lessCompiler.compile(file).getBytes("UTF-8");
            modifier.add(output, compilation);
            return compilation;
        } catch (final LessException | IOException e) {
            throw new LessCompilationException("Failed to compile " + input, e);
        }
    }
}