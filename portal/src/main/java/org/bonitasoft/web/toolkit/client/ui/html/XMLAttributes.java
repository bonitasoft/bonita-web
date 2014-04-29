package org.bonitasoft.web.toolkit.client.ui.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class XMLAttributes {

    HashMap<String, String> attributes = new HashMap<String, String>();

    ArrayList<String> classes = new ArrayList<String>();

    public XMLAttributes() {
    }

    public XMLAttributes(final String name, final String value) {
        this();
        this.add(name, value);
    }

    public XMLAttributes(final String attributes) {
        this.attributes = attributesStringToArray(attributes);
    }

    public XMLAttributes(final HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public XMLAttributes add(final String name, final String value) {

        return this.add(name, value, true);
    }

    public XMLAttributes add(final String name, final String value, final boolean force) {
        if (value == null || !force && this.hasAttribute(name)) {
            return this;
        }

        final String realName = name.toLowerCase();
        if ("class".equalsIgnoreCase(realName)) {
            addClass(value);
        } else {
            this.attributes.put(realName, value);
        }
        return this;
    }

    public XMLAttributes remove(final String name) {
        final String realName = name.toLowerCase();
        if (this.hasAttribute(realName)) {
            this.attributes.remove(realName);
        }

        return this;
    }

    public XMLAttributes remove(final String name, final String value) {
        final String realName = name.toLowerCase();
        if (this.hasAttribute(realName, value)) {
            this.attributes.remove(realName);
        }

        return this;
    }

    public String getAttribute(final String name) {
        return this.attributes.get(name.toLowerCase());
    }

    public boolean hasAttribute(final String name) {
        return this.attributes.containsKey(name.toLowerCase());
    }

    public boolean hasAttribute(final String name, final String value) {
        final String realName = name.toLowerCase();
        return this.attributes.containsKey(realName) && this.attributes.get(realName) == value;
    }

    public boolean hasClass(final String className) {
        final String[] classNames = className.split(" ");
        for (int i = 0; i < classNames.length; i++) {
            if (!this.classes.contains(classNames[i])) {
                return false;
            }
        }

        return true;
    }

    public XMLAttributes setClass(final String className) {
        this.classes.clear();
        return addClass(className);
    }

    public XMLAttributes addClass(final String className) {
        final String[] classNames = className.split(" ");
        this.classes.addAll(Arrays.asList(classNames));
        return this;
    }

    public XMLAttributes removeClass(final String className) {
        final String[] classNames = className.split(" ");
        this.classes.removeAll(Arrays.asList(classNames));
        return this;
    }

    private HashMap<String, String> attributesStringToArray(final String attributes) {
        final String[] result = attributes.split(" ");

        // Rebuild quoted strings
        boolean prev_is_quoted = false;
        int prev_index = -1;

        for (int k = 0; k < result.length; k++) {
            final String v = result[k];
            final int quotePos = v.indexOf('"');
            if (prev_is_quoted) {
                result[prev_index] += " " + v;
                result[k] = null;
                if (quotePos >= 0) {
                    prev_index = -1;
                    prev_is_quoted = false;
                }
            } else if (quotePos >= 0) {
                prev_is_quoted = true;
                prev_index = k;
                if (v.indexOf('"', quotePos) >= 0) {
                    prev_is_quoted = false;
                }
            }
        }

        final ArrayList<String> result2 = new ArrayList<String>();
        for (int k = 0; k < result.length; k++) {
            if (result[k] != null) {
                result2.add(result[k]);
            }
        }

        // Cut The content in a hashmap
        final HashMap<String, String> result3 = new HashMap<String, String>();

        for (final String v : result2) {
            final String[] currentAttribute = v.split("=");
            if (currentAttribute.length == 1) {
                result3.put(currentAttribute[0], currentAttribute[0]);
            } else {
                final StringBuilder value = new StringBuilder();
                for (int i = 1; i < currentAttribute.length; i++) {
                    value.append(i == 1 ? "" : "=").append(currentAttribute[i]);
                }

                result3.put(
                        currentAttribute[0].trim(),
                        value.toString().replaceAll("^\"(.*)\"$", "$1") // Trim quotes
                );
            }
        }
        return result3;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        // Add classes
        if (this.classes.size() > 0) {
            final StringBuilder classes = new StringBuilder();
            for (final String className : this.classes) {
                classes.append(className).append(" ");
            }
            result.append("class=\"").append(classes.toString().trim()).append("\" ");
        }

        // Add other attributes
        for (final Entry<String, String> entry : this.attributes.entrySet()) {
            result.append(entry.getKey()).append("=\"").append(this.attributes.get(entry.getKey()).replaceAll("\"", "&quot;")).append("\" ");
        }

        // Remove trailing space
        return result.toString().trim();
    }

}
