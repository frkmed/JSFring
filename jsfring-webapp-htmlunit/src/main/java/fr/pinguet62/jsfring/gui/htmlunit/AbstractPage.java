package fr.pinguet62.jsfring.gui.htmlunit;

import static java.io.File.createTempFile;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.joining;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.ui.context.Theme;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import fr.pinguet62.jsfring.gui.htmlunit.filter.FilterPathPage;
import fr.pinguet62.jsfring.gui.htmlunit.jasperreport.ParametersJasperReportPage;
import fr.pinguet62.jsfring.gui.htmlunit.jasperreport.UsersRightsJasperReportPage;
import fr.pinguet62.jsfring.gui.htmlunit.profile.ProfilesPage;
import fr.pinguet62.jsfring.gui.htmlunit.right.RightsPage;
import fr.pinguet62.jsfring.gui.htmlunit.user.UsersPage;

public class AbstractPage {

    public static enum Delay {

        /** To use for long actions, when there are server treatments. */
        LONG(7_000),
        /** To use for short server treatment. Example: database reading. */
        MEDIUM(4_000),
        /** To use for simple actions, without server treatments. */
        SHORT(2_000);

        private final long ms;

        private Delay(long ms) {
            this.ms = ms;
        }

        public long getMs() {
            return ms;
        }
    }

    private static final Logger LOGGER = getLogger(AbstractPage.class);

    private static final File TMP_FILE;

    /** Initialize the {@link #OUTPUT_STREAM}. */
    static {
        try {
            TMP_FILE = createTempFile("navigator-", null);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
        LOGGER.debug("Temporary file: {}", TMP_FILE);
    }

    public static void debug(HtmlInput html) {
        debug(html.getHtmlPageOrNull().asXml());
    }

    public static void debug(SgmlPage page) {
        debug(page.asXml());
    }

    public static void debug(String xml) {
        if (!LOGGER.isDebugEnabled())
            return;

        try {
            IOUtils.write(xml, new FileOutputStream(TMP_FILE));
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public static AbstractPage get() {
        return new AbstractPage(null);
    }

    private static String getUrl(String subUrl) {
        if (subUrl == null)
            subUrl = "";
        return "http://localhost:8080/" + subUrl + "?lang=en";
    }

    protected HtmlPage page;

    protected final WebClient webClient = new WebClient();

    {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
    }

    /**
     * Constructor used by classes that inherit.
     *
     * @param page The {@link HtmlPage HTML page} once on the target page.
     */
    protected AbstractPage(HtmlPage page) {
        this.page = page;
        checkI18n();
    }

    /**
     * Check that there is no missing i18n messages into current page.<br>
     * Check only the current language.<br>
     * A missing i18n message is formatted as {@code "???key???"}.
     *
     * @throws NavigatorException Missing i18n message.
     */
    private void checkI18n() {
        if (page == null)
            return;
        Collection<String> libelles = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\?\\?\\?[^\\?]+\\?\\?\\?");
        Matcher matcher = pattern.matcher(page.asXml());
        while (matcher.find())
            libelles.add(matcher.group());
        if (!libelles.isEmpty())
            throw new NavigatorException("Missing i18n message: " + libelles.stream().collect(joining(", ")));
    }

    /**
     * To call after each page action.<br>
     * Executed in {@link Logger#isDebugEnabled() debug level} or less.<br>
     * {@link OutputStream#write(byte[]) write} {@link HtmlPage#asXml() content
     * of current page} into {@link #OUTPUT_STREAM target Stream}.
     *
     * @param page The {@link HtmlPage HTML page} to write.
     */
    protected void debug() {
        debug(page);
    }

    /**
     * Get the <code>&lt;p:messages/&gt;</code> content.
     *
     * @param xpath The XPath to find the tag.<br>
     *            Depends on message level.
     * @return The tag content.
     */
    private String getMessage(String xpath) {
        @SuppressWarnings("unchecked")
        List<HtmlSpan> spans = (List<HtmlSpan>) page.getByXPath(xpath);
        if (spans.isEmpty())
            return null;
        if (spans.size() > 1)
            throw new NavigatorException("More than 1 <p:message> tag found into page.");
        return spans.get(0).getTextContent();
    }

    public String getMessageError() {
        return getMessage("//span[@class='ui-messages-error-summary']");
    }

    public String getMessageInfo() {
        return getMessage("//span[@class='ui-messages-info-summary']");
    }

    public HtmlPage getPage() {
        return page;
    }

    /**
     * Find PrimeFaces {@code <head><link>} tag and parse the {@code href} resource URL.
     *
     * @return The Theme key.
     * @see Theme
     */
    public String getTheme() {
        String resourceUrl = "/javax.faces.resource/theme.css.xhtml?ln=primefaces-";
        HtmlLink link = (HtmlLink) page.getByXPath("//head/link[contains(@href, '" + resourceUrl + "')]").get(0);
        return link.getAttribute("href").substring(resourceUrl.length());
    }

    public ChangePasswordPage gotoChangePasswordPage() {
        try {
            page = webClient.getPage(getUrl("/change-password.xhtml"));
            debug();
            return new ChangePasswordPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public IndexPage gotoIndex() {
        try {
            page = webClient.getPage(getUrl(null));
            debug();
            return new IndexPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public LoginPage gotoLoginPage() {
        try {
            page = webClient.getPage(getUrl("/login.xhtml"));
            debug();
            return new LoginPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public MyAccountPage gotoMyAccountPage() {
        try {
            page = webClient.getPage(getUrl("/my-profile.xhtml"));
            debug();
            return new MyAccountPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public ParametersJasperReportPage gotoParametersJasperReportPage() {
        try {
            page = webClient.getPage(getUrl("/report/parameters.xhtml"));
            debug();
            return new ParametersJasperReportPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public ProfilesPage gotoProfilesPage() {
        try {
            page = webClient.getPage(getUrl("/profile/list.xhtml"));
            debug();
            return new ProfilesPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public UsersRightsJasperReportPage gotoReportsUsersRightsPage() {
        try {
            page = webClient.getPage(getUrl("/report/usersRights.xhtml"));
            debug();
            return new UsersRightsJasperReportPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public RightsPage gotoRightsPage() {
        try {
            page = webClient.getPage(getUrl("/right/list.xhtml"));
            debug();
            return new RightsPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public FilterPathPage gotoSampleFilterSimple() {
        try {
            page = webClient.getPage(getUrl("/sample/filterPath.xhtml"));
            debug();
            return new FilterPathPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    public UsersPage gotoUsersPage() {
        try {
            page = webClient.getPage(getUrl("/user/list.xhtml"));
            debug();
            return new UsersPage(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    /**
     * Wait end of JavaScript (and Ajax) actions.<br>
     * Continue after the delay.
     *
     * @param ms The max time in millisecond.
     */
    public void waitJS(Delay delay) {
        LOGGER.debug("Wait JavaScript");
        final int period = 200 /* ms */;
        JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
        for (int t = 0; manager.getJobCount() > 0 && t < delay.getMs(); t += period)
            try {
                LOGGER.trace("Wait " + t + "ms");
                sleep(period);
            } catch (InterruptedException e) {
                throw new NavigatorException(e);
            }
    }

}