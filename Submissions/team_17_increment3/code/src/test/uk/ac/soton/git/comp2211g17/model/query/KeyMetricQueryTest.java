package uk.ac.soton.git.comp2211g17.model.query;

import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.model.query.filters.AudienceFilter;
import uk.ac.soton.git.comp2211g17.model.query.filters.ContextFilter;
import uk.ac.soton.git.comp2211g17.model.query.filters.DateFilter;
import uk.ac.soton.git.comp2211g17.model.query.metrics.KeyMetricQuery;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.category.AudienceAge;
import uk.ac.soton.git.comp2211g17.model.types.category.Context;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class KeyMetricQueryTest extends QueryTest {

    public void setQuery(KeyMetricQuery query) { super.setQuery(query); }

    public KeyMetricQuery getQuery() {
        return (KeyMetricQuery) super.getQuery();
    }

    @Test
    void contextFilterWorks() throws SQLException {
        getQuery().setFilter(table -> new ContextFilter(Context.NEWS));
        Column<?>[] result = getQuery().execute();
        assertEquals(1, result.length);
        assertEquals(1, result[0].getData().size());
        getQuery().setFilter(null);
    }

    @Test
    void audienceFilterWorks() throws SQLException {
        getQuery().setFilter(table -> new AudienceFilter(AudienceAge.OLD));
        Column<?>[] result = getQuery().execute();
        assertEquals(1, result.length);
        assertEquals(1, result[0].getData().size());
        getQuery().setFilter(null);
    }

    @Test
    void dateFilterWorks() throws SQLException {
        getQuery().setFilter(table -> new DateFilter(DateFilter.DateOperator.TO, LocalDateTime.now(), table));
        Column<?>[] result = getQuery().execute();
        assertEquals(1, result.length);
        assertEquals(1, result[0].getData().size());
        getQuery().setFilter(null);
    }
}
