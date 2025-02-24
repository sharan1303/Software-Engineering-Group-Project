package uk.ac.soton.git.comp2211g17.model.query.filters;

import org.jooq.*;
import org.jooq.impl.DSL;
import uk.ac.soton.git.comp2211g17.generated.tables.Click;
import uk.ac.soton.git.comp2211g17.generated.tables.Impr;
import uk.ac.soton.git.comp2211g17.generated.tables.Srv;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;

import java.time.LocalDateTime;

public class DateFilter extends Filter {
    public enum DateOperator {
        FROM,
        TO
    }

    /**
     * Create new date filter
     *
     * @param from from date
     * @param to to date
     * @param table table
     */
    public DateFilter(LocalDateTime from, LocalDateTime to, Table<? extends Record> table) {
        this(DateOperator.FROM, from, table);
        and(new DateFilter(DateOperator.TO, to, table));
    }

    /**
     * Create a new date filter
     *
     * @param operator FROM/TO
     * @param time date
     * @param table table
     */
    public DateFilter(DateOperator operator, LocalDateTime time, Table<? extends Record> table) {
        if (table instanceof Impr) {
            this.condition = operator == DateOperator.TO ? Impr.IMPR.DATE.le(time) : Impr.IMPR.DATE.ge(time);
        } else if (table instanceof Click) {
            this.condition = operator == DateOperator.TO ? Click.CLICK.DATE.le(time) : Click.CLICK.DATE.ge(time);
        } else if (table instanceof Srv) {
            this.condition = operator == DateOperator.TO ? Srv.SRV.ENTRYDATE.le(time) : Srv.SRV.EXITDATE.ge(time);
        }
    }

    public static void main(String[] args) {
        DatabaseManager dbm = new DatabaseManager();
        DSLContext create = DSL.using(dbm.connectToAuctionDB(), SQLDialect.SQLITE);

        DateFilter filter = new DateFilter(LocalDateTime.MIN, LocalDateTime.now(), Click.CLICK);

        Result<? extends Record> res = filter.applyToAndFetch(create.select().from(Click.CLICK));

        System.out.println(res);
    }
}
