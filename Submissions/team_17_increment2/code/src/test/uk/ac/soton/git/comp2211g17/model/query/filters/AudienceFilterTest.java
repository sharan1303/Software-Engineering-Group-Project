package uk.ac.soton.git.comp2211g17.model.query.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.generated.tables.User;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Gender;
import uk.ac.soton.git.comp2211g17.model.types.category.IncomeGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AudienceFilterTest {
    private AudienceFilter filter;

    @BeforeEach
    public void setUpEach() { filter = new AudienceFilter(); }

    @Test
    public void ageSetCorrectly() {
        filter.setAge(AudienceAge.YOUNG);
        assertEquals(User.USER.AGE.eq(AudienceAge.YOUNG), filter.getCondition());
    }

    @Test
    public void incomeSetCorrectly() {
        filter.setIncome(IncomeGroup.LOW);
        assertEquals(User.USER.INCOME.eq(IncomeGroup.LOW), filter.getCondition());
    }

    @Test
    public void genderSetCorrectly() {
        filter.setGender(Gender.OTHER);
        assertEquals(User.USER.GENDER.eq(Gender.OTHER), filter.getCondition());
    }

    @Test
    public void complexConditionsHandledCorrectly() {
        filter.setAge(AudienceAge.YOUNG);
        filter.addAge(AudienceAge.YOUNG_ADULT);
        filter.setGender(Gender.MALE);
        filter.addGender(Gender.FEMALE);
        filter.setIncome(IncomeGroup.LOW);
        filter.addIncome(IncomeGroup.MEDIUM);
        assertEquals(User.USER.AGE.eq(AudienceAge.YOUNG).or(
                        User.USER.AGE.eq(AudienceAge.YOUNG_ADULT)).and(
                                User.USER.GENDER.eq(Gender.MALE)).or(
                                        User.USER.GENDER.eq(Gender.FEMALE)).and(
                                                User.USER.INCOME.eq(IncomeGroup.LOW)).or(
                                                        User.USER.INCOME.eq(IncomeGroup.MEDIUM)),
                filter.getCondition());
    }
}
