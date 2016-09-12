package yh2tan.crowdcurio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;

import yh2tan.crowdcurio.dummy.DummyContent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * An activity representing a list of Projects. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProjectDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProjectListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    FloatingActionButton favourite;
    FloatingActionButton refresh;
    EditText entry;
    View recyclerView;
    ProgressBar spinner;
    Spinner searchType;

    Button delete;
    FetchData fetch;

    String current = "http://test.crowdcurio.com/api/project/";
    String search = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // request loading indicator
        setContentView(R.layout.activity_project_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        fetch = new FetchData();

        favourite = (FloatingActionButton) findViewById(R.id.favourite);
        refresh = (FloatingActionButton) findViewById(R.id.refreshmain);
        spinner = (ProgressBar) findViewById(R.id.pb1);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                entry.setText("");
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // == to be replaced by refresh
                FetchData newfetch = new FetchData();
                newfetch.execute(current);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // == to be replaced by favourite
                //FetchData newfetch = new FetchData();
                //newfetch.execute("http://test.crowdcurio.com/api/project/");
                DummyContent.filterFavourite();
                setupRecyclerView((RecyclerView) recyclerView);
            }
        });

        entry = (EditText) findViewById(R.id.searchentry);

        // search event
        entry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchkey = entry.getText().toString();

                    if (searchkey.compareTo("") == 0){
                        current = "http://test.crowdcurio.com/api/project/";
                    }else{
                        current = "http://test.crowdcurio.com/api/project/?"+search+"="+searchkey;
                    }

                    FetchData newfetch = new FetchData();
                    newfetch.execute(current);

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(entry.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.project_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        // Spinner
        searchType = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.searchType, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        searchType.setAdapter(adapter);
        searchType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String item = parent.getItemAtPosition(i).toString();
                search = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0){
                search = "name";
            }
        });

        if (findViewById(R.id.project_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        fetch.execute("http://test.crowdcurio.com/api/project/");
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mOwner.setText("Owner: "+mValues.get(position).owner);
            holder.mContentView.setText(mValues.get(position).name);


            //load image
            DownloadImage dl = new DownloadImage(holder.mAvatar, mValues.get(position));
            dl.execute(mValues.get(position).avt);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mValues.get(position).available){
                        Context context = getApplicationContext();
                        CharSequence text = "This content is currently not avaliable, stay tune!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }else {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putString(ProjectDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                            ProjectDetailFragment fragment = new ProjectDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                .replace(R.id.project_detail_container, fragment)
                                .commit();
                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, ProjectDetailActivity.class);
                            intent.putExtra(ProjectDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                            context.startActivity(intent);
                        }
                    }
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {

                    if (holder.mItem.favourite){
                        DummyContent.removeFavourite(holder.mItem.id);
                        Context context = getApplicationContext();
                        CharSequence text = "Project has been removed from favourite";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return true;
                    }else{
                        DummyContent.addFavourite(holder.mItem.id);
                        Context context = getApplicationContext();
                        CharSequence text = "Project has been added from favourite";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return true;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mAvatar;
            public final TextView mOwner;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mAvatar = (ImageView) view.findViewById(R.id.avatar);
                mOwner = (TextView) view.findViewById(R.id.owner);
                mContentView = (TextView) view.findViewById(R.id.title);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    class FetchData extends AsyncTask<String, Void, Void>{

        JSONArray jsonArray;
        String jsonReference;

        @Override
        protected void onPreExecute(){
            Log.d("data fetch", "Spinner Active");
            spinner.setVisibility(View.VISIBLE);
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
                            jsonArray = new JSONArray(sb.toString());
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
            Log.d("fetch data", "Spinner Off");
            DummyContent.constructContent(jsonArray);
            setupRecyclerView((RecyclerView) recyclerView);
            spinner.setVisibility(View.GONE);
        }
    }

    class DownloadImage extends AsyncTask<String,Void, Bitmap>{

        final ImageView imageview;
        DummyContent.DummyItem it;

        public DownloadImage(ImageView iv, DummyContent.DummyItem it){
            imageview = iv;
            this.it = it;
        }

        @Override
        public void onPreExecute(){
            // load slider
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        public Bitmap doInBackground(String... strings){
            try{
                URL urlconnection = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) urlconnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(input);

                return image;
            }catch (Exception e){
                Log.d("fetch image", "Something is wrong");
            }
            return null;
        }

        @Override
        public void onPostExecute(Bitmap result){
            // unload slider
            spinner.setVisibility(View.GONE);

            // SetImage
            imageview.setImageBitmap(getCroppedBitmap(result));
            it.setImage(result);
        }
    }

    //circular cropping
    public Bitmap getCroppedBitmap(Bitmap bitmap) {

        if (bitmap == null){
            return bitmap;
        }

        int min = bitmap.getHeight() > bitmap.getWidth() ? bitmap.getWidth() : bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, min, min);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}


