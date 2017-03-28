package lt.insoft.training.ViewModel;

import org.apache.commons.io.IOUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.io.InputStream;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImagesViewModel {

    private byte[] image = this.getFileBytes();
    private Long value;
    @Init
    public void init() {
        Session session = Sessions.getCurrent();
        if (session.hasAttribute("id")) {
            value = (Long) session.getAttribute("id");
        }
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    private byte[] getFileBytes() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("folder.png");
            byte[] bytes = IOUtils.toByteArray(is);
            return bytes;
        } catch (IOException e) {
            System.out.println(e);
        }
        return new byte[0];
    }
}
