/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;
import net.sf.jasperreports.engine.query.JRQueryExecuter;

/**
 * Query executer factory for SQL queries.
 * <p/>
 * This factory creates JDBC query executers for SQL queries.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRJdbcQueryExecuterFactory.java 5878 2013-01-07 20:23:13Z teodord $
 * @see net.sf.jasperreports.engine.query.JRJdbcQueryExecuter
 */
public class JRBonitaJdbcQueryExecuterFactory extends JRJdbcQueryExecuterFactory
{	
	
	/**
	 * SQL query language.
	 */
	public static final String QUERY_LANGUAGE_BONITASQL = "bonitasql";
	
	public JRQueryExecuter createQueryExecuter(
			JasperReportsContext jasperReportsContext,
			JRDataset dataset, 
			Map<String,? extends JRValueParameter> parameters
			) throws JRException
		{
			return new JRBonitaCsvQueryExecuter(jasperReportsContext, dataset, parameters);
		}
}
