package org.bonitasoft.reporting.utils.widget;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bonitasoft.engine.api.ReportingAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.form.components.DateComponent;
import org.bonitasoft.reporting.form.components.DateRangeComponent;
import org.bonitasoft.reporting.form.components.IComponent;
import org.bonitasoft.reporting.form.components.SelectComponent;
import org.bonitasoft.reporting.utils.BonitaSystem;
import org.bonitasoft.reporting.utils.connection.BonitaConnection;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverterToComponents {

    private static final Log log = LogFactory.getLog(ConverterToComponents.class);

    public static String SQL_PARAM_TENANT = "$P{BONITA_TENANT_ID}";

    public static IComponent getComponent(
            final HttpServletRequest request,
            final BonitaSystem bonitaSystem,
            final Widget widget, final Connection conn, final ResourceBundle messages)
            throws BonitaReportException
    {

        IComponent component = null;

        final SimpleDateFormat sdf = new SimpleDateFormat(
                bonitaSystem.getCurrentLocaleDateFormat(),
                BonitaSystem.getCurrentLocale(request));

        if (Parser.BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_SELECT.equals(widget.getWidget())) {

            component = new SelectComponent();
            component.setBonitaSystem(bonitaSystem);

            if (widget.getQuery() != null) {

                if (conn instanceof BonitaConnection) {
                    executeBonitaQuery(bonitaSystem, widget, messages, component);
                } else {
                    executeQuery(bonitaSystem, widget, conn, messages, component);
                }

            } else if (widget.getAvailableValues() != null) {

                try {

                    final HashMap<String, String> values = new HashMap<String, String>();
                    final HashMap<String, String> defaultValues = new HashMap<String, String>();

                    final JsonFactory f = new JsonFactory();
                    final JsonParser jp = f.createJsonParser(widget.getAvailableValues());
                    jp.nextToken();
                    ObjectMapper objectMapper = new ObjectMapper();
                    int i = 0;
                    while (jp.nextToken() == JsonToken.START_OBJECT) {
                        final Value val = objectMapper.readValue(jp, Value.class);
                        values.put(val.getId(), ConverterToComponents.translate(val.getLabel(), messages));

                        // the default value is the first value
                        // depend of if the a member "All" is defined
                        if (i == 0 && Parser.BONITA_JR_PROPERTY_WIDGET_HAS_ALL_FALSE.equals(widget.getHasAll())) {
                            defaultValues.put("default", val.getId());
                        }
                        i++;
                    }
                    /*
                     * //ObjectMapper objectMapper = new ObjectMapper();
                     * ObjectMapper objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
                     * List<Value> availableValues = null;
                     * availableValues = objectMapper.readValue(widget.getAvailableValues(), new TypeReference<List<Value>>() { });
                     * HashMap<String, String> values = new HashMap<String, String>();
                     * HashMap<String, String> defaultValues = new HashMap<String, String>();
                     * for(int i=0; i < availableValues.size(); i++){
                     * Value v = availableValues.get(i);
                     * values.put(v.getId(), v.getLabel());
                     * // the default value is the first value
                     * if(i == 0){
                     * defaultValues.put("default",v.getId());
                     * }
                     * }
                     */

                    setInitialValue(widget, defaultValues);

                    // check if the component has a "has all element"
                    if (Parser.BONITA_JR_PROPERTY_WIDGET_HAS_ALL_TRUE.equals(widget.getHasAll())) {

                        defaultValues.put(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME,
                                ConverterToComponents.translate(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME, messages));
                        defaultValues.put(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_VALUE, widget.getHasAllValue());

                        if (widget.getInitialValue() == null) {
                            // force initial value
                            widget.setInitialValue(widget.getHasAllValue());
                        }

                    }

                    // values
                    component.setvalues(values);
                    // default values
                    component.setDefaultValues(defaultValues);

                    objectMapper = null;

                } catch (final JsonParseException e) {
                    throw new BonitaReportException("An error occurs during the conversion of json available values of the widget " + widget.getId(), e);
                } catch (final IOException e) {
                    throw new BonitaReportException("An error occurs during the conversion of json available values of the widget " + widget.getId(), e);
                }

            }

        } else if (Parser.BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_DATE.equals(widget.getWidget())) {
            component = new DateComponent();
            component.setBonitaSystem(bonitaSystem);

            final HashMap<String, String> defaultValues = new HashMap<String, String>();

            // if initial value is null set default value to today
            if (widget.getInitialValue() == null) {

                defaultValues.put("default", sdf.format(new Date()));

                // force initial value
                widget.setInitialValue(sdf.format(new Date()));

            } else {
                defaultValues.put("default", widget.getInitialValue());
            }

            // default values
            component.setDefaultValues(defaultValues);

        } else if (Parser.BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_DATE_RANGE.equals(widget.getWidget())) {
            component = new DateRangeComponent();
            component.setBonitaSystem(bonitaSystem);

            final HashMap<String, String> defaultValues = new HashMap<String, String>();

            // if initial value is null set default value to today
            if (widget.getInitialValue() == null) {

                defaultValues.put("from", sdf.format(new Date()));
                defaultValues.put("to", sdf.format(new Date()));

                // force initial value
                widget.setInitialValue(sdf.format(new Date()));

            } else {
                final String[] dts = widget.getInitialValue().split(" - ");
                if (dts != null && dts.length > 0) {
                    if (dts.length > 0 && dts[0] != null) {
                        defaultValues.put("from", dts[0]);
                    }
                    if (dts.length > 1 && dts[1] != null) {
                        defaultValues.put("to", dts[1]);
                    }
                }
            }

            // default values
            component.setDefaultValues(defaultValues);

            ((DateRangeComponent) component).setLabelFrom(
                    ConverterToComponents.translate(DateRangeComponent.DATE_RANGE_LABEL_FROM, messages));
            ((DateRangeComponent) component).setLabelTo(
                    ConverterToComponents.translate(DateRangeComponent.DATE_RANGE_LABEL_TO, messages));
        }

        // id
        component.setId(widget.getId());
        // name
        component.setName(widget.getId());
        // label
        // try to translate
        component.setLabel(ConverterToComponents.translate(widget.getLabel(), messages));
        // type
        component.setType(widget.getWidget());

        return component;

    }

    private static void executeBonitaQuery(final BonitaSystem bonitaSystem, final Widget widget, final ResourceBundle messages, final IComponent component)
            throws BonitaReportException {
        String csvResult = null;
        final String query = widget.getQuery().replace(ConverterToComponents.SQL_PARAM_TENANT, Long.toString(bonitaSystem.getCurrentTenant()));
        try {
            final ReportingAPI reportingAPI = TenantAPIAccessor.getReportingAPI(bonitaSystem.getAPISession());
            if (log.isDebugEnabled()) {
                log.debug("## Reporting - Executing SQL query : " + query);
            }
            csvResult = reportingAPI.selectList(query);
            if (log.isDebugEnabled()) {
                log.debug("## Reporting - Query result : " + csvResult);
            }
        } catch (final Exception e) {
            final String errorMessage = "Error while calling the engine";
            if (log.isErrorEnabled()) {
                log.error(errorMessage, e);
            }
            throw new BonitaReportException(errorMessage, e);
        }

        HashMap<String, String> values = new HashMap<String, String>();
        final HashMap<String, String> defaultValues = new HashMap<String, String>();
        if (csvResult != null) {
            values = extractInitialValuesFromCsv(widget, messages, csvResult, defaultValues);

            setInitialValue(widget, defaultValues);
            addAllElement(widget, messages, defaultValues);
        }
        // values
        component.setvalues(values);
        // default values
        component.setDefaultValues(defaultValues);
    }

    /**
     * @param widget
     * @param messages
     * @param csvResult
     * @param values
     * @return
     */
    private static HashMap<String, String> extractInitialValuesFromCsv(final Widget widget, final ResourceBundle messages, final String csvResult,
            final HashMap<String, String> defaultValues) {
        final HashMap<String, String> initialValues = new HashMap<String, String>();

        final int idColumnIndex = getColumnIndexOf("id", csvResult);
        final int labelColumnIndex = getColumnIndexOf("label", csvResult);

        int lineNumber = 1;
        final String[] rows = getLines(csvResult);
        while (lineNumber < rows.length) {
            final String[] lineValues = getLineValues(rows[lineNumber]);
            initialValues.put(
                    lineValues[idColumnIndex],
                    ConverterToComponents.translate(lineValues[labelColumnIndex], messages));
            // the default value is the first value
            // depend of if the a member "All" is defined
            if (lineNumber == 1 && Parser.BONITA_JR_PROPERTY_WIDGET_HAS_ALL_FALSE.equals(widget.getHasAll())) {
                defaultValues.put("default", lineValues[idColumnIndex]);
            }
            lineNumber++;
        }
        return initialValues;
    }

    /**
     * @param csvResult
     * @return
     */
    private static String[] getLines(final String csv) {
        return csv.split("\r?\n|\r");
    }

    /**
     * @precondition csv isn't null and contains header in first row and column exist in Csv
     * @param columnName
     * @param csv
     * @return
     */
    private static int getColumnIndexOf(final String columnName, final String csv) {
        final String header = getLine(csv, 0);
        final String[] columnNames = getLineValues(header);
        int i = 0;
        while (i < columnNames.length && !columnName.equalsIgnoreCase(columnNames[i])) {
            i++;
        }
        if (i < columnNames.length) {
            return i;
        } else {
            return -1;
        }
    }

    /**
     * @param header
     * @return
     */
    private static String[] getLineValues(final String header) {
        return header.split(",");
    }

    /**
     * @param csv
     * @param lineNumber
     * @return
     */
    private static String getLine(final String csv, final int lineNumber) {
        final String[] rows = getLines(csv);
        if (lineNumber < rows.length) {
            return rows[lineNumber];
        } else {
            return null;
        }
    }

    private static void executeQuery(final BonitaSystem bonitaSystem, final Widget widget, final Connection conn, final ResourceBundle messages,
            final IComponent component) throws BonitaReportException {

        Statement statement = null;
        ResultSet result = null;
        try {

            statement = conn.createStatement();

            // TODO execute sql by JRQueryExcuter
            final String sqlStatement = widget.getQuery().replace(ConverterToComponents.SQL_PARAM_TENANT,
                    Long.toString(bonitaSystem.getCurrentTenant()));
            result = statement.executeQuery(sqlStatement);

            final HashMap<String, String> values = new HashMap<String, String>();
            final HashMap<String, String> defaultValues = new HashMap<String, String>();

            int i = 0;
            while (result.next()) {

                // translation
                final String label = result.getString("label");
                values.put(
                        result.getString("id"),
                        ConverterToComponents.translate(label, messages));

                // the default value is the first value
                // depend of if the a member "All" is defined
                if (i == 0 && Parser.BONITA_JR_PROPERTY_WIDGET_HAS_ALL_FALSE.equals(widget.getHasAll())) {

                    defaultValues.put("default", result.getString("id"));
                }
                i++;
            }

            setInitialValue(widget, defaultValues);
            addAllElement(widget, messages, defaultValues);

            // values
            component.setvalues(values);
            // default values
            component.setDefaultValues(defaultValues);

        } catch (final SQLException e) {

            throw new BonitaReportException("An error occurs during the execution of the sql query of the widget " + widget.getId(), e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (final SQLException e) {
                    throw new BonitaReportException("An error occurs during the execution of the sql query of the widget " + widget.getId(), e);
                }
                result = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (final SQLException e) {
                    throw new BonitaReportException("An error occurs during the execution of the sql query of the widget " + widget.getId(), e);
                }
                statement = null;
            }
        }
    }

    private static void addAllElement(final Widget widget, final ResourceBundle messages, final HashMap<String, String> defaultValues) {
        // check if the component has a "has all element"
        if (Parser.BONITA_JR_PROPERTY_WIDGET_HAS_ALL_TRUE.equals(widget.getHasAll())) {

            defaultValues.put(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME,
                    ConverterToComponents.translate(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_NAME, messages));
            defaultValues.put(SelectComponent.BONITA_JR_SELECT_COMPONENT_ALL_ELEM_VALUE, widget.getHasAllValue());
        }
    }

    private static void setInitialValue(final Widget widget, HashMap<String, String> defaultValues) {
        // override the default value
        if (widget.getInitialValue() != null) {
            defaultValues = new HashMap<String, String>();

            defaultValues.put(
                    "default",
                    widget.getInitialValue());
        }
    }

    public static ArrayList<IComponent> getComponents(
            final HttpServletRequest request,
            final BonitaSystem bonitaSystem,
            final ArrayList<Widget> widgets,
            final Connection conn, final ResourceBundle messages)
            throws BonitaReportException
    {

        final ArrayList<IComponent> components = new ArrayList<IComponent>();

        for (int i = 0; i < widgets.size(); i++) {
            components.add(ConverterToComponents.getComponent(
                    request,
                    bonitaSystem,
                    widgets.get(i), conn, messages));
        }

        return components;

    }

    private static String translate(final String label, final ResourceBundle messages) {

        // if the no ResourceBundle found
        // no translation
        if (messages == null) {
            return label;
        }

        String labelLocale = label;
        try {

            labelLocale = messages.getString(label);

            // the entry exists in the resources file but has no translation
            if (labelLocale == null || labelLocale.length() <= 0) {
                labelLocale = label;
            }

        } catch (final MissingResourceException e) {
            // if no translation available
            // return the not translated label
            // TODO log warning : no translation found for label
            labelLocale = label;
        }

        return labelLocale;
    }

}
