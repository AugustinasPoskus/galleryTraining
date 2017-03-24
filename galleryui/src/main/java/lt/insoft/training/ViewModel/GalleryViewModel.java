package lt.insoft.training.ViewModel;


import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel {

    String name = "test";
    int i = 0;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    @Command
    @NotifyChange("name")
    public void changeName(){
        this.name = "change " + i ;
        i++;
    }
}