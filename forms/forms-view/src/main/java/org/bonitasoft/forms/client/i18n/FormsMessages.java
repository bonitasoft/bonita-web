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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Forms module message labels for i18n
 * 
 * @author Anthony Birembaut
 * 
 */
public interface FormsMessages extends Messages {

    String submissionConfirmationMessage();

    String instanceSubmissionConfirmationMessage(String caseId);

    String submissionConfirmationGoTo(String href);

    String refreshButtonLabel();

    String openUserXPButtonLabel();

    String openUserXPButtonTitle();

    String inboxEmptyMessage();

    String inboxEmptyForCaseMessage();

    String inboxEmptyForProcessMessage();

    String suspendedTaskMessage();

    String cancelledTaskMessage();

    String errorTaskMessage();

    String logoutButtonLabel();

    String trueLabel();

    String falseLabel();

    String hiddenPasswordLabel();

    String candidatesLabel(String candidates);

    String submitButtonLabel();

    String submitButtonTitle();

    String previousPageButtonLabel();

    String previousPageButtonTitle();

    String nextPageButtonLabel();

    String nextPageButtonTitle();

    String defaultMandatoryFieldSymbol();

    String defaultMandatoryFieldLabel();

    String dateFieldValidatorDefaultLabel();

    String longFieldValidatorDefaultLabel();

    String doubleFieldValidatorDefaultLabel();

    String integerFieldValidatorDefaultLabel();

    String floatFieldValidatorDefaultLabel();

    String shortFieldValidatorDefaultLabel();

    String charFieldValidatorDefaultLabel();

    String caseStartLabelPrefix();

    String caseRecapLabelPrefix();

    String uploadButtonLabel();

    String uploadButtonTitle();

    String cancelButtonLabel();

    String cancelButtonTitle();

    String modifyButtonLabel();

    String modifyButtonTitle();

    String removeButtonLabel();

    String removeButtonTitle();

    String uploadingLabel();

    String daysLabel();

    String hoursLabel();

    String minutesLabel();

    String secondsLabel();

    String forbiddenProcessReadMessage();

    String forbiddenApplicationReadMessage();

    String forbiddenProcessStartMessage();

    String forbiddenStepReadMessage();

    String forbiddenInstanceReadMessage();

    String migrationProductVersionMessage();

    String loginButtonLabel();

    String anonymousLabel();

    String removeColumnTitle();

    String addColumnTitle();

    String removeRowTitle();

    String addRowTitle();

    String paginationWithinLabel(String startElment, String endElement, String nbOfElements);

    String lastPageTitle();

    String nextPageTitle();

    String previousPageTitle();

    String firstPageTitle();

    String caseStartButtonLabel();

    String skippedFormMessage();
    
    String abortedFormMessage();

    String aTaskIsNowAvailableMessage();

    String noTaskAvailableMessage();
}
