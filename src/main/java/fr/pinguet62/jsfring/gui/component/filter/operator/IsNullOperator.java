package fr.pinguet62.jsfring.gui.component.filter.operator;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.SimpleExpression;

public final class IsNullOperator<T> implements
        Operator<SimpleExpression<T>, T> {

    private static final long serialVersionUID = 1;

    @Override
    public BooleanExpression apply(SimpleExpression<T> path, T arg1, T arg2) {
        return path.isNull();
    }

    @Override
    public boolean equals(Object obj) {
        return equalsUtil(obj);
    }

    @Override
    public String getMessage() {
        return "is null";
    }

    @Override
    public int getNumberOfParameters() {
        return 0;
    }

    @Override
    public int hashCode() {
        return hashCodeUtil();
    }

}