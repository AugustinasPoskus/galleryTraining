package lt.insoft.training.viewModel;


import lt.insoft.training.model.Picture;
import lt.insoft.training.model.SearchPictureObject;
import lt.insoft.training.model.Thumbnail;
import lt.insoft.training.myComponents.CustomModal;
import lt.insoft.training.services.PictureService;
import lt.insoft.training.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Radiogroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchViewModel extends SelectorComposer<Component> {

    @WireVariable
    private SearchService searchService;
    private SearchPictureObject searchObject = new SearchPictureObject();
    private List<Thumbnail> thumbnails;
    @Wire("#rg")
    private Radiogroup radiogroup;
    private String tags = "";
    private Picture selectedPicture;

    @Init
    public void init() {
        thumbnails = new ArrayList<>();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange("selectedPicture")
    public void open(@BindingParam("id") Long id) {
        selectedPicture = searchService.findFullPicture(id);
    }

    @Command
    @NotifyChange({"thumbnails", "searchObject", "tags"})
    public void search() {
        if (!tags.isEmpty()) {
            List<String> tagList = Arrays.asList(tags.split(","));
            searchObject.setPictureTags(tagList);
            tags = "";
        }
        thumbnails = searchService.searchThumbnails(searchObject);
        searchObject = new SearchPictureObject();
        radiogroup.setSelectedIndex(2);
    }

    public SearchPictureObject getSearchObject() {
        return searchObject;
    }

    public void setSearchObject(SearchPictureObject searchObject) {
        this.searchObject = searchObject;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Picture getSelectedPicture() {
        return selectedPicture;
    }

    public void setSelectedPicture(Picture selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

}
