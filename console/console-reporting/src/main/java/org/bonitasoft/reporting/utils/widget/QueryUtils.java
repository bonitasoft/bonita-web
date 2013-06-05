package org.bonitasoft.reporting.utils.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryUtils {

    public static String getAliasValue(List<String> selectDirectives, String alias) {
        for (String sel : selectDirectives) {
            String[] field = sel.toLowerCase().split("as");
            if (field.length > 1 && alias.equals(field[1].trim())) {
                return field[0].trim();
            }
        }

        throw new RuntimeException("Couldn't find value for alias <" + alias + ">");
    }

    public static String discoverAlias(List<String> selectDirectives, String value) {
        for (String sel : selectDirectives) {
            String[] field = sel.toLowerCase().split("as");

            if (field.length > 1) {
                if (value.toLowerCase().equals(field[0].split("cs.")[1].trim()) || value.toLowerCase().equals(field[0].split("aps.")[1].trim())
                        || value.toLowerCase().equals(field[0].split("usr.")[1])) {
                    return field[1].trim();
                } else if (field[0].contains("(")
                        && (value.toLowerCase().equals("cs_start") || value.toLowerCase().equals("cs_end") || value.toLowerCase().equals("cs_duration"))) {
                    return value.trim();
                }
            } 
        }

        throw new RuntimeException("Couldn't find any alias for <" + value + ">");
    }

    public static List<String> getSelectDirectives(final String query) {
        String select = query.substring(query.toLowerCase().indexOf("select") + 6,
                query.toLowerCase().indexOf("from"));
        return trimList(splitSelectDirectives(select));
    }

    private static List<String> trimList(List<String> list) {
        List<String> trimedList = new ArrayList<String>();
        for (String string : list) {
            trimedList.add(string.trim());
        }
        return trimedList;
    }

    private static List<String> splitSelectDirectives(String select) {
        return Arrays.asList(select.trim().split(","));
    }

}
