import java.io.PrintWriter;
import java.util.Locale;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.PageController;
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.console.common.server.page.PageContext;

public class Index implements PageController {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext) {
        try {
            def String indexContent;
            pageResourceProvider.getResourceAsStream("index.html").withStream { InputStream s->
                indexContent = s.getText()
            }
            
            def String pageResource="pageResource?&page="+ request.getParameter("page")+"&location=";
            
            def locale = pageContext.getLocale();
            if (locale != null) {
                indexContent= indexContent.replace("@_USER_LOCALE_@", locale.getLanguage());
            }
            indexContent= indexContent.replace("@_USER_LOCALE_@", pageContext.getLocale());
            indexContent= indexContent.replace("@_PAGE_RESOURCE_@", pageResource);
            
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(indexContent);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}
