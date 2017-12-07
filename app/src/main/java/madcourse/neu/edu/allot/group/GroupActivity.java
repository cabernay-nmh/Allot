package madcourse.neu.edu.allot.group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import madcourse.neu.edu.allot.LoginActivity;
import madcourse.neu.edu.allot.R;
import madcourse.neu.edu.allot.blackbox.handlers.FetchGroupsHandler;
import madcourse.neu.edu.allot.blackbox.models.Group;
import madcourse.neu.edu.allot.blackbox.models.User;
import madcourse.neu.edu.allot.blackbox.responders.FetchGroupsResponder;
import madcourse.neu.edu.allot.place.CardAdapter;
import madcourse.neu.edu.allot.place.PlaceActivity;

public class GroupActivity extends AppCompatActivity implements FetchGroupsResponder {

    private ListView groupList;
    private CardAdapter cardAdapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    TextView userNameMenu;
    Button settingsButton;
    Button logout;
    Button groupsButton;

    FloatingActionButton fabplus;
    FloatingActionButton fabCreateGroup;
    FloatingActionButton fabJoinGroup;
    Animation fabOpen, fabClose, fabRotateClockwise, fabRotateAntiClockwise;
    TextView fabCreateGroupText, fabJoinGroupText;

    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        getNevigationBar();


        /**
         * floating action bar
         */
        fabplus = (FloatingActionButton) findViewById(R.id.fab);
        fabCreateGroup = (FloatingActionButton) findViewById(R.id.createGoupFab);
        fabJoinGroup = (FloatingActionButton) findViewById(R.id.joinGroupFab);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabRotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_clockwise);
        fabRotateAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_anticlockwise);

        fabCreateGroupText =(TextView) findViewById(R.id.createGroupTextfab);
        fabJoinGroupText =(TextView) findViewById(R.id.joinGroupTextfab);


        fabplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpen){

                    fabCreateGroup.startAnimation(fabClose);
                    fabJoinGroup.startAnimation(fabClose);
                    fabJoinGroupText.startAnimation(fabClose);
                    fabCreateGroupText.startAnimation(fabClose);
                    fabplus.startAnimation(fabRotateAntiClockwise);
                    fabCreateGroup.setClickable(false);
                    fabJoinGroup.setClickable(false);
                    isOpen = false;

                }
                else{

                    fabCreateGroup.startAnimation(fabOpen);
                    fabJoinGroup.startAnimation(fabOpen);
                    fabJoinGroupText.startAnimation(fabOpen);
                    fabCreateGroupText.startAnimation(fabOpen);
                    fabplus.startAnimation(fabRotateClockwise);
                    fabCreateGroup.setClickable(true);
                    fabJoinGroup.setClickable(true);
                    isOpen = true;

                }
            }
        });

        fabCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateGroupActivity = new Intent(GroupActivity.this, CreateGroupActivity.class);
                startActivity(CreateGroupActivity);
            }
        });

        fabJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent JoinGroupActivity = new Intent(GroupActivity.this, JoinGroupActivity.class);
                startActivity(JoinGroupActivity);
            }
        });


        /**
         * User credentials.
         */
        SharedPreferences sharedPref = getSharedPreferences(User.SHARED_PREF_GROUP, MODE_PRIVATE);

        String userId = sharedPref.getString(User.SHARED_PREF_TAG_ID, "NA");
        String userToken = sharedPref.getString(User.SHARED_PREF_TAG_TOKEN, "NA");

        /**
         * Fetch Group List
         */

        FetchGroupsHandler.doFetch(this, userId, userToken);
    }

    public void getNevigationBar(){
        /**
         * Navigation bar
         */
        userNameMenu = (TextView) findViewById(R.id.userNameMenu);
        userNameMenu.setText("Prachi Sharma");

        settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setText("Settings");
        settingsButton.setTextColor(Color.BLACK);

        logout = (Button) findViewById(R.id.Logout);
        logout.setTextColor(Color.BLACK);

        groupsButton = (Button) findViewById(R.id.groupsButton);
        groupsButton.setTextColor(Color.BLACK);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccessfullGroupsFetch(List<Group> groups) {

        /**
         * Group List
         */
        ArrayList<String> testingGroupButtons = new ArrayList();

        for (Group group: groups) {
            Log.d("GroupCheck", group.getName());
            testingGroupButtons.add(group.getName());
        }

        cardAdapter = new CardAdapter(testingGroupButtons, getApplicationContext(),
                R.layout.card_group, PlaceActivity.class);
        groupList = (ListView) findViewById(R.id.list_groups);
        groupList.setAdapter(cardAdapter);
    }

    @Override
    public void onFailedGroupsFetch(String msg) {

    }
}
