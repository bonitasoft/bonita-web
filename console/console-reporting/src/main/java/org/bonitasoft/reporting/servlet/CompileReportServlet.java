package org.bonitasoft.reporting.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.bonitasoft.reporting.utils.tools.ReportCompiler;

public class CompileReportServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CompileReportServlet.class.getName());

    @Override
    public void service(final ServletRequest req, final ServletResponse res)
            throws ServletException, IOException {

        final ReportCompiler cpr = new ReportCompiler(
                "/home/ccharly/Documents/projets/BonitaSoft/tmp/",
                "/home/ccharly/programs/iReport-5.0.1/ireport/modules/ext");
        try {
            cpr.compileReport("report1");
        } catch (final Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * suite l'upload compiler
     * +
     * exception
     * prévenir utilisateur
     * Un zip avec toutes les ressources
     * ou test unitaire
     * prérequis
     * --
     * 1 - Upload du zip
     * 2 - Décompresser ds dossier
     * ---
     * 3 - Recherche orde de compilation
     * 4 - Compiler
     */

}
