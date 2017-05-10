package lt.insoft.training.viewModel;


import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureSearchFilter;
import lt.insoft.training.model.Thumbnail;
import lt.insoft.training.services.SearchService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Radiogroup;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchViewModel extends SelectorComposer<Component> {

    @WireVariable
    private SearchService searchService;
    private PictureSearchFilter searchObject = new PictureSearchFilter();
    private PictureSearchFilter lastSearchedFields;
    private List<Thumbnail> thumbnails;
    @Wire("#rg")
    private Radiogroup radiogroup;
    private String tags = "";
    private Picture selectedPicture;
    private final int PAGE_SIZE = 8;
    private int picturesCount = 0;
    private int currentPage = 0;

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
        try {
            selectedPicture = searchService.findFullPicture(id);
        }catch (NoResultException e){
            selectedPicture = null;
        }
    }

    @Command
    @NotifyChange({"thumbnails", "searchObject", "tags", "picturesCount"})
    public void search() {
        currentPage = 0;
        List<String> tagList;
        if(tags.isEmpty()){
            tagList = null;
        }else {
            tagList = Arrays.asList(tags.split(", "));
        }
        searchObject.setPictureTags(tagList);
        picturesCount = searchService.getSearchPicturesCount(searchObject);
        if(picturesCount != 0){
            thumbnails = searchService.searchThumbnails(currentPage * PAGE_SIZE, PAGE_SIZE, searchObject);
        }else{
            thumbnails.clear();
        }
        lastSearchedFields = searchObject;
        radiogroup.setSelectedIndex(radiogroup.getSelectedIndex());
    }

    public PictureSearchFilter getSearchObject() {
        return searchObject;
    }

    public void setSearchObject(PictureSearchFilter searchObject) {
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

    @Command
    @NotifyChange({"picturesCount", "thumbnails"})
    public void paging() {
        picturesCount = searchService.getSearchPicturesCount(lastSearchedFields);
        thumbnails = searchService.searchThumbnails(currentPage * PAGE_SIZE, PAGE_SIZE, lastSearchedFields);
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    public int getPicturesCount() {
        return picturesCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Command
    public void close(){}

}
