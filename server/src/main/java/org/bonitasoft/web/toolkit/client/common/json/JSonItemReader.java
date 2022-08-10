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
package org.bonitasoft.web.toolkit.client.common.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * This class is just a set of functions used to read the JSon returned by the GWT server side
 * 
 * @author Séverin Moussel
 */
public class JSonItemReader {

    public static boolean APPLY_VALIDATORS = true;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UNSERIALIZER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static JSonUnserializer UNSERIALIZER = null;

    /**
     * @param unserializer
     *            the unserializer to set
     */
    public static void setUnserializer(final JSonUnserializer unserializer) {
        UNSERIALIZER = unserializer;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PARSING
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Parse the JSon of a single Item.
     * 
     * @param json
     *            The json as a string
     * @param itemDefinition
     *            The definition of the IItem to retrieve.
     * @return This function returns an IItem corresponding to the definition.
     */
    public <E extends IItem> E getItem(final String json, final ItemDefinition<E> itemDefinition) {
        return parseItem(json, itemDefinition);
    }

    /**
     * Parse the JSon of a list of Items.
     * 
     * @param json
     *            The json as a string
     * @param itemDefinition
     *            The definition of the Items to retrieve.
     * @return This function returns a list of Items corresponding to the definition.
     */
    public <E extends IItem> List<E> getItems(final String json, final ItemDefinition<E> itemDefinition) {
        return parseItems(json, itemDefinition);
    }

    /**
     * Parse the JSon of a list of Items.
     * 
     * @param json
     *            The json as a string
     * @param itemDefinition
     *            The definition of the Items to retrieve.
     * @return This function returns a list of Items corresponding to the definition.
     */
    public static <E extends IItem> List<E> parseItems(final String json, final ItemDefinition<E> itemDefinition) {
        return parseItems(json, itemDefinition, APPLY_VALIDATORS);
    }

    /**
     * Parse the JSon of a list of Items.
     * 
     * @param json
     *            The json as a string
     * @param itemDefinition
     *            The definition of the Items to retrieve.
     * @return This function returns a list of Items corresponding to the definition.
     */
    public static <E extends IItem> List<E> parseItems(final String json, final ItemDefinition<E> itemDefinition, final boolean applyValidators) {
        AbstractTreeNode<String> tree = UNSERIALIZER._unserializeTree(json);

        if (tree instanceof TreeIndexed<?> && ((TreeIndexed<String>) tree).get("results") != null) {
            tree = ((TreeIndexed<String>) tree).get("results");
        }

        if (!(tree instanceof Tree<?>)) {
            return new ArrayList<E>();
        }

        return parseItems((Tree<String>) tree, itemDefinition, applyValidators);
    }

    private static <E extends IItem> List<E> parseItems(final Tree<String> tree, final ItemDefinition<E> itemDefinition, final boolean applyValidators) {
        final List<E> itemList = new LinkedList<E>();
        for (final AbstractTreeNode<String> node : tree.getNodes()) {
            if (!(node instanceof TreeIndexed<?>)) {
                throw new IllegalArgumentException("JSon format error");
            }
            itemList.add(parseItem((TreeIndexed<String>) node, itemDefinition, applyValidators));
        }
        return itemList;
    }

    /**
     * Parse a Map on a JSon String
     * 
     * @param json
     */
    public static Map<String, String> parseMap(final String json) {
        final AbstractTreeNode<String> tree = UNSERIALIZER._unserializeTree(json);

        if (!(tree instanceof TreeIndexed<?>)) {
            return new HashMap<String, String>();
        }

        return parseMap((TreeIndexed<String>) tree);
    }

    /**
     * Parse a Map on a Tree
     * 
     * @param tree
     */
    private static Map<String, String> parseMap(final TreeIndexed<String> tree) {
        final Map<String, String> result = new HashMap<String, String>();

        for (final Entry<String, String> entry : tree.getValues().entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Parse an item based on a JSon String
     * 
     * @param json
     * @param itemDefinition
     */
    public static <E extends IItem> E parseItem(final String json, final ItemDefinition<E> itemDefinition) {
        return parseItem(json, itemDefinition, APPLY_VALIDATORS);
    }

    /**
     * Parse an item based on a JSon String
     * 
     * @param json
     * @param itemDefinition
     */
    public static <E extends IItem> E parseItem(final String json, final ItemDefinition<E> itemDefinition, final boolean applyValidators) {
        AbstractTreeNode<String> tree = UNSERIALIZER._unserializeTree(json);

        if (tree instanceof Tree<?>) {
            tree = ((Tree<String>) tree).get(0);

            if (!(tree instanceof TreeIndexed<?>)) {
                return itemDefinition.createItem();
            }
        }

        return parseItem((TreeIndexed<String>) tree, itemDefinition);
    }

    /**
     * Parse an item based on a TreeIndexed
     * 
     * @param tree
     * @param itemDefinition
     */
    private static <E extends IItem> E parseItem(final TreeIndexed<String> tree, final ItemDefinition<E> itemDefinition) {
        return parseItem(tree, itemDefinition, APPLY_VALIDATORS);
    }

    /**
     * Parse an item based on a TreeIndexed
     * 
     * @param tree
     * @param itemDefinition
     */
    private static <E extends IItem> E parseItem(final TreeIndexed<String> tree, final ItemDefinition<E> itemDefinition, final boolean applyValidators) {
        final E item = itemDefinition.createItem();

        item.setApplyValidators(applyValidators);

        for (final Entry<String, AbstractTreeNode<String>> entry : tree.getNodes().entrySet()) {
            // primitive type
            if (entry.getValue() instanceof TreeLeaf<?>) {
                item.setAttribute(entry.getKey(), ((TreeLeaf<String>) entry.getValue()).getValue());
            // json object
            } else if (entry.getValue() instanceof TreeIndexed<?>) {
                item.setDeploy(
                        entry.getKey(),
                        parseItem(
                                (TreeIndexed<String>) entry.getValue(),
                                itemDefinition.getDeployDefinition(entry.getKey())
                        )
                
                      );
            // json list - set directly json in attribute value
            } else if (entry.getValue() instanceof Tree<?>) {
                item.setAttribute(entry.getKey(), entry.getValue().toJson());
            }
        }

        return item;
    }

}
