package fr.pinguet62.jsfring.gui.htmlunit;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import fr.pinguet62.jsfring.gui.ForgottenPasswordBean;
import fr.pinguet62.jsfring.gui.htmlunit.field.InputText;
import fr.pinguet62.jsfring.gui.htmlunit.field.ReadWriteField;

/** @see ForgottenPasswordBean */
public final class ForgottenPasswordPage extends AbstractPage {

    protected ForgottenPasswordPage(HtmlPage page) {
        super(page);
    }

    public ReadWriteField<?, String> getEmail() {
        return new InputText(getForm().getFirstByXPath(".//input[contains(@id, ':email')]"));
    }

    private HtmlForm getForm() {
        return (HtmlForm) page.getByXPath("//form").get(0);
    }

    public void submit() {
        HtmlButton submit = (HtmlButton) getForm().getByXPath(".//button[@type='submit']").get(0);
        try {
            page = submit.click();
            waitJS();
            waitJS(); // Service treatment in addition of Ajax
            debug();
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

}