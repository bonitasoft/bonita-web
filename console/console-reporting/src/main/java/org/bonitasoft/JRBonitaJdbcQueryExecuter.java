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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.query.JRAbstractQueryExecuter;
import net.sf.jasperreports.engine.query.JRCsvQueryExecuterFactory;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bonitasoft.engine.api.ReportingAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.reporting.utils.widget.ConverterToComponents;
import org.bonitasoft.reporting.utils.widget.Parser;

/**
 * JDBC query executer for SQL queries.
 * <p/>
 * This query executer implementation offers built-in support for SQL queries.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRJdbcQueryExecuter.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class JRBonitaJdbcQueryExecuter extends JRJdbcQueryExecuter
{

    private static final Log log = LogFactory.getLog(JRBonitaJdbcQueryExecuter.class);

    private StringReader stringReader;

    public JRBonitaJdbcQueryExecuter(final JasperReportsContext jasperReportsContext,
            final JRDataset dataset,
            final Map<String, ? extends JRValueParameter> parameters) {
        super(jasperReportsContext, dataset, parameters);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void appendQueryChunk(final StringBuffer sbuffer, final JRQueryChunk chunk)
    {

        super.appendQueryChunk(sbuffer, chunk);
        appendTenantId(sbuffer);

    }

    protected void appendTenantId(final StringBuffer sbuffer) {

        // TODO add tenantId
        // TODO also transfoms behavior in dev
        // TODO attention agit aussi en dev
        log.error("appendTenantId");
        log.error(sbuffer.toString());

    }

    @Override
    public JRDataSource createDatasource() throws JRException {

        String csvResult = null;
        try {
            final ReportingAPI reportingAPI = TenantAPIAccessor.getReportingAPI((APISession) getParameterValue("BONITA_API_SESSION", true));
            String queryString = getQueryString();
            
			log.debug("Query String to build report datasource: " + queryString);
            csvResult = reportingAPI.selectList(queryString);
            
        } catch (final Exception e) {
            final String errorMessage = "Error while calling the engine";
            if (log.isErrorEnabled()) {
                log.error(errorMessage, e);
            }
            throw new JRException(errorMessage, e);
        }
        if (csvResult != null) {
            stringReader = new StringReader(csvResult);
            JRCsvDataSource csvDataSource = new JRCsvDataSource(stringReader);
            addColumnsNames(csvResult, csvDataSource);
            return csvDataSource;
        } else {
            return null;
        }
//         if (getQueryString().contains("TASK REQ1")) {
//         return new JRTableModelDataSource(new CustomDsTask1());
//         } else if (getQueryString().contains("TASK REQ2")) {
//         return new JRTableModelDataSource(new CustomDsTask2());
//         } else if (getQueryString().contains("CASE REQ2")) {
//         return new JRTableModelDataSource(new CustomDsCase());
//         }
//         return null;
    }

    private void addColumnsNames(String csvResult, JRCsvDataSource csvDataSource) {
    	if (csvResult != null) {
            final String[] rows = csvResult.split("\n")[0].split("\r");
            final String[] columnNamesRow = rows[0].split(",");
            for (int i = 0; i < columnNamesRow.length; i++) {
                csvDataSource.getColumnNames().put(columnNamesRow[i], Integer.valueOf(i));
            }
    	}
    	
    	NumberFormat numberFormat = (NumberFormat) getParameterValue(JRCsvQueryExecuterFactory.CSV_NUMBER_FORMAT, true);
		if (numberFormat != null) {
			csvDataSource.setNumberFormat(numberFormat);
		} else {
			String numberFormatPattern = getStringParameterOrProperty(JRCsvQueryExecuterFactory.CSV_NUMBER_PATTERN);
			if(numberFormatPattern != null){
				csvDataSource.setNumberPattern(numberFormatPattern);
			}
		}
		
		DateFormat dateFormat = (DateFormat) getParameterValue(JRCsvQueryExecuterFactory.CSV_DATE_FORMAT, true);
		if (dateFormat!=null) {
			csvDataSource.setDateFormat(dateFormat);
		} else {
			String dateFormatPattern = getStringParameterOrProperty(JRCsvQueryExecuterFactory.CSV_DATE_PATTERN);
			if(dateFormatPattern != null){
				csvDataSource.setDatePattern(dateFormatPattern);
			}
		}
	}

	@Override
    public synchronized void close() {
        if (stringReader != null) {
            stringReader.close();
        }
        super.close();
    }
    
    @Override
    protected String getParameterReplacement(String parameterName) {
    	return String.valueOf(getParameterValue(parameterName));
    }

    public class CustomDsTask1 extends AbstractTableModel
    {

        /**
		 *
		 */
        private final String[] columnNames = { "STATENAME", "EXPECTEDENDDATE", "NB_TASKS" };

        /**
		 *
		 */
        private final Object[][] data =
        {

                /*
                 * STATENAME;EXPECTEDENDDATE;NB_TASKS
                 * completed;0;2
                 * completed;1364312;14
                 * completed;1364313;2
                 * failed;1364312;4
                 * failed;1364313;1
                 * ready;0;3
                 * ready;1364312;1
                 * ready;1364313;6
                 * ready;1364425;1
                 */

                { "completed", new Long(0), new Long(2) },
                { "completed", new Long(1364312), new Long(14) },
                { "completed", new Long(1364313), new Long(2) },
                { "failed", new Long(1364312), new Long(4) },
                { "failed", new Long(1364313), new Long(1) },
                { "ready", new Long(0), new Long(3) },
                { "ready", new Long(1364312), new Long(1) },
                { "ready", new Long(1364313), new Long(6) },
                { "ready", new Long(1364425), new Long(1) }
        };

        /**
		 *
		 */
        public CustomDsTask1()
        {
        }

        /**
		 *
		 */
        @Override
        public int getColumnCount()
        {
            return columnNames.length;
        }

        /**
		 *
		 */
        @Override
        public String getColumnName(final int columnIndex)
        {
            return columnNames[columnIndex];
        }

        /**
		 *
		 */
        @Override
        public int getRowCount()
        {
            return data.length;
        }

        /**
		 *
		 */
        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex)
        {
            return data[rowIndex][columnIndex];
        }

    }

    public class CustomDsTask2 extends AbstractTableModel
    {

        /**
		 *
		 */
        private final String[] columnNames = { "STATENAME", "NB_TASKS" };

        /**
		 *
		 */
        private final Object[][] data =
        {

                /*
                 * STATENAME;NB_TASKS
                 * completed;18
                 * completed;2
                 * failed;5
                 * ready;11
                 */
                { "completed", new Long(18) },
                { "completed", new Long(2) },
                { "failed", new Long(5) },
                { "ready", new Long(11) }
        };

        /**
		 *
		 */
        public CustomDsTask2()
        {
        }

        /**
		 *
		 */
        @Override
        public int getColumnCount()
        {
            return columnNames.length;
        }

        /**
		 *
		 */
        @Override
        public String getColumnName(final int columnIndex)
        {
            return columnNames[columnIndex];
        }

        /**
		 *
		 */
        @Override
        public int getRowCount()
        {
            return data.length;
        }

        /**
		 *
		 */
        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex)
        {
            return data[rowIndex][columnIndex];
        }

    }

    public class CustomDsCase extends AbstractTableModel
    {

        /**
		 *
		 */
        private final String[] columnNames = { "STATENAME", "EXPECTEDENDDATE", "DURATION" };

        /**
		 *
		 */
        private final Object[][] data =
        {

                /*
                 * STATENAME;EXPECTEDENDDATE;NB_TASKS
                 * completed;0;2
                 * completed;1364312;14
                 * completed;1364313;2
                 * failed;1364312;4
                 * failed;1364313;1
                 * ready;0;3
                 * ready;1364312;1
                 * ready;1364313;6
                 * ready;1364425;1
                 */

                { "completed", new Long(1), new Double(1.0) },
                { "completed", new Long(1), new Double(2.0) },
                { "completed", new Long(1), new Double(3.0) },
                { "failed", new Long(1), new Double(4.0) },
                { "failed", new Long(5), new Double(5.0) },
                { "ready", new Long(1), new Double(7.0) },
                { "ready", new Long(1), new Double(4.0) },
                { "ready", new Long(2), new Double(8.0) },
                { "ready", new Long(2), new Double(9.0) },
                { "ready", new Long(2), new Double(11.0) }
        };

        /**
		 *
		 */
        public CustomDsCase()
        {
        }

        /**
		 *
		 */
        @Override
        public int getColumnCount()
        {
            return columnNames.length;
        }

        /**
		 *
		 */
        @Override
        public String getColumnName(final int columnIndex)
        {
            return columnNames[columnIndex];
        }

        /**
		 *
		 */
        @Override
        public int getRowCount()
        {
            return data.length;
        }

        /**
		 *
		 */
        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex)
        {
            return data[rowIndex][columnIndex];
        }

    }

}
