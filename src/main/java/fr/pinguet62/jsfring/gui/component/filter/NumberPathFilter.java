package fr.pinguet62.jsfring.gui.component.filter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.expr.NumberExpression;

import fr.pinguet62.jsfring.gui.component.filter.operator.BetweenOperator;
import fr.pinguet62.jsfring.gui.component.filter.operator.EqualsToOperator;
import fr.pinguet62.jsfring.gui.component.filter.operator.GreaterThanOperator;
import fr.pinguet62.jsfring.gui.component.filter.operator.Operator;

/** A {@link PathFilter} for {@link NumberExpression} fields. */
public final class NumberPathFilter<T extends Number & Comparable<?>> extends
        PathFilter<NumberExpression<T>, T> implements Serializable {

    private static final long serialVersionUID = 1;

    public NumberPathFilter(NumberExpression<T> path) {
        super(path);
    }

    @Override
    public List<Operator<NumberExpression<T>, T>> getOperators() {
        return Arrays.asList(new EqualsToOperator<NumberExpression<T>, T>(),
                new GreaterThanOperator<T>(), new BetweenOperator<T>());
    }

}