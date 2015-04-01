/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.test.toolkit.bpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.category.Category;
import org.bonitasoft.engine.bpm.category.CategoryCriterion;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;

/**
 * @author Séverin Moussel
 * 
 */
public class TestCategory {

    private final Category category;

    /**
     * Default Constructor.
     * 
     */
    public TestCategory(final Category category) {
        this.category = category;
        /*
        System.err.println("\n\n");
        System.err.println("Building category: " + category.getName());
        Thread.dumpStack();
        System.err.println("\n\n");
        */
    }

    /**
     * Default Constructor.
     * 
     */
    public TestCategory(final APISession apiSession, final String name, final String description) {
        this(createCategory(apiSession, name, description));
    }

    public TestCategory(final String name, final String description) {
        this(TestToolkitCtx.getInstance().getInitiator().getSession(), name, description);
    }

    private static Category createCategory(final APISession apiSession, final String name, final String description) {
        try {
            return TenantAPIAccessor.getProcessAPI(apiSession).createCategory(name, description);
        } catch (final Exception e) {
            throw new TestToolkitException("Can't create category.", e);
        }
    }

    public void delete(final APISession apiSession) {
        try {
            TenantAPIAccessor.getProcessAPI(apiSession).deleteCategory(this.category.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't delete category", e);
        }
    }

    public void delete() {
        try {
            delete(TestToolkitCtx.getInstance().getInitiator().getSession());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't delete category", e);
        }
    }

    public TestCategory addProcess(final APISession apiSession, final TestProcess process) {
        return addProcess(apiSession, process.getId());
    }

    public TestCategory addProcess(final APISession apiSession, final long processId) {
        try {
            TenantAPIAccessor.getProcessAPI(apiSession).addCategoriesToProcess(processId, Arrays.asList(getId()));
            return this;
        } catch (final Exception e) {
            throw new TestToolkitException("Can't add this process to this category.", e);
        }
    }

    public Category getCategory() {
        return this.category;
    }

    public static List<TestCategory> getAll(final APISession apiSession) {
        try {
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
            final int nbCat = (int) processAPI.getNumberOfCategories();
            final List<Category> catList = processAPI.getCategories(0, nbCat, CategoryCriterion.NAME_ASC);
            final List<TestCategory> testCatList = new ArrayList<TestCategory>();
            for (final Category cat : catList) {
                testCatList.add(new TestCategory(cat));
            }
            return testCatList;
        } catch (final Exception e) {
            throw new TestToolkitException("Can't get categories", e);
        }

    }

    public long getId() {
        return this.category.getId();
    }
}
