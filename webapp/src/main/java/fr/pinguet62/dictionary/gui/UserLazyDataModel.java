package fr.pinguet62.dictionary.gui;

import javax.faces.model.DataModel;

import fr.pinguet62.dictionary.model.User;
import fr.pinguet62.dictionary.service.UserService;

/** Lazy {@link DataModel} for {@link User}. */
public final class UserLazyDataModel extends AbstractLazyDataModel<User> {

    /** Serial version UID. */
    private static final long serialVersionUID = 1;

    /**
     * Constructor.
     *
     * @param service
     *            The {@link UserService}.
     */
    public UserLazyDataModel(UserService service) {
        super(service);
    }

}
