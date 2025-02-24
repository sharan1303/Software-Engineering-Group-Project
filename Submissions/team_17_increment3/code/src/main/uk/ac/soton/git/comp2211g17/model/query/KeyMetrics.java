package uk.ac.soton.git.comp2211g17.model.query;

import uk.ac.soton.git.comp2211g17.model.query.metrics.*;
import uk.ac.soton.git.comp2211g17.model.query.rawdata.ClickLogQuery;
import uk.ac.soton.git.comp2211g17.model.query.rawdata.ImpressionLogQuery;
import uk.ac.soton.git.comp2211g17.model.query.rawdata.ServerLogQuery;

import java.util.function.Function;

public enum KeyMetrics {
    IMPRESSIONS("Number of Impressions", ImpressionCountQuery::new, ImpressionLogQuery::new),
    CLICKS("Number of Clicks", ClickCountQuery::new, ClickLogQuery::new),
    UNIQUES("Number of Unique Clicks", UniqueCountQuery::new, ClickLogQuery::new),
    BOUNCES("Number of Bounces", BounceCountQuery::new, ServerLogQuery::new),
    CONVERSIONS("Number of Conversions", ConversionCountQuery::new, ServerLogQuery::new),
    TOTAL_COST("Total Cost", TotalCostQuery::new, ClickLogQuery::new),
    CTR("Click Through Rate", ClickThroughRateQuery::new, ClickLogQuery::new),
    CPA("Cost Per Acquisition", CostPerAcquisitionQuery::new, ClickLogQuery::new),
    CPC("Cost Per Click", CostPerClickQuery::new, ClickLogQuery::new),
    CPM("Cost Per 1000 Impressions (CPM)", CostPerThousandImpressionsQuery::new, ImpressionLogQuery::new),
    BOUNCE_RATE("Bounce Rate", BounceRateQuery::new, ServerLogQuery::new);

    String name;
    Function<DatabaseManager, KeyMetricQuery> constructor;
    Function<DatabaseManager, PaginatableQuery> rawDataConstructor;

    KeyMetrics(String name, Function<DatabaseManager, KeyMetricQuery> constructor, Function<DatabaseManager, PaginatableQuery> rawDataConstructor) {
        this.name = name;
        this.constructor = constructor;
        this.rawDataConstructor = rawDataConstructor;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public KeyMetricQuery construct(DatabaseManager dbm) {
        return constructor.apply(dbm);
    }

    public PaginatableQuery constructRawData(DatabaseManager dbm) {
        return rawDataConstructor.apply(dbm);
    }
}
