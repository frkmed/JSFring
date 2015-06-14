package fr.pinguet62.jsfring.gui.component.filter.operator;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.NumberExpression;

public final class BetweenOperator<T extends Number & Comparable<?>> implements
NumberOperator<T> {

    private static final long serialVersionUID = 1;

    @Override
    public BooleanExpression apply(NumberExpression<T> path, T arg1, T arg2) {
        return path.between(arg1, arg2);
    }

    @Override
    public boolean equals(Object obj) {
        return equalsUtil(obj);
    }

    @Override
    public String getMessage() {
        return "between";
    }

    @Override
    public int getNumberOfParameters() {
        return 2;
    }

    @Override
    public int hashCode() {
        return hashCodeUtil();
    }

}