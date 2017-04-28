package lt.insoft.training.myComponents;

import org.zkoss.zk.ui.select.annotation.Listen;

import java.awt.*;

public class CustomModal extends org.zkoss.zk.ui.HtmlBasedComponent {
    private String title = "";
    private String type = "";
    private boolean show = false;


    public void setTitle(String title) {
        title = title;
        smartUpdate("title", title);
        this.setShow(true);
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        smartUpdate("type", type);
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
        smartUpdate("show", show);
    }

    protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException {
        super.renderProperties(renderer);
        render(renderer, "title", title);
        render(renderer, "type", type);
        render(renderer, "show", show);
    }

    public void onClick(Event e) throws InterruptedException{
        System.out.println("A");
    }

}
