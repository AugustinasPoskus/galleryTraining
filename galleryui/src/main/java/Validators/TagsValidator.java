package Validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TagsValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        int tagsCount = 6;
        try{
            String tags = (String)ctx.getProperty().getValue();
            if(!tags.matches("^$|^\\s*[\\w]+(\\s*,\\s*[\\w]+){0,5}$")){
                this.addInvalidMessage(ctx,"Please enter 0-" + tagsCount +" tags seperated by commas!");
            }
        }catch (ClassCastException e){
            addInvalidMessage(ctx, "Wrong input!");
        }
    }
}
