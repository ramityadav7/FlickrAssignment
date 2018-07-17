package flickr.example.com.flickr.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import flickr.example.com.flickr.R;
import flickr.example.com.flickr.data.HomeDataItem;
import flickr.example.com.flickr.utils.AppUtil;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<HomeDataItem> homeDataItems;
    private HomeItemClickHandler homeItemClickHandler;

    public interface HomeItemClickHandler {
        void onHomeItemClick(int position);
    }

    public HomeAdapter(List<HomeDataItem> homeDataItems, HomeItemClickHandler homeItemClickHandler)
    {
        this.homeDataItems = homeDataItems;
        this.homeItemClickHandler = homeItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return AppUtil.isCollectionEmpty(homeDataItems) ? 0 : homeDataItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        View view = layoutInflater.inflate(R.layout.home_item, parent, false);
        viewHolder = new HomeItemHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HomeItemHolder homeItemHolder = (HomeItemHolder) holder;
        HomeDataItem homeDataItem = homeDataItems.get(position);


        Picasso.get().load(homeDataItem.getThumbnail()).into(homeItemHolder.imageView);

        homeItemHolder.imageView.setTag(position);
        homeItemHolder.imageView.setOnClickListener(this);

    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public HomeItemHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imageViewThumb);
        }
    }

    @Override
    public void onClick(View v) {
        homeItemClickHandler.onHomeItemClick((int)v.getTag());
    }
}
