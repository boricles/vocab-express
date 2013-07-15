package ld.vocab_express.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/Home.htm")
public class HomeActionBean extends BaseActionBean {

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution("/WEB-INF/jsp/home.jsp");
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public String getOsName() {
        return System.getProperty("os.name");
    }
}
