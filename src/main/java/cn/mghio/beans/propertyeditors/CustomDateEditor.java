package cn.mghio.beans.propertyeditors;

import cn.mghio.utils.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class CustomDateEditor extends PropertyEditorSupport {

    private final boolean allowEmpty;

    private final DateFormat dateFormat;

    private final int exactDateLength;

    public CustomDateEditor(boolean allowEmpty, DateFormat dateFormat) {
        this.allowEmpty = allowEmpty;
        this.dateFormat = dateFormat;
        this.exactDateLength = -1;
    }

    public CustomDateEditor(boolean allowEmpty, DateFormat dateFormat, int exactDateLength) {
        this.allowEmpty = allowEmpty;
        this.dateFormat = dateFormat;
        this.exactDateLength = exactDateLength;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasLength(text)) {
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0 && text.length() != exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        } else {
            try {
                setValue(this.dateFormat.parse(text));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Could not parse date: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? this.dateFormat.format(value) : "");
    }
}
