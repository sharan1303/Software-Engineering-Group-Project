package uk.ac.soton.git.comp2211g17.model.types.field;

import uk.ac.soton.git.comp2211g17.model.types.Field;
import uk.ac.soton.git.comp2211g17.model.types.column.StringColumn;

public class StringField extends Field {

    public StringField(String name) {
        super(name);
    }

    public StringColumn makeColumn(String[] data) {
        return new StringColumn(this, data);
    }
}
