package lt.insoft.training.validators;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;


public class LengthValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        Long maxLength = (Long) ctx.getBindContext().getValidatorArg("maxLength");
        Long minLength = (Long) ctx.getBindContext().getValidatorArg("minLength");
        String message = (String) ctx.getBindContext().getValidatorArg("message");
        String value = (String) ctx.getProperty().getValue();
        if (value.length() > maxLength || value.length() < minLength) {
            addInvalidMessage(ctx, message);
        }
    }
}