import org.bonitasoft.console.common.server.page.*

import javax.servlet.http.HttpServletRequest

public class Xml implements RestApiController {

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {

        def String xmlResponse
        pageResourceProvider.getResourceAsStream("xml/demo.xml").withStream { InputStream s ->
            xmlResponse = s.getText()
        }

        apiResponseBuilder.with {
            withResponse(xmlResponse)
            withMediaType("application/xml")
            withCharacterSet("ISO-8859-5")
            build()
        }
    }

}
