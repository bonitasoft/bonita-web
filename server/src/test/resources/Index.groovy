import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.PageController;
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.console.common.server.page.PageContext;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.session.APISession;

import org.bonitasoft.engine.api.TenantAPIAccessor;

public class Index implements PageController {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext) {
        try {
            HttpSession session = request.getSession();
            IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(pageContext.getApiSession());
            User user = identityAPI.getUserByUserName((String)session.getAttribute("username"));
            PrintWriter out = response.getWriter();
            out.write("<div class=\"user\">");
            out.write("<span>User details returned from Engine API Call using the existing API Session:</span>");
            out.write(user.toString());
            out.write("</div>");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
