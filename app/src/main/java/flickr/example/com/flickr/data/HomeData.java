package flickr.example.com.flickr.data;

import java.util.List;


public class HomeData{

    private List<HomeDataItem> homeDataItems;
    private boolean showProgress;
    private boolean showError;

    public List<HomeDataItem> getHomeDataItems() {
        return homeDataItems;
    }

    public void setHomeDataItems(List<HomeDataItem> homeDataItems) {
        this.homeDataItems = homeDataItems;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean showError) {
        this.showError = showError;
    }
}
