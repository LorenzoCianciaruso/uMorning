package com.example.umorning.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.umorning.R;
import com.example.umorning.model.Badge;
import com.example.umorning.model.DatabaseHelper;

import java.util.List;

public class BadgeAchievementsActivity extends Activity {

    private ImageView[] imageViews = new ImageView[12];
    private TextView[] textViews = new TextView[12];
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_achievements);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("Achievements");

        db = new DatabaseHelper(this);

        aquireImageView();
        aquireTextView();

        //ricevo lista dei badge
        final List<Badge> badges = db.getAllBadges();

        //setto badgeView e imageView in base al badge
        for(int i = 0; i < badges.size(); i++ ){
            if( badges.get(i).isAquired()){
                imageViews[i].setImageResource(badges.get(i).getIconAcquired());
            }else{
                imageViews[i].setImageResource(badges.get(i).getIconPending());
            }
            textViews[i].setText(badges.get(i).getName());

            final int finalI = i;
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(BadgeAchievementsActivity.this).create();
                    alertDialog.setTitle(badges.get(finalI).getName());
                    alertDialog.setMessage(badges.get(finalI).getDescription());
                    alertDialog.show();
                }
            });
        }

    }

    private void aquireTextView(){
        textViews[0] = (TextView) findViewById(R.id.text1);
        textViews[1] = (TextView) findViewById(R.id.text2);
        textViews[2] = (TextView) findViewById(R.id.text3);
        textViews[3] = (TextView) findViewById(R.id.text4);
        textViews[4] = (TextView) findViewById(R.id.text5);
        textViews[5] = (TextView) findViewById(R.id.text6);
        textViews[6] = (TextView) findViewById(R.id.text7);
        textViews[7] = (TextView) findViewById(R.id.text8);
        textViews[8] = (TextView) findViewById(R.id.text9);
        textViews[9] = (TextView) findViewById(R.id.text10);
        textViews[10] = (TextView) findViewById(R.id.text11);
        textViews[11] = (TextView) findViewById(R.id.text12);
    }

    private void aquireImageView(){
        imageViews[0] = (ImageView) findViewById(R.id.badge1);
        imageViews[1] = (ImageView) findViewById(R.id.badge2);
        imageViews[2] = (ImageView) findViewById(R.id.badge3);
        imageViews[3] = (ImageView) findViewById(R.id.badge4);
        imageViews[4] = (ImageView) findViewById(R.id.badge5);
        imageViews[5] = (ImageView) findViewById(R.id.badge6);
        imageViews[6] = (ImageView) findViewById(R.id.badge7);
        imageViews[7] = (ImageView) findViewById(R.id.badge8);
        imageViews[8] = (ImageView) findViewById(R.id.badge9);
        imageViews[9] = (ImageView) findViewById(R.id.badge10);
        imageViews[10] = (ImageView) findViewById(R.id.badge11);
        imageViews[11] = (ImageView) findViewById(R.id.badge12);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
