/*
 * This file is generated by jOOQ.
 */
package uk.ac.soton.git.comp2211g17.generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MetaObj implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long schemaversion;

    public MetaObj(MetaObj value) {
        this.schemaversion = value.schemaversion;
    }

    public MetaObj(
        Long schemaversion
    ) {
        this.schemaversion = schemaversion;
    }

    /**
     * Getter for <code>meta.schemaVersion</code>.
     */
    public Long getSchemaversion() {
        return this.schemaversion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MetaObj (");

        sb.append(schemaversion);

        sb.append(")");
        return sb.toString();
    }
}
