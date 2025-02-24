package uk.ac.soton.git.comp2211g17.model.query.misc;

import org.jooq.*;
import org.jooq.impl.DSL;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.model.query.Query;
import uk.ac.soton.git.comp2211g17.model.query.async.QueryPlan;
import uk.ac.soton.git.comp2211g17.model.query.util.QueryUtils;
import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.field.FloatField;
import uk.ac.soton.git.comp2211g17.model.types.field.IntegerField;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.jooq.impl.DSL.*;
import static uk.ac.soton.git.comp2211g17.generated.Tables.CLICK;

public class KeyStatisticsQuery implements Query {
	public static final FloatField maxClickCost = new FloatField("Maximum Click Cost");
	public static final FloatField avgClickCost = new FloatField("Mean Click Cost");
	public static final FloatField minClickCost = new FloatField("Minimum Click Cost");
	public static final IntegerField maxFrequency = new IntegerField("Maximum Click Cost Frequency (p)");
	public static final IntegerField minFrequency = new IntegerField("Minimum Click Cost Frequency (p)");
	public static final IntegerField medianFrequency = new IntegerField("Median Click Cost Frequency (p)");

	private final DatabaseManager dbm;

	public KeyStatisticsQuery(DatabaseManager dbm) {
		this.dbm = dbm;
	}

	@Override
	public Field<?>[] getFields() {
		return new Field<?>[]{maxClickCost, avgClickCost, minClickCost, minFrequency, medianFrequency, maxFrequency};
	}

	public Column<?>[] execute() throws SQLException {
		float maxClickCostD;
		float avgClickCostD;
		float minClickCostD;
		int maxFrequencyD;
		int minFrequencyD;
		int medianFrequencyD;

		DSLContext create = DSL.using(dbm.conn, SQLDialect.SQLITE);
		Record3<Float, BigDecimal, Float> clickResult = create.select(
				max(CLICK.CLICKCOST),
				avg(CLICK.CLICKCOST),
				min(CLICK.CLICKCOST))
				.from(CLICK).orderBy(CLICK.CLICKCOST).fetchOne();
		// TODO: missing data exception?

		if (clickResult == null || clickResult.get(0) == null) {
			return new Column<?>[]{
					maxClickCost.makeColumn(Collections.emptyList()),
					avgClickCost.makeColumn(Collections.emptyList()),
					minClickCost.makeColumn(Collections.emptyList()),
					minFrequency.makeColumn(Collections.emptyList()),
					medianFrequency.makeColumn(Collections.emptyList()),
					maxFrequency.makeColumn(Collections.emptyList())
			};
		}

		maxClickCostD = clickResult.get(max(CLICK.CLICKCOST));
		avgClickCostD = clickResult.get(avg(CLICK.CLICKCOST)).floatValue();
		minClickCostD = clickResult.get(min(CLICK.CLICKCOST));

		Result<Record2<Float, Integer>> clickResult2 = create.select(
				floor(CLICK.CLICKCOST),
				count(floor(CLICK.CLICKCOST))
		).from(CLICK)
				.where(CLICK.CLICKCOST.ne(0.0F)) // TODO: hmm
				.groupBy(floor(CLICK.CLICKCOST))
				.orderBy(count(floor(CLICK.CLICKCOST))).fetch();

		minFrequencyD = clickResult2.getValue(0, floor(CLICK.CLICKCOST)).intValue();
		maxFrequencyD = clickResult2.getValue(clickResult2.size() - 1, floor(CLICK.CLICKCOST)).intValue();

		List<Integer> rangeData = new ArrayList<>();
		List<Integer> freqData = new ArrayList<>(clickResult2.getValues(count(floor(CLICK.CLICKCOST))));

		for (Float value : clickResult2.getValues(floor(CLICK.CLICKCOST))) {
			rangeData.add((int) value.floatValue());
		}

		int medianIndex = QueryUtils.getMedian(freqData.stream().mapToInt(i -> i).toArray());
		medianFrequencyD = rangeData.get(medianIndex);

		return new Column<?>[]{
				maxClickCost.makeColumn(Collections.singletonList(maxClickCostD)),
				avgClickCost.makeColumn(Collections.singletonList(avgClickCostD)),
				minClickCost.makeColumn(Collections.singletonList(minClickCostD)),
				minFrequency.makeColumn(Collections.singletonList(minFrequencyD)),
				medianFrequency.makeColumn(Collections.singletonList(medianFrequencyD)),
				maxFrequency.makeColumn(Collections.singletonList(maxFrequencyD))
		};
	}

	@Override
	public QueryPlan buildQueryPlan() {
		return new QueryPlan(new KeyStatisticsQuery(dbm)::execute);
	}
}