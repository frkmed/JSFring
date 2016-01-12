package fr.pinguet62.jsfring.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.Expression;

import fr.pinguet62.jsfring.model.Profile;
import fr.pinguet62.jsfring.model.QProfile;

/** The DAO for {@link Profile}. */
@Repository
public class ProfileDao extends AbstractDao<Profile, Integer> {

    @Override
    protected Expression<Profile> getBaseExpression() {
        return QProfile.profile;
    }

}