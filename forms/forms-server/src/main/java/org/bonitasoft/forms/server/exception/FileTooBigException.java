/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.exception;

/**
 * Exception thrown when the file to upload or download is too big
 * 
 * @author Anthony Birembaut
 */
public class FileTooBigException extends Exception {

    /**
     * UID
     */
    private static final long serialVersionUID = -5246821093446995056L;
    
    /**
     * The name of the file that is too big
     */
    protected String fileName;
    
    /**
     * The file's max size limitation
     */
    protected String maxSize;

    /**
     * constructor
     */
    public FileTooBigException() {
        super();
    }

    /**
     * @param message message associated with the exception
     * @param fileName the filename
     * @param cause cause of the exception
     */
    public FileTooBigException(final String message, final String fileName, final Throwable cause) {
        super(message, cause);
        this.fileName = fileName;
    }

    /**
     * @param message message associated with the exception
     * @param fileName the filename
     */
    public FileTooBigException(final String message, final String fileName, final String maxSize) {
        super(message);
        this.fileName = fileName;
        this.maxSize = maxSize;
    }

    
    public String getMaxSize() {
        return maxSize;
    }

    /**
     * @param fileName the filename
     * @param cause cause of the exception
     */
    public FileTooBigException(final String fileName, final Throwable cause) {
        super(cause);
        this.fileName = fileName;
    }

    /**
     * @return the file name (null if it hasn't been set)
     */
    public String getFileName() {
        return fileName;
    }
    
}
