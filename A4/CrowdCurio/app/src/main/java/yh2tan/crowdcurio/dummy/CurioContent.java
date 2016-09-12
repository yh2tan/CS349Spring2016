package yh2tan.crowdcurio.dummy;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ED on 2016/07/25.
 */
public class CurioContent{

    static String tag = "Content Construction";

    /**
     * An array of sample (dummy) items.
     */
    public static List<CurioItem> ITEMS = new ArrayList<CurioItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, CurioItem> ITEM_MAP = new HashMap<String, CurioItem>();
    private static int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static ArrayList<String> favourite = new ArrayList<String>();

    private static void addItem(CurioItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    // Only used for creating examples
    private static CurioItem createDummyItem(int position) {
        return new CurioItem(String.valueOf(position), "Item " + position, makeDetails(position));
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

    public static void constructContent(JSONArray json, String id){
        ITEMS.clear();
        ITEM_MAP.clear();

        COUNT = 0;

        for (int i = 0; i < json.length() ; i++){
            try{
                JSONObject jso = json.getJSONObject(i);

                int proj = jso.getInt("project");

                //Log.d("",jso.toString());

                if (proj == Integer.parseInt(id)){
                    String question = jso.getString("question");
                    String motive = jso.getString("motivation");

                    CurioItem citem = new CurioItem(String.valueOf(id+1),question,motive);
                    ITEMS.add(citem);
                    ITEM_MAP.put(String.valueOf(id+1),citem);
                }
            }catch (JSONException e){
                Log.d("Curio", "Bad JSON");
            }
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class CurioItem {
        public final String id;
        public final String question;
        public final String motive;

        public CurioItem(String id, String q, String m) {
            this.id = id;
            question = q;
            motive = m;
        }

        @Override
        public String toString() {
            return question;
        }
    }
}
