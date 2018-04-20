package authenticislam.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayz4sci.androidfactory.DownloadProgressView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import authenticislam.MainActivity;
import authenticislam.R;

/**
 * Created by abdoulbasith on 18/09/2016.
 */
public class ListAdapter extends BaseAdapter {
    private ArrayList listItemData;
    private LayoutInflater layoutInflater;
    private Resources res;
    private Context ctx;
    private Activity activity;
    private String dirName;

    public ListAdapter(Context context, String dirName,  ArrayList listItem, Resources res) {

        this.res = res;
        this.listItemData = listItem;
        this.dirName = dirName;
        layoutInflater = LayoutInflater.from(context);
        this.ctx = context;
        this.activity = (Activity) context;
    }

    @Override
    public int getCount() {
        return listItemData.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.list_image);
            viewHolder.name = (TextView) convertView.findViewById(R.id.list_title);
            viewHolder.author = (TextView) convertView.findViewById(R.id.list_author);
            viewHolder.publish = (TextView) convertView.findViewById(R.id.list_publish);
            viewHolder.lang = (TextView) convertView.findViewById(R.id.list_lang);
            viewHolder.download = (ImageView) convertView.findViewById(R.id.list_button_download);
            viewHolder.open = (ImageView) convertView.findViewById(R.id.list_button_open);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.list_button_delete);
            viewHolder.downloadProgressView = (DownloadProgressView) convertView.findViewById(R.id.downloadProgressView);

            convertView.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();




        if(position%2==0)
            viewHolder.layout.setBackgroundColor(ctx.getResources().getColor(R.color.colorListOdd));
        else
            viewHolder.layout.setBackgroundColor(ctx.getResources().getColor(R.color.colorListEven));

        final ListItem listItem = (ListItem) listItemData.get(position);


        Picasso.with(ctx)
                .load(listItem.getType().equals("Book") ? res.getIdentifier("note", "mipmap", MainActivity.PACKAGE_NAME) : res.getIdentifier("casette", "mipmap", MainActivity.PACKAGE_NAME))
                .resize(200, 200)
                .into(viewHolder.cover);



        /*if(!listItem.getAuthor().equals(""))
            viewHolder.author.setText(listItem.getAuthor());
        else
            viewHolder.author.setVisibility(View.GONE);

        if(!listItem.getPublish().equals(""))
            viewHolder.publish.setText(listItem.getPublish());
        else
            viewHolder.publish.setVisibility(View.GONE);*/

        viewHolder.name.setText(listItem.getName());
        viewHolder.author.setText(listItem.getAuthor());
        viewHolder.publish.setText(listItem.getPublish());
        viewHolder.lang.setText(listItem.getLang());


        if(fileExist(listItem.getName(), listItem.getType())){
            viewHolder.download.setVisibility(View.GONE);
            viewHolder.open.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.VISIBLE);

            viewHolder.open.setImageResource(listItem.getType().equals("Book") ? R.mipmap.ic_insert_drive_file_black_24dp : R.mipmap.ic_play_circle_filled_black_24dp);

        } else {
            viewHolder.download.setVisibility(View.VISIBLE);
            viewHolder.open.setVisibility(View.GONE);
            viewHolder.delete.setVisibility(View.GONE);
        }

        final View finalConvertView = convertView;

        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(listItem.getName(), listItem.getType(), listItem.getLink(), viewHolder.download, viewHolder.open, viewHolder.delete, finalConvertView, viewHolder.downloadProgressView);
            }
        });

        viewHolder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(listItem.getName(), listItem.getType());
            }
        });


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.AlertDialogCustom));
                alert.setTitle(ctx.getString(R.string.txt_delete_file) + " " +listItem.getName() + " ?");

                alert.setPositiveButton(ctx.getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new File(MainActivity.PATH + dirName + "/" + listItem.getName() + (listItem.getType().equals("Book") ? ".pdf" : ".mp3")).delete();
                        Snackbar.make(finalConvertView, listItem.getName()+" deleted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton(ctx.getString(R.string.txt_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });

        return convertView;
    }


    public void download(final String name, String type, String link, final ImageView download, final ImageView open, final ImageView delete, final View convertView, final DownloadProgressView downloadProgressView){

        long downloadID;

        DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));

        File directory = new File(MainActivity.PATH + dirName);

        if(!directory.exists())
            directory.mkdirs();

        File file = new File(MainActivity.PATH + dirName + "/" + name + (type.equals("Book") ? ".pdf" : ".mp3"));

        request.setTitle(name);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(file));

        downloadID = downloadManager.enqueue(request);

        downloadProgressView.show(downloadID, new DownloadProgressView.DownloadStatusListener() {
            @Override
            public void downloadFailed(int reason) {
                Snackbar.make(activity.findViewById(R.id.frame), "Download Failed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void downloadSuccessful() {
                download.setVisibility(View.GONE);
                open.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);

                Snackbar.make(activity.findViewById(R.id.frame), name+" downloaded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                notifyDataSetChanged();
            }

            @Override
            public void downloadCancelled() {
                Snackbar.make(activity.findViewById(R.id.frame), "Download Canceled", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    public boolean fileExist(String name, String type){

        String filepath = MainActivity.PATH + dirName + "/" + name +  (type.equals("Book") ? ".pdf" : ".mp3");

        return new File(filepath.trim()).exists();
    }

    public void openFile(String name, String type){


        String filepath = MainActivity.PATH + dirName + "/" + name +  (type.equals("Book") ? ".pdf" : ".mp3");


        File file = new File(filepath.trim());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), (type.equals("Book") ? "application/pdf" : "audio/*"));
        ctx.startActivity(intent);

    }



    static class ViewHolder{
        TextView name, author, publish, lang;
        RelativeLayout layout;
        ImageView cover ;
        ImageView download, open, delete;
        DownloadProgressView downloadProgressView;
    }

    @Override
    public String toString() {
        return "ListAdapter{" +
                "listItemData=" + listItemData +
                '}';
    }
}
