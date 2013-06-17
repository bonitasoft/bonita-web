package org.bonitasoft.console.server.api.bpm.process;

import static junit.framework.Assert.assertNull;
import static org.bonitasoft.console.server.model.builder.bpm.process.CategoryItemBuilder.aCategoryItem;
import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.console.server.datastore.bpm.process.CategoryDatastore;
import org.bonitasoft.engine.bpm.category.Category;
import org.bonitasoft.engine.bpm.category.CategoryNotFoundException;
import org.bonitasoft.test.toolkit.bpm.TestCategory;
import org.bonitasoft.test.toolkit.bpm.TestCategoryFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.api.model.bpm.process.CategoryItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

/**
 * @author Nicolas Tith
 */
public class APICategoryIntegrationTest extends AbstractConsoleTest {

    private APICategory api;

    @Override
    public void consoleTestSetUp() throws Exception {
        this.api = new APICategory();
        this.api.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/category"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getRidleyScott();
    }

    /**
     * Fetch a Category by id from engine
     * 
     * @return the category or null if not found
     */
    private CategoryItem getFromEngine(long categoryId) {
        try {
            return new CategoryDatastore(getInitiator().getSession()).get(makeAPIID(categoryId));
        } catch (APIException e) {
            if (e.getCause() instanceof CategoryNotFoundException) {
                return null;
            }
            throw e;
        }
    }

    @Test
    public void testSearchCategoryItem() throws Exception {
        final List<TestCategory> catList = TestCategoryFactory.getCategories(3);

        // Search the CommentItem
        final List<CategoryItem> actualCatList = this.api.runSearch(0, 10, null, null, new HashMap<String, String>(), new ArrayList<String>(),
                new ArrayList<String>()).getResults();
        Assert.assertNotNull("Categories not found", actualCatList);
        Assert.assertTrue(catList.size() == 3);
    }

    @Test
    public void addCategoryTest() {
        // API call
        final CategoryItem categoryItem = new CategoryItem();
        categoryItem.setName("categoryTest");
        categoryItem.setDescription("categoryDescription");
        this.api.runAdd(categoryItem);

        // Check
        final List<TestCategory> catList = TestCategoryFactory.getAllCategories(getInitiator().getSession());
        Assert.assertEquals("No categories added", 1, catList.size());
        final Category resultCategory = catList.get(0).getCategory();
        Assert.assertEquals("Wrong category found (not same name)", categoryItem.getName(), resultCategory.getName());
        Assert.assertEquals("Wrong category found (not same description)", categoryItem.getDescription(), resultCategory.getDescription());

    }

    @Test
    public void updateCategoryTest() {
        final String newDescription = "Lorem ipsum dolor sit amet";

        // Init
        final TestCategory category = TestCategoryFactory.getRandomCategory();

        // Update
        final Map<String, String> updates = new HashMap<String, String>();
        updates.put(CategoryItem.ATTRIBUTE_DESCRIPTION, newDescription);
        this.api.runUpdate(APIID.makeAPIID(category.getCategory().getId()), updates);

        // Get
        final CategoryItem output = this.api.runGet(APIID.makeAPIID(category.getCategory().getId()), new ArrayList<String>(), new ArrayList<String>());

        Assert.assertNotNull("Category not found", output);
        Assert.assertEquals("Update of category failed", newDescription, output.getDescription());

    }

    @Test
    public void getCategoryTest() {
        // Init
        final TestCategory category = TestCategoryFactory.getRandomCategory();

        // API Call
        final CategoryItem catItem = this.api.runGet(APIID.makeAPIID(category.getId()), new ArrayList<String>(), new ArrayList<String>());

        Assert.assertNotNull("Category not found", category);
        Assert.assertEquals("Wrong category description found", category.getCategory().getDescription(), catItem.getDescription());
        Assert.assertEquals("Wrong category found", category.getCategory().getName(), catItem.getName());
    }

    @Test
    public void deleteCategoryTest() {
        final TestCategory category = TestCategoryFactory.getRandomCategory();

        this.api.runDelete(Arrays.asList(APIID.makeAPIID(category.getId())));

        assertNull(getFromEngine(category.getId()));
    }

    @Test(expected = APIForbiddenException.class)
    public void addingTwiceSameCategoryIsForbidden() throws Exception {
        CategoryItem categoryItem = aCategoryItem().build();

        api.runAdd(categoryItem);
        api.runAdd(categoryItem);
    }
}
