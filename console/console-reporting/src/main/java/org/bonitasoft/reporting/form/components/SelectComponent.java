package org.bonitasoft.reporting.form.components;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.reporting.utils.BonitaSystem;

public class SelectComponent implements IComponent {

    public final static String BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME = "All";

    public final static String BONITA_JR_SELECT_COMPONENT_ALL_ELEM_VALUE = "ALL_VALUE";

    private String name;

    private String label;

    private String type;

    private String id;

    private HashMap<String, String> values;

    private HashMap<String, String> defaultValues;

    private HashMap<String, String> jsLibraries;

    private BonitaSystem bonitaSytem;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public HashMap<String, String> getvalues() {
        return values;
    }

    @Override
    public HashMap<String, String> getDefaultValues() {
        return defaultValues;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setId(final String id) {
        this.id = id;

    }

    @Override
    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public void setvalues(final HashMap<String, String> values) {
        this.values = values;
    }

    @Override
    public void setDefaultValues(final HashMap<String, String> defaultValues) {
        this.defaultValues = defaultValues;
    }

    @Override
    public String getCssCode() {
        final StringBuffer sb = new StringBuffer();
        sb.append("#").append(getId()).append("{").append("\n");
        sb.append("}").append("\n");
        return sb.toString();
    }

    @Override
    public String getJsCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<String, String> getJsLibraries() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<String, String> getCssLibraries() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getBodyHtml() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<div class='formentry formentry_priority priority select' id='form-report-ct-slc-").append(getId()).append("'>").append("\n");
        sb.append("	<div class='label'  id='form-report-lbl-slc-").append(getId()).append("'>").append("\n");
        sb.append("		<label for='").append(getId()).append("'>").append(getLabel()).append("</label>").append("\n");
        sb.append("	</div>").append("\n");
        sb.append("	<div class='input'  id='form-report-ipt-slc-").append(getId()).append("'>").append("\n");
        sb.append("		<select id='").append(getId()).append("' name='").append(getId()).append("' class='' onchange='this.form.submit();'>").append("\n");

        final Map<String, String> sortedMap = sortByComparator(values);
        final Iterator<String> keys = sortedMap.keySet().iterator();

        // add "All" member at the begining
        if (getDefaultValues() != null
                && getDefaultValues().get(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME) != null) {

            if (getDefaultValues() != null
                    && getDefaultValues().get("default") != null
                    && getDefaultValues().get(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_VALUE) != null
                    && getDefaultValues().get("default").equals(getDefaultValues().get(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_VALUE))) {

                sb.append("		<option value='").append(getDefaultValues().get(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_VALUE)).append("' selected >")
                        .append(getDefaultValues().get(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME)).append("</option>").append("\n");
            } else {
                sb.append("		<option value='").append(getDefaultValues().get(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_VALUE)).append("'>")
                        .append(getDefaultValues().get(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME)).append("</option>").append("\n");
            }
        }
        while (keys.hasNext()) {
            final String key = keys.next();
            if (getDefaultValues() != null && getDefaultValues().get("default") != null && getDefaultValues().get("default").equals(key)) {
                sb.append("		<option value='").append(key).append("' selected >").append(getvalues().get(key)).append("</option>").append("\n");
            } else {
                sb.append("		<option value='").append(key).append("'>").append(getvalues().get(key)).append("</option>").append("\n");
            }
        }

        sb.append("		</select>").append("\n");
        sb.append("	</div>").append("\n");
        sb.append("</div>").append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("name:").append(name).append(", ");
        sb.append("label:").append(label).append(", ");
        sb.append("type:").append(type).append(", ");
        sb.append("id:").append(id).append(", ");
        if (values != null) {
            sb.append("values: {").append(values.toString()).append("}, ");
        } else {
            sb.append("values: {nil}, ");
        }
        if (defaultValues != null) {
            sb.append("defaultValues: {").append(defaultValues.toString()).append("}, ");
        }
        else {
            sb.append("defaultValues: {nil}, ");
        }
        if (jsLibraries != null) {
            sb.append("jsLibraries: {").append(jsLibraries.toString()).append("}, ");
        }
        else {
            sb.append("jsLibraries: {nil}, ");
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private Map sortByComparator(final Map unsortMap) {

        final List list = new LinkedList(unsortMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {

            @Override
            public int compare(final Object o1, final Object o2) {
                return ((Comparable) ((Map.Entry) o1).getValue())
                        .compareTo(((Map.Entry) o2).getValue());
            }
        });

        // put sorted list into map again
        // LinkedHashMap make sure order in which keys were inserted
        final Map sortedMap = new LinkedHashMap();
        for (final Iterator it = list.iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * system properties
     */
    @Override
    public BonitaSystem getBonitaSystem() {
        return bonitaSytem;
    }

    public void setBonitaSystem(final BonitaSystem sys) {
        bonitaSytem = sys;
    }

}
