package lt.insoft.training.model;


import java.util.Date;

public class SearchObject {

    private String pictureName;

    private String pictureDescription;

    private String pictureQuality;

    private Date pictureInsertDate;

    private String sort;

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureDescription() {
        return pictureDescription;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public String getPictureQuality() {
        return pictureQuality;
    }

    public void setPictureQuality(String pictureQuality) {
        this.pictureQuality = pictureQuality;
    }

    public Date getPictureInsertDate() {
        return pictureInsertDate;
    }

    public void setPictureInsertDate(Date pictureInsertDate) {
        this.pictureInsertDate = pictureInsertDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
