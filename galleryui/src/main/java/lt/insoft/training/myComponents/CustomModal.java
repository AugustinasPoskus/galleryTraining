package lt.insoft.training.myComponents;

public class CustomModal extends org.zkoss.zk.ui.HtmlBasedComponent {
    private String _title = "";
    private String _type = "";


    public void setTitle(String title) {
        if (!_title.equals(title)) {
            _title = title;
            smartUpdate("title", _title);
        }
    }

    public String getTitle(){
        return _title;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException {
        super.renderProperties(renderer);
        render(renderer, "title", _title);
        render(renderer, "type", _type);
    }
}
