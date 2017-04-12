package Validators;

import lt.insoft.training.model.PictureData;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;

public class ImageFormValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {

        Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
        validateName(ctx, (String)beanProps.get("name").getValue());
        validateQuality(ctx, (String)beanProps.get("quality").getValue());
        validateDescription(ctx, (String)beanProps.get("description").getValue());
        validateUpload(ctx, (PictureData)ctx.getValidatorArg("upload"));
        validateTags(ctx, (String)ctx.getValidatorArg("tags"));
    }

    private void validateName(ValidationContext ctx, String name) {
        if(name == null || (name.length() > 15) || name.isEmpty() || (name.length() < 3)) {
            this.addInvalidMessage(ctx, "name", "Your picture name should be between 3 and 15 characters!");
        }
    }

    private void validateDescription(ValidationContext ctx, String description) {
        if(description == null || (description.length() > 400) || description.isEmpty() || (description.length() < 3)) {
            this.addInvalidMessage(ctx, "description", "Your picture description should be between 3 and 400 characters!");
        }
    }

    private void validateQuality(ValidationContext ctx, String quality) {
        if(quality == null || !quality.matches("\\d{1,4}x\\d{1,4}")) {
            this.addInvalidMessage(ctx, "quality", "Please enter a valid picture quality! (e.g. 256x256)");
        }
    }
    private void validateUpload(ValidationContext ctx, PictureData pictureData) {
        if(pictureData == null || pictureData.getData() == null) {
            this.addInvalidMessage(ctx, "upload", "Please upload a picture!");
        }
    }

    private void validateTags(ValidationContext ctx, String tags) {
        if(!tags.matches("^\\s*[\\w]+(\\s*,\\s*[\\w]+){0,5}$")){
            this.addInvalidMessage(ctx, "tags", "Please enter 0-6 tags seperated by commas!");
        }
    }
}
