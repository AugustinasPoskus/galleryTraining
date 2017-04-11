package Validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;

public class MaxLengthValidator implements Validator {

    public void validate(ValidationContext ctx) {
        Number maxLength = 15;
        if (ctx.getProperty().getValue() instanceof String) {
            String value = (String) ctx.getProperty().getValue();
            if (value.length() > maxLength.longValue()) {
                ctx.setInvalid();
            }
        } else {
            ctx.setInvalid();
        }
    }
}