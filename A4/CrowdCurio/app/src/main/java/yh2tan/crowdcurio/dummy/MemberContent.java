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
public class MemberContent{

    static String tag = "Content Construction";

    /**
     * An array of sample (dummy) items.
     */
    public static List<MemberItem> ITEMS = new ArrayList<MemberItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, MemberItem> ITEM_MAP = new HashMap<String, MemberItem>();
    private static int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static ArrayList<String> favourite = new ArrayList<String>();

    private static void addItem(MemberItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    // Only used for creating examples
    private static MemberItem createDummyItem(int position) {
        return new MemberItem(String.valueOf(position), "Item " + position, " ", makeDetails(position));
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
        ArrayList<Integer> members = DummyContent.ITEM_MAP.get(id).member;

        Log.d("MEM",json.toString());
        for (int i = 0; i < json.length() ; i++){
            try{
                JSONObject jso = json.getJSONObject(i);

                int memid = jso.getInt("id");

                if (members.contains(memid)){
                    String thisid = String.valueOf(memid);
                    String nickname = jso.getString("nickname");
                    String email = jso.getString("email");
                    String avatar = jso.getString("avatar");

                    MemberItem mitem = new MemberItem(thisid,email, nickname, avatar);
                    ITEMS.add(mitem);
                    ITEM_MAP.put(thisid, mitem);
                }

                COUNT++;
            }catch (JSONException e){
                Log.d("Curio", "Bad JSON");
            }
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class MemberItem {
        public final String id;
        public final String nickname;
        public final String email;
        public final String avatar;

        public MemberItem(String id, String e, String n, String a) {
            this.id = id;
            nickname = n;
            email = e;
            avatar = a;
        }

        @Override
        public String toString() {
            return email;
        }
    }
}
