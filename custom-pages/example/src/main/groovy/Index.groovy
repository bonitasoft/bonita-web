import java.io.PrintWriter;
import java.util.Locale;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.PageController;
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.console.common.server.page.PageContext;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.session.APISession;

import com.bonitasoft.engine.api.TenantAPIAccessor;

public class Index implements PageController {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext) {
        try {

            def String indexContent;
            pageResourceProvider.getResourceAsStream("index.html").withStream { InputStream s->
                indexContent = s.getText()
            }
            PrintWriter out = response.getWriter();
            out.print(indexContent);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
