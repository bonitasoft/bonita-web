import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang3.StringEscapeUtils
import org.bonitasoft.console.common.server.page.PageContext
import org.bonitasoft.console.common.server.page.PageController
import org.bonitasoft.console.common.server.page.PageResourceProvider

import org.bonitasoft.engine.api.TenantAPIAccessor


public class Index implements PageController {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext) {
        try {
            def String indexContent;
            pageResourceProvider.getResourceAsStream("Index.groovy").withStream { InputStream s->
                indexContent = s.getText()
            }
            response.setCharacterEncoding("UTF-8");
            def PrintWriter out = response.getWriter()

            /*
             *
             * bonita.css can be retrieved using pageResourceProvider.getBonitaThemeCSSURL()
             * other css/js resources can be retrieved using pageResourceProvider.getResourceURL("<path in the custom page zip>")
             *
             */

            out.write("""
            <html lang="en">
            <head>
            <meta charset="utf-8">
            <link href="${pageResourceProvider.getBonitaThemeCSSURL()}" type="text/css" rel="stylesheet" />
            <link rel="stylesheet" href="jquery-ui-1.11.4/jquery-ui.min.css">
            <script src="jquery-ui-1.11.4/external/jquery/jquery.js"></script>
            <script src="jquery-ui-1.11.4/jquery-ui.min.js"></script>
            <link href="css/style.css")}" rel="stylesheet">
            <link href="css/prettify.css")}" type="text/css" rel="stylesheet" />
            <script type="text/javascript" src="js/prettify.js")}"></script>
            """)
            out.write('''
            <script>
              $(function() {
                $( "#accordion" ).accordion();
              });
              </script>
            <style type="text/css">
            pre {
                background-color: #f8f8f8;
                font-family: monospace;
            }
            .toolbar {
                margin-top: 30px;
                margin-left: 40px;
                margin-bottom: 30px
            }
            </style>
            </head>
            </head>
            <body onload="prettyPrint()">
            <div id="wrap">
            <div id="main">
            <div class="toolbar">
                <a class="back btn" href="#"
                    onclick="javascript:window.parent.history.back();return false">Back</a>
            </div>
            <div id="accordion">
              <h3>Hello world</h3>
              <div>
                    <h4>Write simple text</h4>
            ''')
            //START_EXAMPLE:Hello_WORLD
            // Write simple HTML code using the print writer of the response
            out.write("<p>Hello world!</p>");
            //END_EXAMPLE:Hello_WORLD
            out.print(extractCodeSample(indexContent, "Hello_WORLD"))
            out.write('''</div>
            <h3>Get context information</h3>
            <div><h4>Get information provided in the groovy class</h4>''')
            //START_EXAMPLE:Context_Information
            // pageContext contains most of the information on the session
            def apiSession=pageContext.getApiSession()
            out.write("""<div>
                <div> session id = ${apiSession.id}</div>
                <div> user id = ${apiSession.userId}</div>
                <div> user name = ${apiSession.userName}</div>
                <div> locale = "${pageContext.getLocale()}"</div>
                <div> profile = "${pageContext.getProfileID()}"</div></div>""")
            //END_EXAMPLE:Context_Information
            out.print(extractCodeSample(indexContent, "Context_Information"))
            out.write('''
            </div>
            <h3>Get resources of the custom page</h3>
            <div>
            <h4>Retrieve the URL of a resource of the custom page</h4>''')
            //START_EXAMPLE:Resources
            // Get external resource using the pageResourceProvider
            out.write("""<div> <img src="img/logo.png")}"/> </div>""");
            //END_EXAMPLE:Resources
            out.print(extractCodeSample(indexContent, "Resources"))

            out.write('''
            </div>
            <h3>Call API</h3>
            <div>
            <h4>Call the Bonita BPM Engine APIs using the current session</h4>''')
            //START_EXAMPLE:Call_API
            // Use the session given by pageContext to retrieve an Engine API and use it
            def session = pageContext.getApiSession()
            def identityAPI = TenantAPIAccessor.getIdentityAPI(session);
            def nbUser = identityAPI.getNumberOfUsers()
            //END_EXAMPLE:Call_API
            out.write("nbuser = "+nbUser)
            out.print(extractCodeSample(indexContent, "Call_API"))
            out.write("""
            </div>
            <h3>URLs</h3>
            <div>
            <h4>URL to an external page</h4>
            """)
            //START_EXAMPLE:Link_External
            // Link to an external page
            out.write("""<label>Link to</label>&nbsp:&nbsp;
            <a style="color: blue;" href="http://documentation.bonitasoft.com" target="_blank">An external link</a>""")
            //END_EXAMPLE:Link_External
            out.print(extractCodeSample(indexContent, "Link_External"))
            out.write("<br/>")
            out.write('<h4>Url to a portal page</h4>')
            //START_EXAMPLE:Link_Portal
            // Link to a Portal page
            // Must use the onclick attribute
            out.write("""<label>Link to</label>&nbsp:&nbsp;
            <a style="color: blue;" href="#" onclick="window.parent.location.hash='?_p=caselistingadmin&_f=archivedcases';return false">Link to archived cases</a>""")
            //END_EXAMPLE:Link_Portal
            out.print(extractCodeSample(indexContent, "Link_Portal"))
            out.write('''
            </div>
            <h3>Use localized strings</h3>
            <div>
            <h4>Get localized string from resources bundles</h4>''')
            [
                "en",
                "fr",
                "es",
                "it",
                "de",
                "ja"
            ].each{
                def locale = new Locale(it)
                ResourceBundle messages = pageResourceProvider.getResourceBundle("page",locale)
                out.write("<div> ${it}: " + messages.getString("hello")+"</div>")
            }
            out.write("<br/>")
            out.write("<h4>Get localized string using current locale</h4>")
            //START_EXAMPLE:Localized_Strings
            // Retrieve the current locale using pageContext.getLocale() and the ResourceBundle with pageResourceProvider.getResourceBundle
            def currentLocale = pageContext.getLocale()
            ResourceBundle messages = pageResourceProvider.getResourceBundle("page",pageContext.getLocale())
            out.write("""<div>${messages.getString("hello")}&nbsp;${apiSession.userName}</div>""")
            //END_EXAMPLE:Localized_Strings
            out.print(extractCodeSample(indexContent, "Localized_Strings"))
            out.write("""
            </div>
            </div>
            </div>
            </div>
            </body>
            </html>
            """)
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String extractCodeSample(String indexContent, String exampleName){
        def startLine = indexContent.split('//START_EXAMPLE:'+exampleName)
        def endLine = startLine[1].split('//END_EXAMPLE:'+exampleName)
        def  code = "<br/><pre class=\"prettyprint\">"+ StringEscapeUtils.escapeHtml4(endLine[0]) + "</pre>"
        return code
    }


}
