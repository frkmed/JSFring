package fr.pinguet62.jsfring.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import com.mysema.query.jpa.impl.JPAQuery;

import fr.pinguet62.jsfring.model.Profile;
import fr.pinguet62.jsfring.model.QProfile;
import fr.pinguet62.jsfring.model.Right;
import fr.pinguet62.jsfring.service.AbstractService;
import fr.pinguet62.jsfring.service.ProfileService;
import fr.pinguet62.jsfring.service.RightService;
import fr.pinguet62.jsfring.util.cdi.SpringViewScoped;

/** @see Profile */
@Named
@SpringViewScoped
public final class ProfilesManagedBean extends AbstractCrudManagedBean<Profile> {

    private static final long serialVersionUID = 1;

    @Inject
    private transient ProfileService profileService;

    /**
     * The {@link Right} association (available/associated) of the
     * {@link #getSelectedValue() selected profile}.
     *
     * @property.getter {@link #getRightsAssociation()}
     * @property.setter {@link #setRightsAssociation(DualListModel)}
     */
    private DualListModel<Right> rightsAssociation = new DualListModel<Right>();

    @Inject
    private transient RightService rightService;

    @Override
    protected Profile getNewValue() {
        return new Profile();
    }

    @Override
    protected JPAQuery getQuery() {
        return new JPAQuery().from(QProfile.profile);
    }

    /** @property.attribute {@link #rightsAssociation} */
    public DualListModel<Right> getRightsAssociation() {
        return rightsAssociation;
    }

    @Override
    public AbstractService<Profile, ?> getService() {
        return profileService;
    }

    /** @property.attribute {@link #rightsAssociation} */
    public void setRightsAssociation(DualListModel<Right> rightsAssociation) {
        this.rightsAssociation = rightsAssociation;
    }

    /**
     * Call when the user click on "Show" or "Edit" button.
     * <ul>
     * <li>Set the selected {@link Profile};</li>
     * <li>Initialize the {@link Right} association.</li>
     * </ul>
     *
     * @param profile The selected {@link Profile}.
     */
    @Override
    public void setSelectedValue(Profile profile) {
        super.setSelectedValue(profile);

        // Custom
        List<Right> associatedRights = new ArrayList<>(getSelectedValue()
                .getRights());
        List<Right> availableRights = rightService.getAll().stream()
                .filter(right -> !associatedRights.contains(right))
                .collect(Collectors.toList());
        rightsAssociation = new DualListModel<Right>(availableRights,
                associatedRights);
    }

    /**
     * Call when the user click on "Submit" button into "Edit" dialog.
     * <p>
     * Save the modified {@link Profile}.
     */
    @Override
    public void update() {
        // Right association
        getSelectedValue().getRights().clear();
        getSelectedValue().getRights().addAll(rightsAssociation.getTarget());

        super.update();
    }

}