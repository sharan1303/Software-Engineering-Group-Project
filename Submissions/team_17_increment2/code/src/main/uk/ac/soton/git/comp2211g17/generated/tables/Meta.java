/*
 * This file is generated by jOOQ.
 */
package uk.ac.soton.git.comp2211g17.generated.tables;


import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import uk.ac.soton.git.comp2211g17.generated.DefaultSchema;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Meta extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>meta</code>
     */
    public static final Meta META = new Meta();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>meta.schemaVersion</code>.
     */
    public final TableField<Record, Long> SCHEMAVERSION = createField(DSL.name("schemaVersion"), SQLDataType.BIGINT, this, "");

    private Meta(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Meta(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("CREATE VIEW meta (schemaVersion) AS SELECT 2"));
    }

    /**
     * Create an aliased <code>meta</code> table reference
     */
    public Meta(String alias) {
        this(DSL.name(alias), META);
    }

    /**
     * Create an aliased <code>meta</code> table reference
     */
    public Meta(Name alias) {
        this(alias, META);
    }

    /**
     * Create a <code>meta</code> table reference
     */
    public Meta() {
        this(DSL.name("meta"), null);
    }

    public <O extends Record> Meta(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, META);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Meta as(String alias) {
        return new Meta(DSL.name(alias), this);
    }

    @Override
    public Meta as(Name alias) {
        return new Meta(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Meta rename(String name) {
        return new Meta(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Meta rename(Name name) {
        return new Meta(name, null);
    }
}
