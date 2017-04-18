package Validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.text.ParseException;

public class QualityValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        try{
            String quality = (String)ctx.getProperty().getValue();
            if(quality == null || !quality.matches("\\d{1,4}x\\d{1,4}")) {
                addInvalidMessage(ctx, "Please enter a valid picture quality! (e.g. 256x256)");
            }
        }catch (ClassCastException e){
            addInvalidMessage(ctx, "Wrong input!");
        }
    }
}
