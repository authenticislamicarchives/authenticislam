package authenticislam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by abdoulbasith on 30/04/2016.
 */
public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    protected TextView splashVerseAr, splashVerseEn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                finish();
            }
        }, SPLASH_TIME_OUT);

        splashVerseAr = (TextView) findViewById(R.id.splash_verse_ar);
        splashVerseEn = (TextView) findViewById(R.id.splash_verse_en);

        randomVerse();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void randomVerse(){
        String[] verseAr = getResources().getStringArray(R.array.splash_screen_verses_ar);
        String[] verseEn = getResources().getStringArray(R.array.splash_screen_verses_en);

        int rand = new Random().nextInt(verseAr.length);

        splashVerseAr.setText(verseAr[rand]);
        splashVerseEn.setText(verseEn[rand]);


    }
}
