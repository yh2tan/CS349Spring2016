package yh2tan.crowdcurio;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import yh2tan.crowdcurio.dummy.CurioContent;
import yh2tan.crowdcurio.dummy.DummyContent;
import yh2tan.crowdcurio.dummy.MemberContent;

/**
 * An activity representing a single Project detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ProjectListActivity}.
 */
public class ProjectDetailActivity extends AppCompatActivity
        implements CurioFragment.OnListFragmentInteractionListener,
                   MemberFragment.OnListFragmentInteractionListener{

    ViewPager viewPager;
    DescriptionFragment description;
    CurioFragment curioList;
    MemberFragment members;

    DummyContent.DummyItem mItem;
    FloatingActionButton fab;
    ActionBar actionBar;

    AppBarLayout appbar;
    CollapsingToolbarLayout ctbl;

    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        spinner = (ProgressBar) findViewById(R.id.pb2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curioList.refetch();
                members.refetch();
            }
        });

        // Show the Up button in the action bar.
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mItem = DummyContent.ITEM_MAP.get(getIntent().getStringExtra(ProjectDetailFragment.ARG_ITEM_ID));
        // Create the detail fragment and add it to the activity
        appbar = (AppBarLayout)findViewById(R.id.app_bar);
        appbar.setBackground(new BitmapDrawable(mItem.image));

        ctbl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctbl.setTitle(mItem.name);

        viewPager = (ViewPager) findViewById(R.id.detailTab);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        if (savedInstanceState == null) {

            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString("ID", getIntent().getStringExtra(ProjectDetailFragment.ARG_ITEM_ID));

            description = new DescriptionFragment();
            curioList = new CurioFragment();
            members = new MemberFragment();
            curioList.forcePass(getIntent().getStringExtra(ProjectDetailFragment.ARG_ITEM_ID));
            members.forcePass(getIntent().getStringExtra(ProjectDetailFragment.ARG_ITEM_ID));
            description.setArguments(arguments);
            curioList.setArguments(arguments);
            members.setArguments(arguments);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ProjectListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onListFragmentInteraction(CurioContent.CurioItem item) {}
    public void onListFragmentInteraction(MemberContent.MemberItem item) {}

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        public final String[] titles= {"Description", "Questions", "Team"};

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return description;
            }else if (position == 1){
                return curioList;
            }else if (position == 2){
                return members;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return titles[position];
        }
    }
}
