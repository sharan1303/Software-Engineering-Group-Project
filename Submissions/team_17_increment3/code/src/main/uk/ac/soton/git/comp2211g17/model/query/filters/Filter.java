package uk.ac.soton.git.comp2211g17.model.query.filters;

import org.jooq.*;
import org.jooq.Record;

public abstract class Filter {
    protected Condition condition;

    /**
     * Create filter with condition
     *
     * @param condition initial condition
     */
    public Filter(Condition condition) {
        this.condition = condition;
    }

    public Filter() {
        this.condition = null;
    }

    /**
     * Get current condition
     *
     * @return current condition
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * This and Other
     *
     * @param other filter
     * @return this filter
     */
    public Filter and(Filter other) {
        this.condition = this.condition.and(other.getCondition());

        return this;
    }

    /**
     * This or Other
     *
     * @param other filter
     * @return this filter
     */
    public Filter or(Filter other) {
        this.condition = this.condition.or(other.getCondition());

        return this;
    }

    /**
     * This and not Other
     *
     * @param other filter
     * @return this filter
     */
    public Filter andNot(Filter other) {
        this.condition = this.condition.andNot(other.getCondition());

        return this;
    }

    /**
     * This or not Other
     *
     * @param other filter
     * @return this filter
     */
    public Filter orNot(Filter other) {
        this.condition.orNot(other.getCondition());

        return this;
    }

    /**
     * Negate the filters condition
     *
     * @return this filter
     */
    public Filter not() {
        this.condition = this.condition.not();

        return this;
    }

    public Result<? extends Record> applyToAndFetch(SelectJoinStep<? extends Record> select) {
        return select.where(this.condition).fetch();
    }

    public SelectConditionStep<? extends Record> applyTo(SelectJoinStep<? extends Record> select) {
        return select.where(this.condition);
    }
}
