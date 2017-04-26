package lt.insoft.training.validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TagsValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        int tagsCount = 6;
        String message = (String) ctx.getBindContext().getValidatorArg("message");
        String tags = (String) ctx.getProperty().getValue();
        if (!tags.matches("^$|^\\s*[\\w]+(\\s*,\\s*[\\w]+){0," + (tagsCount-1) + "}$")) {
            this.addInvalidMessage(ctx, message);
        }
    }
}
