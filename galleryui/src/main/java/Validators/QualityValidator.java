package Validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class QualityValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        String message = (String) ctx.getBindContext().getValidatorArg("message");
        String quality = (String) ctx.getProperty().getValue();
        if (quality == null || !quality.matches("\\d{1,4}x\\d{1,4}")) {
            addInvalidMessage(ctx, message);
        }
    }
}
