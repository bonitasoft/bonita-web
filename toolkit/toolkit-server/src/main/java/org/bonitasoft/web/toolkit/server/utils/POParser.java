/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
 */

package org.bonitasoft.web.toolkit.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author Séverin Moussel
 * 
 */
public class POParser {

    private final Map<String, String> translations = new TreeMap<String, String>();

    public POParser() {
    }

    public static Map<String, String> parse(final String path) {
        return new POParser().parseFile(path);
    }

    public static Map<String, String> parse(final File file) {
        return new POParser().parseFile(file);
    }

    public Map<String, String> parseFile(final String path) {
        return this.parseFile(new File(path));
    }

    public Map<String, String> parseFile(final File file) {
        try {

            final FileReader fileReader = new FileReader(file);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);

            parseBuffer(bufferedReader);

            bufferedReader.close();
            fileReader.close();

        } catch (final java.io.FileNotFoundException e) {
            // TODO Log error
        } catch (final java.io.IOException e) {
            // TODO Log error
        }

        return this.translations;
    }

    /**
     * Parse the bufferedReader
     * 
     */

    private void parseBuffer(final BufferedReader bufferedReader) {
        String source = null;
        String destination = null;

        String line;
        while ((line = nextLine(bufferedReader)) != null) {
            if (line.toLowerCase().startsWith("msgid ")) {
                source = readContent(line);
            } else if (line.toLowerCase().startsWith("msgstr ")) {
                destination = readContent(line);
            }
            if (source != null && destination != null && destination.length() > 0) {
                this.translations.put(source, destination);
                source = null;
                destination = null;
            }
        }
    }

    /**
     * Retrieve the main content from a line by getting the content in quotes
     * 
     * @param value
     * @return
     */
    private String readContent(final String value) {
        return value.replaceAll("^.*?\"(.*)\"$", "$1");
    }

    /**
     * Read the next valid line (avoid empty lines and comments
     * 
     * @return
     */
    private String nextLine(final BufferedReader bufferedReader) {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && !line.startsWith("#~")) {
                    return line;
                }
            }
        } catch (final IOException e) {
            // TODO log error
        }
        return null;
    }
}
