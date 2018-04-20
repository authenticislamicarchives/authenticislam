package authenticislam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import authenticislam.MainActivity;
import authenticislam.R;
import authenticislam.view.ListAdapter;
import authenticislam.view.ListItem;

/**
 * Created by abdoulbasith on 23/04/2016.
 */
public class HadithFragment extends android.support.v4.app.Fragment {

    protected ArrayList<ListItem> hadithListItem;
    protected ListAdapter hadithListAdapter;
    protected ListView hadithListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);

        MainActivity.toolbar.setTitle(getString(R.string.menu_hadith));

        hadithListView = (ListView) view.findViewById(R.id.fragment_list);

        hadithListItem = getListData();

        hadithListAdapter = new ListAdapter(getActivity(), getString(R.string.menu_hadith), hadithListItem, getResources());

        hadithListView.setAdapter(hadithListAdapter);

        return view;
    }

    private ArrayList<ListItem> getListData(){

        ArrayList<ListItem> listData = new ArrayList<>();
        String[] name = getResources().getStringArray(R.array.HadithBooksNameArray);
        String[] author = getResources().getStringArray(R.array.HadithAuthorsNameArray);
        String[] publish = getResources().getStringArray(R.array.HadithPublishingHouseNameArray);
        String[] link = getResources().getStringArray(R.array.HadithLinksArray);
        String[] lang = getResources().getStringArray(R.array.HadithLanguageArray);
        String[] type = getResources().getStringArray(R.array.HadithTypeArray);

        for(int i=0; i < name.length; i++){
            ListItem mediaData = new ListItem();
            mediaData.setName(name[i]);
            mediaData.setAuthor(author[i]);
            mediaData.setPublish(publish[i]);
            mediaData.setLink(link[i]);
            mediaData.setLang(lang[i]);
            mediaData.setType(type[i]);

            listData.add(mediaData);
        }

        return listData;
    }
}
