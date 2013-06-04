package org.bonitasoft.reporting.utils.tools;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.servlet.CompileReportServlet;

public class ReportCompiler {

    private static final Logger LOGGER = Logger
            .getLogger(CompileReportServlet.class.getName());

    // folder where the report to compile and its resources are
    private final String reportPath;

    // folder where the copiled report must be
    private final String compiledReportPath;

    // default libray path
    private final String defaultLibrariesPath;

    private final ArrayList<String> completedSubReports = new ArrayList<String>(30);

    private Throwable subReportException = null;

    // Constructor
    public ReportCompiler(final String reportPath, final String defaultLibraryPath) {

        this(reportPath, reportPath, defaultLibraryPath);

    }

    public ReportCompiler(final String reportPath, final String compiledReportPath,
            final String defaultLibraryPath) {

        super();

        this.reportPath = reportPath;
        this.compiledReportPath = compiledReportPath;
        defaultLibrariesPath = defaultLibraryPath;

    }

    // getters and setters

    /**
     * Recursively compile report and subreports
     */
    public JasperReport compileReport(final String reportName) throws BonitaReportException {

        final String reportFilePath = reportPath + System.getProperty("file.separator") + reportName + ".jrxml";
        final String copiledReportFilePath = reportPath + System.getProperty("file.separator") + reportName + ".jasper";

        LOGGER.finest("Compiling : " + reportFilePath);

        // JRPropertiesUtil ju = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance());
        // ju.setProperty(JRCompiler.COMPILER_CLASSPATH, this.buildJRClasspathValueToString());

        JasperDesign jasperDesign;
        try {
            jasperDesign = JRXmlLoader.load(reportFilePath);
        } catch (final JRException e) {
            throw new BonitaReportException("Could not load the jrxml file: " + reportFilePath, e);
        }

        JasperReport jasperReport;
        try {
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
        } catch (final JRException e) {
            throw new BonitaReportException("Could not compile the jrxml file: " + reportFilePath, e);
        }

        try {
            JRSaver.saveObject(jasperReport, copiledReportFilePath);
        } catch (final JRException e) {
            throw new BonitaReportException("Could not saved the jasper file: " + copiledReportFilePath, e);
        }

        LOGGER.finest("Saving compiled report to: " + System.getProperty("file.separator") + compiledReportPath
                + reportName + ".jasper");

        // Compile sub reports
        JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {

            @Override
            public void visitBreak(final JRBreak breakElement) {
            }

            @Override
            public void visitChart(final JRChart chart) {
            }

            @Override
            public void visitCrosstab(final JRCrosstab crosstab) {
            }

            @Override
            public void visitElementGroup(final JRElementGroup elementGroup) {
            }

            @Override
            public void visitEllipse(final JREllipse ellipse) {
            }

            @Override
            public void visitFrame(final JRFrame frame) {
            }

            @Override
            public void visitImage(final JRImage image) {
            }

            @Override
            public void visitLine(final JRLine line) {
            }

            @Override
            public void visitRectangle(final JRRectangle rectangle) {
            }

            @Override
            public void visitStaticText(final JRStaticText staticText) {
            }

            @Override
            public void visitSubreport(final JRSubreport subreport) {
                try {

                    // retrive subreport inside the jasperReports template
                    final String expression = subreport.getExpression().getText()
                            .replace(".jasper", "");
                    final StringTokenizer st = new StringTokenizer(expression, "\"/");
                    String subReportName = null;

                    // TODO can be done with other method
                    while (st.hasMoreTokens()) {
                        subReportName = st.nextToken();
                    }

                    // Sometimes the same subreport can be used multiple times,
                    // but
                    // there is no need to compile multiple times
                    if (completedSubReports.contains(subReportName)) {
                        return;
                    }
                    completedSubReports.add(subReportName);
                    LOGGER.finest("go to compile subreport : " + subReportName);
                    compileReport(subReportName);
                } catch (final Throwable e) {
                    subReportException = e;
                }
            }

            @Override
            public void visitTextField(final JRTextField textField) {
            }

            @Override
            public void visitComponentElement(
                    final JRComponentElement componentElement) {
            }

            @Override
            public void visitGenericElement(final JRGenericElement element) {
            }
        });

        if (subReportException != null) {
            throw new BonitaReportException("An error occures during the subreport compilation", subReportException);
        }

        return jasperReport;
    }

}
