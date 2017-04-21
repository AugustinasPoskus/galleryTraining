package lt.insoft.training.ViewModel;


import lt.insoft.training.model.SearchObject;
import lt.insoft.training.model.Thumbnail;
import lt.insoft.training.services.SearchService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchViewModel {

    @WireVariable
    private SearchService searchService;
    private SearchObject searchObject = new SearchObject();
    private List<Thumbnail> thumbnails;
    @Wire("#rg")
    Radiogroup radiogroup;

    @Init
    public void init() {
        thumbnails = new ArrayList<>();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange({"thumbnails", "searchObject"})
    public void search() {
        thumbnails = searchService.searchThumbnails(searchObject);
        searchObject = new SearchObject();
        radiogroup.setSelectedIndex(2);
    }

    public SearchObject getSearchObject() {
        return searchObject;
    }

    public void setSearchObject(SearchObject searchObject) {
        this.searchObject = searchObject;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }
}
