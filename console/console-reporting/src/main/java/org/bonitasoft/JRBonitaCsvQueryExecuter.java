/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 * This program is part of JasperReports.
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRCsvQueryExecuter;
import net.sf.jasperreports.engine.query.JRCsvQueryExecuterFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bonitasoft.engine.api.ReportingAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.reporting.utils.widget.ConverterToComponents;
import org.bonitasoft.reporting.utils.widget.ConverterToReportParameters;
import org.bonitasoft.reporting.utils.widget.QueryUtils;

/**
 * JDBC query executer for SQL queries.
 * <p/>
 * This query executer implementation offers built-in support for SQL queries.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRJdbcQueryExecuter.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class JRBonitaCsvQueryExecuter extends JRCsvQueryExecuter
{

    private static final Log log = LogFactory.getLog(JRBonitaCsvQueryExecuter.class);

    private StringReader stringReader;

    public JRBonitaCsvQueryExecuter(final JasperReportsContext jasperReportsContext,
            final JRDataset dataset,
            final Map<String, ? extends JRValueParameter> parameters) {
        super(jasperReportsContext, dataset, parameters);
        parseQuery();
    }

    // @Override
    // protected void appendQueryChunk(final StringBuffer sbuffer, final JRQueryChunk chunk)
    // {
    //
    // super.appendQueryChunk(sbuffer, chunk);
    // appendTenantId(sbuffer);
    //
    // }
    //
    // protected void appendTenantId(final StringBuffer sbuffer) {
    //
    // // TODO add tenantId
    // // TODO also transfoms behavior in dev
    // // TODO attention agit aussi en dev
    // log.error("appendTenantId");
    // log.error(sbuffer.toString());
    //
    // }

    @Override
    public JRDataSource createDatasource() throws JRException {

        String csvResult = null;
        try {
            String queryString = getQueryString();

            if(log.isDebugEnabled()) {
                log.debug("Report query: " + queryString);
            }
            final ReportingAPI reportingAPI = TenantAPIAccessor.getReportingAPI((APISession) getParameterValue("BONITA_API_SESSION", true));
            csvResult = reportingAPI.selectList(queryString);
            
            if(log.isDebugEnabled()) {
                log.debug("Report query result: " + csvResult);
            }

        } catch (final Exception e) {
            final String errorMessage = "Error while calling the engine";
            if (log.isErrorEnabled()) {
                log.error(errorMessage, e);
            }
            throw new JRException(errorMessage, e);
        }
        if (csvResult != null) {
            
            stringReader = new StringReader(csvResult);
            return super.createDatasource();
        } else {
            return null;
        }
    }

    @Override
    protected Object getParameterValue(String parameterName) {
        if (JRCsvQueryExecuterFactory.CSV_READER.equals(parameterName)) {
            return stringReader;
        }
        return super.getParameterValue(parameterName);
    }

    @Override
    protected boolean getBooleanParameterOrProperty(String name,
            boolean defaultValue) {
        if (JRCsvQueryExecuterFactory.CSV_USE_FIRST_ROW_AS_HEADER.equals(name)) {
            return true;
        }

        return super.getBooleanParameterOrProperty(name, defaultValue);
    }

    @Override
    public synchronized void close() {
        if (stringReader != null) {
            stringReader.close();
        }
        super.close();
    }
}
