package uk.ac.soton.git.comp2211g17.model.types.column;

import uk.ac.soton.git.comp2211g17.model.types.Column;
import uk.ac.soton.git.comp2211g17.model.types.field.StringField;

public class StringColumn extends Column<StringField> {

    private final String[] data;

    public StringColumn(StringField field, String[] data) {
        super(field, data.length);
        this.data = data;
    }

    @Override
    public String[] getDataAsFormatted() {
        return data;
    }
}
