/*
 * This file is generated by jOOQ.
 */
package uk.ac.soton.git.comp2211g17.generated;


import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import uk.ac.soton.git.comp2211g17.generated.tables.Click;
import uk.ac.soton.git.comp2211g17.generated.tables.Impr;
import uk.ac.soton.git.comp2211g17.generated.tables.Srv;


/**
 * A class modelling indexes of tables in the default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index CLICK_ID = Internal.createIndex(DSL.name("click_id"), Click.CLICK, new OrderField[] { Click.CLICK.ID }, false);
    public static final Index IMPR_ID = Internal.createIndex(DSL.name("impr_id"), Impr.IMPR, new OrderField[] { Impr.IMPR.ID }, false);
    public static final Index SRV_ID = Internal.createIndex(DSL.name("srv_id"), Srv.SRV, new OrderField[] { Srv.SRV.ID }, false);
}
