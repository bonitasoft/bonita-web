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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bonitasoft.engine.session.APISession;

/**
 * @author Séverin Moussel
 * 
 */
public class TestCategoryFactory {

    private final Map<String, TestCategory> categories = new HashMap<>();

    private TestCategory _getCategory(final String name, final String description) {
        if (!this.categories.containsKey(name)) {
            this.categories.put(name, new TestCategory(name, description));
        }
        return this.categories.get(name);
    }

    private static String getRandomString() {
        return String.valueOf(new Random().nextLong());
    }

    private static final TestCategoryFactory instance = new TestCategoryFactory();

    public static TestCategoryFactory getInstance() {
        return instance;
    }

    public static TestCategory getCategory(final String name, final String description) {
        return getInstance()._getCategory(name, description);
    }

    public static List<TestCategory> getCategories(final int count) {
        final List<TestCategory> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            results.add(getRandomCategory());
        }

        return results;
    }

    public static List<TestCategory> getAllCategories(final APISession apiSession) {
        final List<TestCategory> all = TestCategory.getAll(apiSession);
        for (TestCategory testCategory : all) {
            getInstance().getCategoriesList().put(testCategory.getCategory().getName(), testCategory);
        }
        return all;
    }

    public static TestCategory getRandomCategory() {
        return getCategory(getRandomString(), getRandomString());
    }

    public void clear() {
        for (TestCategory testCategory : getCategoriesList().values()) {
            testCategory.delete();
        }
        getCategoriesList().clear();
    }

    /**
     * @return the userList
     */
    private Map<String, TestCategory> getCategoriesList() {
        return this.categories;
    }

    public void check() {
        if (!getCategoriesList().isEmpty()) {
            throw new RuntimeException(this.getClass().getName() + " cannot be reset because the list is not empty: " + getCategoriesList());
        }
    }

    public static void removeTestCategoryFromList(TestCategory category) {
        getInstance().getCategoriesList().remove(category.getCategory().getName());
    }
}
