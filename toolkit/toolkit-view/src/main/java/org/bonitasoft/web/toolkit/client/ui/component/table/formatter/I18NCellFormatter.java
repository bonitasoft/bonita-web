/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.toolkit.client.ui.component.table.formatter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;


/**
 * @author Fabio Lombardi
 *
 */
public class I18NCellFormatter extends ItemTableCellFormatter {

    @Override
    public void execute() {
        this.table.addCell(_(getAttribute().read(getItem())));
    }

}
