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
package org.bonitasoft.forms.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Forms module error labels for i18n
 * @author Anthony Birembaut
 *
 */
public interface FormsErrors extends Messages {
    
    String confirmationTempateError();
    
    String errorTempateError();
    
    String credentialsSessionError();
    
    String pageRetrievalError();
    
    String pageIndexError(int index);
    
    String fieldValidationError();
    
    String pageValidationError();
    
    String formSubmissionError();
    
    String nextTaskRetrievalError();
    
    String pageListRetrievalError();
    
    String processInstantiationError();
    
    String anyTaskRetrievalError();
    
    String processConfigRetrievalError();
    
    String applicationConfigRetrievalError();
       
    String taskExecutionError();
       
    String taskRetrievalError();
    
    String nextSubprocessURLRetrievalError();
    
    String fileTooBigError();
    
    String fileTooBigErrorWithSize(String maxSize);
    
    String fileTooBigErrorWithName(String fileName);
    
    String fileTooBigErrorWithNameSize(String fileName, String maxSize);
    
    String taskFormSkippedError();

    String nothingToDisplay();

    String formAlreadySubmittedError();
    
    String formAlreadySubmittedOrCancelledError();

    String formUrlParameterIsMandatoryError();
}
