package authenticislam.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import authenticislam.MainActivity;
import authenticislam.R;

/**
 * Created by AbdoulBasith on 10/12/2014.
 */

public class AppRater {
    private final static String APP_TITLE = MainActivity.APP_NAME;
    private final static String APP_PNAME = MainActivity.PACKAGE_NAME;

    private final static int DAYS_UNTIL_PROMPT = 2;
    private final static int LAUNCHES_UNTIL_PROMPT = 5;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    //(DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext, R.style.AlertDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setTitle(mContext.getString(R.string.rate));

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10,10,10,10);

        TextView tvRating = new TextView(mContext);
        tvRating.setText(mContext.getString(R.string.txt_rate));
        tvRating.setPadding(5,5,5,5);
        tvRating.setTextColor(Color.WHITE);
        tvRating.setGravity(Gravity.CENTER);
        layout.addView(tvRating);

        Button buttonRate = new Button(mContext);
        buttonRate.setText(mContext.getString(R.string.rate));
        buttonRate.setTextColor(Color.DKGRAY);
        buttonRate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        layout.addView(buttonRate);

        Button buttonRemind = new Button(mContext);
        buttonRemind.setText(mContext.getString(R.string.rate_remind));
        buttonRemind.setTextColor(Color.DKGRAY);
        buttonRemind.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layout.addView(buttonRemind);

        Button buttonDeny = new Button(mContext);
        buttonDeny.setText(mContext.getString(R.string.rate_no));
        buttonDeny.setTextColor(Color.DKGRAY);
        buttonDeny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        layout.addView(buttonDeny);

        dialog.setContentView(layout);
        dialog.show();
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.ic_launcher);

    }
}