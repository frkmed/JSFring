package fr.pinguet62.jsfring.gui.htmlunit.field;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

import fr.pinguet62.jsfring.gui.htmlunit.NavigatorException;

public final class CheckBox extends ReadWriteField<HtmlDivision, Boolean> {

    public CheckBox(HtmlDivision div) {
        super(div);
    }

    /**
     * The {@link HtmlDivision} contains the {@code "ui-state-active"} class.
     */
    @Override
    public Boolean getValue() {
        HtmlDivision div = (HtmlDivision) html.getByXPath("./div[1]/input").get(0);
        String value = div.getAttribute("aria-checked");
        if ("true".equals(value))
            return true;
        else if ("false".equals(value))
            return false;
        else
            throw new NavigatorException("Unknown CheckBox 'aria-checked' value: " + value);
    }

    @Override
    public void setValue(Boolean value) {
        try {
            HtmlSpan span = (HtmlSpan) html.getByXPath("./div[2]/span[contains(@class, 'ui-chkbox-icon')]").get(0);
            HtmlPage page = span.click();
            debug(page);
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

}