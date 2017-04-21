package Validators;

import lt.insoft.training.model.PictureData;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;


public class UploadValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        PictureData pictureData = (PictureData) ctx.getValidatorArg("upload");
        String message = (String) ctx.getBindContext().getValidatorArg("message");
        if (pictureData == null || pictureData.getData() == null) {
            this.addInvalidMessage(ctx, "pictureUpload", message);
        }
    }
}