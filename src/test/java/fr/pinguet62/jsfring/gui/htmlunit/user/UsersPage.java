package fr.pinguet62.jsfring.gui.htmlunit.user;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import fr.pinguet62.jsfring.gui.htmlunit.NavigatorException;
import fr.pinguet62.jsfring.gui.htmlunit.datatable.AbstractDatatablePage;
import fr.pinguet62.jsfring.gui.htmlunit.field.SelectOneButton;

public final class UsersPage extends AbstractDatatablePage<UserRow> {

    public static enum ActiveFilter {
        All, No, Yes;
    }

    public static enum Column {

        ACTIONS(5, "Actions"), ACTIVE(3, "Actif"), EMAIL(2, "Email"), LAST_CONNECTION(4, "Dernière connexion"), LOGIN(1,
                "Identifiant");

        static Column fromTitle(String title) {
            for (Column column : values())
                if (title.equals(column.title))
                    return column;
            throw new IllegalArgumentException("Unknown title: " + title);
        }

        private final int index;

        private final String title;

        private Column(int index, String title) {
            this.index = index;
            this.title = title;
        }

        public int getIndex() {
            return index;
        }

        public String getTitle() {
            return title;
        }

    }

    public UsersPage(HtmlPage page) {
        super(page);
    }

    /**
     * Check that {@link HtmlTableHeaderCell table header} {@code style}
     * contains {@code "display: none"} value;
     */
    public boolean columnVisibile(Column column) {
        HtmlTableHeaderCell th = getDatatableTableHeader(column.getTitle());
        return !th.getAttribute("style").matches(".*display *: *none.*");
    }

    public void filterByActive(ActiveFilter value) {
        // TODO common abstract parent method
        HtmlDivision div = (HtmlDivision) getDatatableTableHeader(Column.ACTIVE.getTitle()).getByXPath(
                "./div[contains(@class, 'ui-column-customfilter')]/div[contains(@class, 'ui-selectonebutton')]").get(0);
        SelectOneButton<ActiveFilter> selectOneButton = new SelectOneButton<ActiveFilter>(div, ActiveFilter::valueOf);
        selectOneButton.setValue(value);
    }

    /**
     * @param value The value to set.<br>
     *            {@code null} to reset filter.
     */
    public void filterLogin(String value) {
        HtmlInput input = (HtmlInput) getDatatableTableHeader(Column.LOGIN.getTitle()).getByXPath(
                "./input[contains(@class, 'ui-column-filter')]").get(0);
        input.setValueAttribute(value);
        waitJS();
        debug();
    }

    @Override
    protected Function<HtmlTableRow, UserRow> getRowFactory() {
        return UserRow::new;
    }

    // TODO test
    public void hideOrShowColumn(Column column) {
        Function<String, Column> converter = Column::fromTitle;

        try {
            HtmlButton toggler = (HtmlButton) getDatatableHeader().getByXPath("./button[contains(@id, 'toggler')]").get(0);

            // Show
            page = toggler.click();
            waitJS();
            debug();

            @SuppressWarnings("unchecked")
            List<HtmlListItem> choices = (List<HtmlListItem>) page
            .getByXPath("//div[contains(@class, 'ui-columntoggler')]/ul[contains(@class, 'ui-columntoggler-items')]/li[contains(@class, 'ui-columntoggler-item')]");
            HtmlListItem choice = choices.stream()
                    .filter(li -> converter.apply(((HtmlLabel) li.getByXPath("./label").get(0)).asText()).equals(column))
                    .findAny().get();
            page = ((HtmlDivision) choice.getByXPath("./div[contains(@class, 'ui-chkbox')]").get(0)).click();
            waitJS();
            debug();

            // Hide Toogler
            page = toggler.click();
            waitJS();
            debug();
        } catch (IOException e) {
            throw new NavigatorException(e);
        }
    }

    /**
     * Find the {@link HtmlTableHeaderCell} from column title, and click to sort
     * results.
     * <p>
     * <code>&lt;span class="...ui-column-title..."&gt;</code>
     *
     * @param title The column title.
     */
    public void sortBy(Column column) {
        sortBy(column.getTitle());
    }

}