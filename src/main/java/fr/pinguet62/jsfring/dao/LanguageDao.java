package fr.pinguet62.jsfring.dao;

import javax.inject.Named;

import com.mysema.query.types.Expression;

import fr.pinguet62.jsfring.model.Language;
import fr.pinguet62.jsfring.model.QLanguage;

/** The DAO for {@link Language}. */
@Named
public final class LanguageDao extends AbstractDao<Language, String> {

    @Override
    protected Expression<Language> getBaseExpression() {
        return QLanguage.language;
    }

}