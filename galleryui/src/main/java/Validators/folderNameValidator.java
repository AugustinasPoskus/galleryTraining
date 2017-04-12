package Validators;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;


public class folderNameValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
        validateName(ctx, (String)beanProps.get("name").getValue());
    }

    private void validateName(ValidationContext ctx, String name) {
        if(name == null || (name.length() > 15) || name.isEmpty() || (name.length() < 3)) {
            this.addInvalidMessage(ctx, "folderName", "Folder name should be between 3 and 15 characters long!");
        }
    }
}