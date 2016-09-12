package yh2tan.crowdcurio.dummy;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    static String tag = "Content Construction";

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    private static int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static ArrayList<String> favourite = new ArrayList<String>();

    /* add a favourite project by ID */
    public static void addFavourite(String i){
        favourite.add(i);
        Log.d("add", i);
        ITEM_MAP.get(String.valueOf(i)).setFavourite(true);
    }

    public static void removeFavourite(String i){
        favourite.remove(i);
        Log.d("remove", i);
        ITEM_MAP.get(String.valueOf(i)).setFavourite(false);
    }

    public static void filterFavourite(){

        List<DummyItem> newItem = new ArrayList<DummyItem>();
        Map<String, DummyItem> newMap = new HashMap<String, DummyItem>();
        COUNT = 0;
        for (DummyItem di : ITEMS){
            if (favourite.contains(di.id)){
                newItem.add(di);
                newMap.put(di.id,di);
                COUNT++;
            }
        }

        ITEMS = newItem;
        ITEM_MAP = newMap;
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, "", "", makeDetails(position), "", true);
    }

    /* used as placeholders */
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static void constructContent(JSONArray json){
        /* avatar maps to title by index*/

        // clear all items
        ITEMS.clear();
        ITEM_MAP.clear();

        COUNT = json.length();

        // construct contents with given resource
        for (int i = 0; i < json.length(); i ++){

            try {
                JSONObject jo = json.getJSONObject(i);

                String id = String.valueOf(jo.getInt("id"));
                String name = jo.getString("name");
                String avatar = jo.getString("avatar");
                String shortd = jo.getString("short_description");
                String longd = jo.getString("description");
                boolean isActive = jo.getBoolean("is_active");
                String owner = jo.getString("owner");
                JSONArray member = jo.getJSONArray("team");

                DummyItem item = new DummyItem(id, name, avatar, shortd, longd, owner, isActive);
                item.getMember(member);

                item.setFavourite(favourite.contains(id));

                ITEMS.add(item);
                ITEM_MAP.put(item.id, item);

            }catch (JSONException ex){
                Log.d("Content Construction", "BAD JSON");
            }
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String name;
        public final String content;
        public final String longcontent;
        public final boolean available;
        public final String owner;
        public final String avt;
        public Bitmap image;
        public boolean favourite;

        public ArrayList<Integer> member;

        public DummyItem(String id, String name, String avt, String content, String detail, String owner, boolean available) {
            this.id = id;
            this.name = name;
            this.avt = avt;
            this.content = content;
            this.longcontent = detail;// long description
            this.owner = owner;
            this.available = available;
            this.favourite = false;
            member = new ArrayList<Integer>();
        }

        public void getMember(JSONArray json){
            for (int i = 0; i < json.length(); i++){
                try {
                    member.add(json.getJSONObject(i).getInt("id"));
                }catch (JSONException e){}
            }
        }
        public void setImage(Bitmap original){
            image = original;
        }
        public void setFavourite(boolean t){
            this.favourite = t;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
