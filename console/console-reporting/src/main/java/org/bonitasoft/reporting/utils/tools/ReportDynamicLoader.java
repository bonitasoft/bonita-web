package org.bonitasoft.reporting.utils.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.utils.BonitaSystem;

public class ReportDynamicLoader
{

    private static final Logger LOGGER = Logger.getLogger(ReportDynamicLoader.class.getName());

    public static ClassLoader loadMyResourceBundle(final String reportPath, final String reportName) throws BonitaReportException {

        try {
            File f = new File(reportPath + System.getProperty("file.separator") + reportName + ".properties");
            if (!f.exists()) {
                return null;
            }
            f = new File(reportPath);
            if (f.isDirectory()) {

                // get the parent classloader
                final ClassLoader parentLoader = BonitaSystem.getCurrentClassLoader();
                final ReportResourceLoader resLoader = new ReportResourceLoader(reportPath + System.getProperty("file.separator"), parentLoader);
                return resLoader;
            }
            return null;

        } catch (final Throwable t) {

            throw new BonitaReportException("could not load properties files in " + reportPath, t);
        }

    }

    /**
     * Scan the directory where libraryies are
     * 
     * @param jrLibDirectory
     *            jrLibDirectory is the folder to scan to build the dedicated
     *            classpath
     * @return the classpath used by JasperReprorts Engine (by default equals to
     *         WEB-INF/lib)
     */
    private static ArrayList<String> buildJRClasspathValue(String defaultLibrariesPath, String reportPath) {
        final ArrayList<String> classpathElements = new ArrayList<String>();
        File f = new File(defaultLibrariesPath);
        f = new File(reportPath);
        if (f.isDirectory()) {
            for (final String namefile : listJarFiles(f)) {
                LOGGER.finest("jar file [" + reportPath + System.getProperty("file.separator") + namefile + "] in JasperReports classpath");
                classpathElements.add(buildFilePath(reportPath, namefile));
            }
        }
        return classpathElements;
    }

    private static String buildFilePath(String reportPath, final String namefile) {
        return reportPath + System.getProperty("file.separator") + namefile;
    }

    private static String[] listJarFiles(File f) {
        return f.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String file) {
                return file.endsWith("jar");
            }
        });
    }

    /**
     * report classpath to string.
     * 
     * @param defaultLibrariesPath
     * @param reportPath
     * @return
     */
    public static String buildJRClasspathValueToString(
            final String defaultLibrariesPath,
            final String reportPath
            ) {

        final StringBuffer sbf = new StringBuffer();
        final ArrayList<String> classpathElements = ReportDynamicLoader.buildJRClasspathValue(
                defaultLibrariesPath,
                reportPath
                );
        for (final String classpathElement : classpathElements) {
            if (sbf.length() == 0) {
                sbf.append(classpathElement);
            } else {
                sbf.append(System.getProperty("path.separator")).append(classpathElement);
            }
        }
        return sbf.toString();
    }

    /**
     * update the classloader with new jar
     * 
     * @param defaultLibrariesPath
     * @param reportPath
     * @throws BonitaReportException
     */
    public static void updateClassLoader(String defaultLibrariesPath, String reportPath)
            throws BonitaReportException {

        final ArrayList<String> classpathElements = ReportDynamicLoader
                .buildJRClasspathValue(defaultLibrariesPath, reportPath);

        List<URL> urls = new ArrayList<URL>();
        for (int i = 0; i < classpathElements.size(); i++) {
            try {
                urls.add(createUrl(classpathElements.get(i)));
            } catch (MalformedURLException e) {
                LOGGER.severe("Malformed url for artifact <" + classpathElements.get(i) + ">. We will load the others anyway.");
            }
        }

        URLClassLoader classLoader = new URLClassLoader(asArray(urls),
                BonitaSystem.getCurrentClassLoader());
        BonitaSystem.setContextClassLoader(classLoader);
    }

    private static URL[] asArray(List<URL> urls) {
        return urls.toArray(new URL[0]);
    }

    private static URL createUrl(final String filePath) throws MalformedURLException {
        File file = new File(filePath);
        if (file.exists()) {
            return new URL(file.toURI().toString());
        }
        throw new MalformedURLException("File at <" + filePath + "> doesn't exist.");
    }
}
