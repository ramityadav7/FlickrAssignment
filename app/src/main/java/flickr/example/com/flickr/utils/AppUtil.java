package flickr.example.com.flickr.utils;

import java.util.Collection;

public class AppUtil {

    //Check Collection for Empty
    public static boolean isCollectionEmpty(Collection<? extends Object> collection)
    {
        if(collection == null || collection.isEmpty())
        {
            return true;
        }

        return false;
    }
}
