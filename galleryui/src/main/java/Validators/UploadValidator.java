package Validators;

import lt.insoft.training.model.PictureData;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;


public class UploadValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        try {
            PictureData pictureData = (PictureData) ctx.getValidatorArg("upload");
            if (pictureData == null || pictureData.getData() == null) {
                this.addInvalidMessage(ctx, "pictureUpload","Please upload a picture!");
            }
        } catch (ClassCastException e) {
            addInvalidMessage(ctx, "pictureUpload", "Wrong input!");
        }
    }
}