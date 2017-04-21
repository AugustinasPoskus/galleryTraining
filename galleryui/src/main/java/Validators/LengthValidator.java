package Validators;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;


public class LengthValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        Number maxLength = (Number) ctx.getBindContext().getValidatorArg("maxLength");
        Number minLength = (Number) ctx.getBindContext().getValidatorArg("minLength");
        String message = (String) ctx.getBindContext().getValidatorArg("message");
        String value = (String) ctx.getProperty().getValue();
        if (value.length() > maxLength.longValue() || value.length() < minLength.longValue()) {
            addInvalidMessage(ctx, message);
        }
    }
}