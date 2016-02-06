package fr.pinguet62.jsfring.gui.htmlunit.datatable.popup;

import static fr.pinguet62.jsfring.gui.htmlunit.AbstractPage.Delay.LONG;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import fr.pinguet62.jsfring.gui.htmlunit.AbstractPage;
import fr.pinguet62.jsfring.gui.htmlunit.NavigatorException;

/** {@link Popup} with "Submit" button. */
public interface SubmitPopup extends Popup {

    default void submit() {
        HtmlButton submit = (HtmlButton) getDialog().getByXPath("./div[contains(@class, 'ui-dialog-content')]/form/button")
                .get(0);
        try {
            HtmlPage page = submit.click();
            waitJS(LONG);
            AbstractPage.debug(page); // TODO debug();
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

}