package flickr.example.com.flickr.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import flickr.example.com.flickr.data.HomeDataItem;
import flickr.example.com.flickr.utils.AppUtil;

public class ImageSliderAdapter extends PagerAdapter{

    private List<HomeDataItem> homeDataItems;
    private Context mContext;

    public ImageSliderAdapter(List<HomeDataItem> homeDataItems, Context context) {
        this.homeDataItems = homeDataItems;
        this.mContext = context;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return AppUtil.isCollectionEmpty(homeDataItems) ? 0 : homeDataItems.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        Picasso.get().load(homeDataItems.get(position).getThumbnail()).into(imageView);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }
}
