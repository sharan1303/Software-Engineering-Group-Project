package uk.ac.soton.git.comp2211g17.model.query.filters;

import org.junit.jupiter.api.Test;
import uk.ac.soton.git.comp2211g17.generated.tables.Click;
import uk.ac.soton.git.comp2211g17.generated.tables.Impr;
import uk.ac.soton.git.comp2211g17.generated.tables.Srv;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateFilterTest {
    @Test
    public void fromDateSetCorrectly() {
        LocalDateTime time = LocalDateTime.now();
        DateFilter filter = new DateFilter(DateFilter.DateOperator.FROM, time, Impr.IMPR);
        assertEquals(Impr.IMPR.DATE.ge(time), filter.getCondition());
    }

    @Test
    public void toDateSetCorrectly() {
        LocalDateTime time = LocalDateTime.now();
        DateFilter filter = new DateFilter(DateFilter.DateOperator.TO, time, Impr.IMPR);
        assertEquals(Impr.IMPR.DATE.le(time), filter.getCondition());
    }

    @Test
    public void appliesToImpr() {
        LocalDateTime time = LocalDateTime.now();
        DateFilter filter = new DateFilter(DateFilter.DateOperator.TO, time, Impr.IMPR);
        assertEquals(Impr.IMPR.DATE.le(time), filter.getCondition());
    }

    @Test
    public void appliesToSrv() {
        LocalDateTime time = LocalDateTime.now();
        DateFilter filter = new DateFilter(DateFilter.DateOperator.TO, time, Srv.SRV);
        assertEquals(Srv.SRV.ENTRYDATE.le(time), filter.getCondition());
    }

    @Test
    public void appliesToClick() {
        LocalDateTime time = LocalDateTime.now();
        DateFilter filter = new DateFilter(DateFilter.DateOperator.TO, time, Click.CLICK);
        assertEquals(Click.CLICK.DATE.le(time), filter.getCondition());
    }
}
