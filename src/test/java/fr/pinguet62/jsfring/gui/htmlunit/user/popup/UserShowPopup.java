package fr.pinguet62.jsfring.gui.htmlunit.user.popup;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

import fr.pinguet62.jsfring.gui.htmlunit.field.BooleanOutputText;
import fr.pinguet62.jsfring.gui.htmlunit.field.Field;
import fr.pinguet62.jsfring.gui.htmlunit.field.ListField;
import fr.pinguet62.jsfring.gui.htmlunit.field.StringOutputText;

public final class UserShowPopup extends AbstractUserPopup {

    public UserShowPopup(HtmlPage page) {
        super(page);
    }

    public Field<?, ?> getEmail() {
        return new StringOutputText((HtmlSpan) getFieldTableCell(1).getByXPath("./span").get(0));
    }

    public Field<?, ?> getProfiles() {
        return new ListField((HtmlDivision) getFieldTableCell(4).getByXPath("./div[contains(@class, 'ui-datalist')]").get(0));
    }

    public Field<?, ?> isActive() {
        return new BooleanOutputText((HtmlSpan) getFieldTableCell(2).getByXPath("./span").get(0));
    }

}