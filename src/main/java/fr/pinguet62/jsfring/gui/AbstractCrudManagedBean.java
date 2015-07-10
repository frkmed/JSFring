package fr.pinguet62.jsfring.gui;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pinguet62.jsfring.service.AbstractService;

/** {@link AbstractSelectableManagedBean} with CRUD operations on selected value. */
public abstract class AbstractCrudManagedBean<T> extends
AbstractSelectableManagedBean<T> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractCrudManagedBean.class);

    private static final long serialVersionUID = 1;

    /**
     * Create new value.
     * <p>
     * The {@link #getSelectedValue() selected value} is the new value
     * {@link #setSelectedValue(Object) set} by calling {@link #postCreate()}
     * before showing the creation view.
     * <p>
     * {@link #refresh() Refresh} list after creation.
     *
     * @see #postCreate()
     * @see AbstractService#create(Object)
     */
    public void create() {
        try {
            getService().create(getSelectedValue());
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "New element created with success", null));
            refresh();
        } catch (RuntimeException e) {
            LOGGER.warn("Error during creation", e);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error during creation", null));
        }
    }

    /**
     * Delete the selected value.
     * <p>
     * {@link #refresh() Refresh} list after deletion.
     *
     * @see #getSelectedValue()
     * @see AbstractService#delete(Object)
     */
    public void delete() {
        try {
            getService().delete(getSelectedValue());
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Element deleted with success", null));
            refresh();
        } catch (RuntimeException e) {
            LOGGER.warn("Error during deletion", e);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error during deletion", null));
        }
    }

    /**
     * Get a new instance of the value, for the creation.
     *
     * @see #create()
     */
    abstract protected T getNewValue();

    /**
     * Initialise the processus of creation by {@link #setSelectedValue(Object)
     * setting new selected value}.
     *
     * @see #getNewValue()
     * @see #setSelectedValue(Object)
     */
    public void postCreate() {
        setSelectedValue(getNewValue());
        refresh();
    }

    /**
     * Update the selected value.
     * <p>
     * {@link #refresh() Refresh} list after update.
     *
     * @see #getSelectedValue()
     * @see AbstractService#update(Object)
     */
    public void update() {
        try {
            getService().update(getSelectedValue());
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Element updated with success", null));
            refresh();
        } catch (RuntimeException e) {
            LOGGER.warn("Error during updating", e);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error during updating", null));
        }
    }

}