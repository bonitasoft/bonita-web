package org.bonitasoft.reporting.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class ImageReportServlet extends HttpServlet {

    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

    /**
	 *
	 */
    public static final String REQUEST_PARAMETER_IMAGE_NAME = "image";

    /**
	 *
	 */
    public void service(
            HttpServletRequest request,
            HttpServletResponse response
            ) throws IOException, ServletException
    {
        byte[] imageData = null;
        String imageMimeType = null;

        String imageName = request.getParameter(REQUEST_PARAMETER_IMAGE_NAME);
        if ("px".equals(imageName))
        {
            try
            {
                Renderable pxRenderer =
                        RenderableUtil.getInstance(DefaultJasperReportsContext.getInstance()).getRenderable("net/sf/jasperreports/engine/images/pixel.GIF");
                imageData = pxRenderer.getImageData(DefaultJasperReportsContext.getInstance());
                imageMimeType = ImageTypeEnum.GIF.getMimeType();
            } catch (JRException e)
            {
                throw new ServletException(e);
            }
        }
        else
        {

            JasperPrint jrPrint = (JasperPrint) request.getSession().getAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);

            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument(null, "svg", null);
            SVGGraphics2D grx = new SVGGraphics2D(document);

            try {

                JRGraphics2DExporter exporter = new JRGraphics2DExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrPrint);
                exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, grx);
                exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(0));

                exporter.exportReport();
            } catch (JRException e)
            {
                throw new ServletException(e);
            }

            response.setHeader("Content-Type", "image/svg+xml");
            // response.setContentLength(imageData.length);
            grx.stream(response.getWriter(), true);

        }

        if (imageData != null && imageData.length > 0)
        {
            if (imageMimeType != null)
            {
                response.setHeader("Content-Type", imageMimeType);
            }
            response.setContentLength(imageData.length);
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(imageData, 0, imageData.length);
            ouputStream.flush();
            ouputStream.close();
        }
    }
}
