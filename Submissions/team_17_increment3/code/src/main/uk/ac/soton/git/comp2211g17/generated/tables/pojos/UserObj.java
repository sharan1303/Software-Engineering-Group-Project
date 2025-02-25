/*
 * This file is generated by jOOQ.
 */
package uk.ac.soton.git.comp2211g17.generated.tables.pojos;


import java.io.Serializable;

import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserObj implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long        id;
    private final AudienceAge age;
    private final Gender      gender;
    private final IncomeGroup income;
    private final Context     context;

    public UserObj(UserObj value) {
        this.id = value.id;
        this.age = value.age;
        this.gender = value.gender;
        this.income = value.income;
        this.context = value.context;
    }

    public UserObj(
        Long        id,
        AudienceAge age,
        Gender      gender,
        IncomeGroup income,
        Context     context
    ) {
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.income = income;
        this.context = context;
    }

    /**
     * Getter for <code>user.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>user.age</code>.
     */
    public AudienceAge getAge() {
        return this.age;
    }

    /**
     * Getter for <code>user.gender</code>.
     */
    public Gender getGender() {
        return this.gender;
    }

    /**
     * Getter for <code>user.income</code>.
     */
    public IncomeGroup getIncome() {
        return this.income;
    }

    /**
     * Getter for <code>user.context</code>.
     */
    public Context getContext() {
        return this.context;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UserObj (");

        sb.append(id);
        sb.append(", ").append(age);
        sb.append(", ").append(gender);
        sb.append(", ").append(income);
        sb.append(", ").append(context);

        sb.append(")");
        return sb.toString();
    }
}
