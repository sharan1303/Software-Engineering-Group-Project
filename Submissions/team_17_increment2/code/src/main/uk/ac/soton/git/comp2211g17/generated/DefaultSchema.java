/*
 * This file is generated by jOOQ.
 */
package uk.ac.soton.git.comp2211g17.generated;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import uk.ac.soton.git.comp2211g17.generated.tables.Click;
import uk.ac.soton.git.comp2211g17.generated.tables.Impr;
import uk.ac.soton.git.comp2211g17.generated.tables.Meta;
import uk.ac.soton.git.comp2211g17.generated.tables.Srv;
import uk.ac.soton.git.comp2211g17.generated.tables.User;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>click</code>.
     */
    public final Click CLICK = Click.CLICK;

    /**
     * The table <code>impr</code>.
     */
    public final Impr IMPR = Impr.IMPR;

    /**
     * The table <code>meta</code>.
     */
    public final Meta META = Meta.META;

    /**
     * The table <code>srv</code>.
     */
    public final Srv SRV = Srv.SRV;

    /**
     * The table <code>user</code>.
     */
    public final User USER = User.USER;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Click.CLICK,
            Impr.IMPR,
            Meta.META,
            Srv.SRV,
            User.USER);
    }
}
