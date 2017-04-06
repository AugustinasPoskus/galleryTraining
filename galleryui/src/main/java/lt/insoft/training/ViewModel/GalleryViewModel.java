package lt.insoft.training.ViewModel;

import lt.insoft.training.model.Folder;
import lt.insoft.training.services.FolderService;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import java.util.List;

public class GalleryViewModel {
    @WireVariable
    private FolderService folderService;
    private List<Folder> folderList;
    private String folderName;
    private Long selectedId;
    private int foldersCount = 0;
    private int currentPage = 1;
    private final int paginationBy = 6;
    private int pagesCount;

    @Init
    public void init(@ContextParam(ContextType.EXECUTION) Execution execution) {
        foldersCount = folderService.foldersCount();
        this.calculatePages();
        String page = execution.getParameter("page");
        if (page != null) {
            if (page.equals("next") && (currentPage + 1) != pagesCount) {
                currentPage = currentPage + 1;
            } else if (page.equals("previous") && (currentPage - 1) != 0) {
                currentPage = currentPage - 1;
            } else {
                try {
                    currentPage = Integer.parseInt(page);
                    System.out.println("current page:" + currentPage + "pages count: " + pagesCount);
                    if (currentPage > pagesCount || (currentPage <= 0)) {
                        currentPage = 1;
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Page not found!");
                    currentPage = 1;
                }
            }
        }
        int firstFolderIndex = (currentPage - 1) * paginationBy;
        folderList = folderService.getFolders(firstFolderIndex, paginationBy);
        Clients.evalJavaScript("pagination(" + this.pagesCount + "," + currentPage + ");");
    }

    @Command
    public void open(@BindingParam("id") Long id) {
        Executions.sendRedirect(UriComponentsBuilder.fromUriString("images.zul").queryParam("folderId", id).build().encode().toString());
    }

    @Command
    @NotifyChange({"folderList", "foldersCount", "pagesCount"})
    public void add() {
        Folder folder = new Folder();
        folder.setName("Unnamed");
        folderService.addFolder(folder);
        if (folderList.size() < paginationBy) {
            folderList.add(folder);
        }
        foldersCount++;
        this.calculatePages();
        Clients.evalJavaScript("paginationChange(" + this.pagesCount + ");");
    }

    @Command
    @NotifyChange({"folderList", "foldersCount", "pagesCount"})
    public void remove(@BindingParam("id") Long id) {
        if (this.containsId(id)) {
            if (folderService.removeFolder(id)) {
                folderList.removeIf(p -> p.getId().equals(id));
                foldersCount--;
                System.out.println("folderCount: " + foldersCount + " folder List size : " + ((currentPage - 1) * paginationBy + folderList.size()));
                if (foldersCount > (currentPage - 1) * paginationBy + folderList.size()) {
                    folderList.add(folderService.getFolders(paginationBy - 1, 1).get(0));
                }
                this.calculatePages();
                Clients.evalJavaScript("paginationChange(" + this.pagesCount + ");");
            }
        }
    }

    @Command
    @NotifyChange("folderList")
    public void editFolderName() {
        if (this.containsId(this.selectedId) && !(this.folderName.equals(""))) {
            if (folderService.updateFolder(this.selectedId, this.folderName)) {
                for (Folder folder : folderList) {
                    if (folder.getId().equals(this.selectedId)) {
                        folder.setName(this.folderName);
                    }
                }
            }
        }
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    public boolean containsId(Long id) {
        List<Folder> list = this.getFolderList();
        for (Folder object : list) {
            if (object.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    @Command
    @NotifyChange("folderName")
    public void setSelectedId(@BindingParam("id") Long id) {
        this.selectedId = id;
        for (Folder folder : folderList) {
            if (folder.getId().equals(this.selectedId)) {
                this.folderName = folder.getName();
            }
        }
    }

    public int getFoldersCount() {
        return foldersCount;
    }

    public void setFoldersCount(int foldersCount) {
        this.foldersCount = foldersCount;
    }

    private void calculatePages() {
        if (foldersCount % paginationBy == 0) {
            this.pagesCount = foldersCount / paginationBy;
        } else {
            this.pagesCount = foldersCount / paginationBy + 1;
        }
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}