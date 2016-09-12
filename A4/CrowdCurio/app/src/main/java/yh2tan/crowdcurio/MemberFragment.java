package yh2tan.crowdcurio;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import yh2tan.crowdcurio.dummy.CurioContent;
import yh2tan.crowdcurio.dummy.MemberContent.MemberItem;
import yh2tan.crowdcurio.dummy.MemberContent;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

public class MemberFragment extends Fragment{

    // TODO: Customize parameters
    private int mColumnCount = 1;

    String memberID;
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    ProgressBar spinner;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MemberFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberID = getArguments().getString("ID");

        GetJson member = new GetJson();
        member.execute("http://test.crowdcurio.com/api/user/profile/");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyMemberRecyclerViewAdapter(MemberContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refetch(){
        GetJson member = new GetJson();
        member.execute("http://test.crowdcurio.com/api/user/profile/");
    }

    public void forcePass(String id){
        this.memberID = id;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener{
        // TODO: Update argument type and name
        void onListFragmentInteraction(MemberItem item);
    }


    class GetJson extends AsyncTask<String, Void, Void>{

        JSONObject json;
        String jsonReference;

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected Void doInBackground(String... string) {
            HttpURLConnection http = null;

            try{
                URL url = new URL(string[0]);
                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.setUseCaches(false);
                http.setConnectTimeout(5000); // connection timeout at 5 seconds
                http.setReadTimeout(5000); // read timeout at 5 second
                http.connect();
                int status = http.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();
                        try {
                            json = new JSONObject(sb.toString());
                            jsonReference = sb.toString();
                        }catch (Exception e){
                            Log.d("Backgournd Thread", "Unable to Parse JSONARRAY");
                        }
                }

            }catch (MalformedURLException ex){
                //okay....
            }catch (Exception ex){
                // okay....
            }finally{

                if (http != null){
                    try {
                        http.disconnect();
                    }catch (Exception e){
                        // okay ......
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void voids){
            try {
                JSONArray jarray = json.getJSONArray("results");
                MemberContent.constructContent(jarray, memberID);
            }catch (JSONException e) {

            }

            if (recyclerView != null)
                recyclerView.setAdapter(new MyMemberRecyclerViewAdapter(MemberContent.ITEMS, mListener));

        }
    }
}
