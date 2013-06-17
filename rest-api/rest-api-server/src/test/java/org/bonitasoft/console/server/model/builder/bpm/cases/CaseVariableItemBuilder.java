/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.server.model.builder.bpm.cases;

import java.io.Serializable;

import org.bonitasoft.web.rest.api.model.bpm.cases.CaseVariableItem;


/**
 * @author Colin PUY
 *
 */
public class CaseVariableItemBuilder {

    private long caseId = 1L;
    private String name = "aName";
    private Serializable value = "aValue";
    private String type = "aType";
    private String description = "aDescription";
    
    public static CaseVariableItemBuilder aCaseVariableItem() {
        return new CaseVariableItemBuilder();
    }

    public CaseVariableItem build() {
        return new CaseVariableItem(caseId, name, value, type, description);
    }
    
    public CaseVariableItemBuilder withCaseId(long caseId) {
        this.caseId = caseId;
        return this;
    }
}
