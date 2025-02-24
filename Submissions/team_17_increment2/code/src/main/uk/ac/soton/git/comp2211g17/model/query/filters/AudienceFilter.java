package uk.ac.soton.git.comp2211g17.model.query.filters;

import uk.ac.soton.git.comp2211g17.generated.tables.User;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;

public class AudienceFilter extends Filter {
    public AudienceFilter() {
        condition = null;
    }

    public AudienceFilter(AudienceAge age) {
        this();
        this.setAge(age);
    }

    public AudienceFilter(IncomeGroup income) {
        this();
        this.setIncome(income);
    }

    public AudienceFilter(Gender gender) {
        this();
        this.setGender(gender);
    }

    public AudienceFilter setAge(AudienceAge age) {
        condition = condition != null ? condition.and(User.USER.AGE.eq(age)) : User.USER.AGE.eq(age);
        return this;
    }

    public AudienceFilter addAge(AudienceAge age) {
        condition = condition != null ? condition.or(User.USER.AGE.eq(age)) : User.USER.AGE.eq(age);
        return this;
    }

    public AudienceFilter setIncome(IncomeGroup income) {
        condition = condition != null ? condition.and(User.USER.INCOME.eq(income)) : User.USER.INCOME.eq(income);
        return this;
    }

    public AudienceFilter addIncome(IncomeGroup income) {
        condition = condition != null ? condition.or(User.USER.INCOME.eq(income)) : User.USER.INCOME.eq(income);
        return this;
    }

    public AudienceFilter setGender(Gender gender) {
        condition = condition != null ? condition.and(User.USER.GENDER.eq(gender)) : User.USER.GENDER.eq(gender);
        return this;
    }

    public AudienceFilter addGender(Gender gender) {
        condition = condition != null ? condition.or(User.USER.GENDER.eq(gender)) : User.USER.GENDER.eq(gender);
        return this;
    }
}
