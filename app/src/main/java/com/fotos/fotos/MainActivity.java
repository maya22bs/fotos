package com.fotos.fotos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.fotos.fotos.cardHandling.Card;
import com.fotos.fotos.cardHandling.CardViewAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fotos.fotos.facebookAccess.FacebookData;
import com.fotos.fotos.facebookAccess.FacebookDataAsyncResponse;
import com.fotos.fotos.facebookAccess.Friend;
import com.fotos.fotos.facebookAccess.Photo;
import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, FacebookDataAsyncResponse {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    // :(
    private static Context context;

    // :( :(
    private static CardViewAdapter adapter = null;

    private FacebookData fbAccess = new FacebookData();
    // Holds friend list
    private List<Friend> friendList = null;

    // for debug
    private static final String TAG = "FBLogin";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init FB SDK, must do this before anything else
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_main);
        //Parse.enableLocalDatastore(this);

        //Parse.initialize(this, "qPRNUtum7ZZFN5MN2y", "aKYxLCpcwJiQ4RXOX8kTDG0tUmNvSlvwBC8eBZQo");
        //String userId="";
        // this.updateDB(userId);
        //ParseObject testObject = new ParseObject("mayabs");
        // testObject.put("facebookkkkid", "maya052");
        //testObject.saveInBackground();

        // :( X 3
        context = getApplicationContext();


        // color notification bar
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(Color.parseColor("#485678"));

        // action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        fbAccess.delegate = this;

        fbAccess.GetFriends();
        Profile currentUser = Profile.getCurrentProfile();
        String name = (currentUser == null) ? "Yoad Atzmoni" : currentUser.getName();
        fbAccess.GetUserPhotos(AccessToken.getCurrentAccessToken().getUserId(), name);
    }

    private void updateDB(String id) {
        ParseObject updateObject = new ParseObject("loginObject");
        updateObject.put("facebookId", id);
        updateObject.saveInBackground();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void GetFriendListResponce(List<Friend> friendList) {
        this.friendList = friendList;

        // Go over list, for each friend get Photos
        for (int i=0; i<friendList.size(); i++) {
            fbAccess.GetUserPhotos(friendList.get(i).getId(), friendList.get(i).getName());
        }

        // Woohoo !
        // SystemClock.sleep(3000);

        Log.d(TAG, "Got Friend list !");
    }

    @Override
    public void GetUserPhotosResponse(String id, String name, List<Photo> friendList) {
        // TODO: Finish this !
        Log.d(TAG, "Got Photos !");

        int picturesSize = (friendList.size() > 2) ? 2 : friendList.size();

        for (int i = 0; i < picturesSize; i++) {
            LoadPhotoAsync task = new LoadPhotoAsync();
            task.execute(new String[] { name, friendList.get(i).getUrl(), friendList.get(i).getLocation() });
        }


//        LoadPhotoAsync task = new LoadPhotoAsync();
//        task.execute(new String[] { name, "https://scontent.xx.fbcdn.net/hphotos-xpa1/t31.0-8/s720x720/241039_10152849403198250_6204450900998303748_o.jpg" });

        //Bitmap x = drawable_from_url("https://scontent.xx.fbcdn.net/hphotos-xpa1/t31.0-8/s720x720/241039_10152849403198250_6204450900998303748_o.jpg");
    }

    private class LoadPhotoAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Bitmap x = drawable_from_url(params[1]);

            Card card = new Card(x, params[0], "Where was this taken?", params[2], false, "sponsor");
            adapter.add_card(card);

            return null;
        }

        private Bitmap drawable_from_url(String url)  {
            Bitmap x;

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL(url) .openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setRequestProperty("User-agent", "Mozilla/4.0");

            InputStream input = null;
            try {
                connection.connect();
                input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            x = BitmapFactory.decodeStream(input);
            return x;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private List<Card> cards = new ArrayList<>();

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            cards = initializeCards();
            adapter = new CardViewAdapter(cards);
            final RecyclerView cardViewer = (RecyclerView) rootView.findViewById(R.id.recycler_card_view);
            cardViewer.setHasFixedSize(true);
            cardViewer.setAdapter(adapter);

            cardViewer.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    adapter.handleAnswer(direction, getCustomAppContext(), viewHolder);
                    adapter.remove(viewHolder.getAdapterPosition());
                    adapter.onDetachedFromRecyclerView(cardViewer);
                }
            });
            itemTouchHelper.attachToRecyclerView(cardViewer);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private Bitmap drawable_from_url(String url)  {
            Bitmap x;

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL(url) .openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setRequestProperty("User-agent", "Mozilla/4.0");

            InputStream input = null;
            try {
                connection.connect();
                input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            x = BitmapFactory.decodeStream(input);
            return x;
        }


        public List<Card> initializeCards() {
            List<Card> cards = new ArrayList<>();
            cards.add(new Card(R.drawable.camp, "Amanda Johnson", "Where was this taken?", "Crystal Falls State Forest, Michigan", "Iron Mountain, Michigan", false, "sponsor"));
            cards.add(new Card(R.drawable.beach, "Marc Cohen", "Where was this taken?", "Whitehaven Beach, Australia", "Fort Lauderdale, Florida", false, "sponsor"));
            cards.add(new Card(R.drawable.mcdonalds, "David Peters", "Check out David and Maya at McDonald’s!", "option1", "FIND A MCDONALD’S NEAR YOU!", true, "mcdonalds"));
            cards.add(new Card(R.drawable.selfie, "Karen Williams", "Who else is here with Karen?", "Marc Cohen", "Diana Charleston", false, "sponsor"));
            cards.add(new Card(R.drawable.starbucks, "Li Chang", "Li looks awesome in Starbucks at Stanford!",  "option1","FIND A STARBUCKS NEAR YOU!", true, "starbucks"));
            cards.add(new Card(R.drawable.club, "Henry Ruth", "Where was this taken?", "Harvey's Comedy Club, Portland", "Oregon Convention Center, Portland", false, "sponsor"));
            cards.add(new Card(R.drawable.concert, "Marc Cohen", "SO COOL! Marc saw Imagine Dragons LIVE!", "option1", "FIND TICKETS TOO!", true, "ticketmaster"));
            cards.add(new Card(R.drawable.louvre, "Daniel Silberberg", "Where was this taken?", "The Eiffel Tower, Paris", "The Louvre, Paris", false, "sponsor"));
            return cards;
        }

        public static Context getCustomAppContext(){
            return context;
        }
    }
}


