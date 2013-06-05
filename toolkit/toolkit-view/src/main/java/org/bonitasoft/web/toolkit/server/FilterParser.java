package org.bonitasoft.web.toolkit.server;

import java.util.HashMap;
import java.util.List;

public class FilterParser {

    public static HashMap<String, String> parse(final String filter) {
        return parse(null, filter);
    }

    public static HashMap<String, String> parse(final List<String> filters) {
        final HashMap<String, String> result = new HashMap<String, String>();

        if (filters != null) {
            for (final String filter : filters) {
                parse(result, filter);
            }
        }

        return result;
    }

    private static HashMap<String, String> parse(HashMap<String, String> result, final String filter) {
        if (result == null) {
            result = new HashMap<String, String>();
        }

        final String[] split = filter.split("=");

        if (split.length > 0) {
            final String name = split[0];
            final String value = filter.replaceAll("^" + name + "=", "");
            result.put(name, value);
        }

        return result;
    }

}
