package org.bonitasoft.reporting.utils;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.reporting.exception.BonitaReportException;

public class BonitaSystem {

	private static final String DEFAULT_LOCALE = "en";

	private BonitaReportEngineContext bonitaReportCtx;

	private APISession apiSession;

	public String getCurrentUser() {
		String username = null;
		if (apiSession != null) {
			username = apiSession.getUserName();
		}
		return username;
	}

	public long getCurrentTenant() {

		long tenant = -1;
		if (apiSession != null) {
			tenant = apiSession.getTenantId();
		}
		return tenant;
	}

	public static String getBaseUrl(final HttpServletRequest request) {

		return "http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath();
	}

	public static String getLibSystemPath(final ServletContext context) {
		return context.getRealPath("/") + context.getServletContextName()
				+ "/WEB-INF/lib";
	}

	public static Locale getCurrentLocale(final HttpServletRequest request) {
		String locale = request.getParameter("locale");
		if(locale == null || "null".equals(locale)) {
			return new Locale(DEFAULT_LOCALE);
		}
		return new Locale(locale);
	}

	public String getCurrentLocaleDateFormat() throws BonitaReportException {
		return _("MM/dd/yyyy");
	}

	public String getCurrentLocaleTimeFormat() throws BonitaReportException {
		return getBonitaReportCtx().getTimeFormat();
	}

	public String getCurrentLocaleDatetimeFormat() throws BonitaReportException {
		return getCurrentLocaleDateFormat() + " "
				+ getCurrentLocaleTimeFormat();
	}

	/**
	 * utility for date in javascript
	 * 
	 * @return
	 */
	public String getCurrentLocaleJsDateFormat() throws BonitaReportException {
		return _("mm/dd/yy");
	}

	public TimeZone getCurrentTimeZone() throws BonitaReportException {
		final String timeZone = getBonitaReportCtx().getTimeZoneId();
		return TimeZone.getTimeZone(timeZone);
	}

	public static ClassLoader getCurrentClassLoader() {
		// TODO more better class loader
		// perhaps it will be interessting to have a classe loadder by
		// session. In
		return Thread.currentThread().getContextClassLoader();
	}

	public static void setContextClassLoader(final ClassLoader cl) {
		Thread.currentThread().setContextClassLoader(cl);
	}

	public BonitaReportEngineContext getBonitaReportCtx() {
		return bonitaReportCtx;
	}

	public void setBonitaReportCtx(
			final BonitaReportEngineContext bonitaReportCtx) {
		this.bonitaReportCtx = bonitaReportCtx;
	}

	public APISession getAPISession() {
		return apiSession;
	}

	public void setAPISession(final APISession apiSession) {
		this.apiSession = apiSession;
	}

}
