package com.kuruvatech.EduG.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.kuruvatech.EduG.R;
import com.kuruvatech.EduG.adapter.YoutubeRecyclerAdapter;
import com.kuruvatech.EduG.model.FeedItem;
import com.kuruvatech.EduG.utils.Constants;
import com.kuruvatech.EduG.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;


public class VideoFragment extends Fragment {
    private static final String TAG_HEADING = "heading";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_FEEDIMAGES = "feedimages";
    private static final String TAG_VIDEO = "feedvideo";
    private static final String TAG_FEEDVIDEOS = "feedvideos";
    private static final String TAG_FEEDAUDIOS = "feedaudios";
    private static final String TAG_URL = "url";
  //  RecyclerView recyclerView;
  //  Adapter adapter;
    boolean isSwipeRefresh =true;
    ArrayList<FeedItem> feedList;

    RecyclerView recyclerView;
    YoutubeRecyclerAdapter adapter;
    SessionManager session;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment_item_list, container, false);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView=(RecyclerView)view.findViewById(R.id.video_list);
        recyclerView.setHasFixedSize(true);
        session = new SessionManager(getActivity().getApplicationContext());

//        mAdView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
    //    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        isSwipeRefresh = false;
        feedList = new ArrayList<FeedItem>();
        adapter=new YoutubeRecyclerAdapter(getContext(),feedList);
        recyclerView.setAdapter(adapter);
        initfromsession();
        getVideos();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isSwipeRefresh = true;
               // getVideos();
            }

        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);
        swipeRefreshLayout.setProgressBackgroundColor(android.R.color.transparent);
        return view;
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    public void initfromsession()
    {
        String data  = session.getLastVideoFeed();

        if(data == null)
        {
            return;
        }

        try
        {
            JSONArray feedsarray = new JSONArray(data);
            ArrayList<FeedItem> lfeedList = new ArrayList<FeedItem>();
            for (int i = 0; i < feedsarray.length(); i++) {
                JSONObject feed_object = feedsarray.getJSONObject(i);
                FeedItem feedItem = new FeedItem();
                if (feed_object.has(TAG_HEADING)) {
                    feedItem.setHeading(feed_object.getString(TAG_HEADING));
                }
                if (feed_object.has(TAG_VIDEO)) {
                    feedItem.setVideoid(feed_object.getString(TAG_VIDEO));
                }

                        if (feed_object.has(TAG_DESCRIPTION))

                            feedItem.setDescription(TextUtils.htmlEncode(feed_object.getString(TAG_DESCRIPTION)));
                        if (feed_object.has(TAG_FEEDVIDEOS)) {
                            JSONArray feedimagesarray = feed_object.getJSONArray(TAG_FEEDVIDEOS);
                            ArrayList<String> strList = new ArrayList<String>();
                            strList.clear();
                            for (int j = 0; j < feedimagesarray.length(); j++) {
                                JSONObject image_object = feedimagesarray.getJSONObject(j);
                                if (image_object.has(TAG_URL)) {
                                    strList.add(image_object.getString(TAG_URL));
                                }
                            }
                            feedItem.setFeedvideos(strList);

                        }
                        if (feed_object.has(TAG_FEEDAUDIOS)) {
                            JSONArray feedimagesarray = feed_object.getJSONArray(TAG_FEEDAUDIOS);
                            ArrayList<String> strList = new ArrayList<String>();
                            strList.clear();
                            for (int j = 0; j < feedimagesarray.length(); j++) {
                                JSONObject image_object = feedimagesarray.getJSONObject(j);
                                if (image_object.has(TAG_URL)) {
                                    strList.add(image_object.getString(TAG_URL));
                                }
                            }
                            feedItem.setFeedaudios(strList);

                }
                lfeedList.add(feedItem);
            }
            feedList.clear();
            feedList.addAll(lfeedList);
            initAdapter();
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void initAdapter()
    {
        // adapter = new FeedAdapter(getActivity(),R.layout.feeditem,feedList);
//        adapter = new Adapter(getActivity(),imageList);
//        recyclerView.setAdapter(adapter);


        adapter.notifyDataSetChanged();
//        if(imageList.size() > 0 ) {
//            noFeedstv.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
//            noFeedstv.setVisibility(View.VISIBLE);
//        }
    }
    public void getVideos()
    {
        String getFeedsUrl = Constants.GET_VIDEOS_URL;
        getFeedsUrl = getFeedsUrl +  Constants.USERNAME;
        new JSONAsyncTask().execute(getFeedsUrl);
    }


    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        public JSONAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isSwipeRefresh == false) {
                swipeRefreshLayout.setRefreshing(true);
//                dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.custom_progress_dialog);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                dialog.show();
//                dialog.setCancelable(true);
            }

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet request = new HttpGet(urls[0]);
//                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
//                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
//                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);

                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();



                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity,HTTP.UTF_8);
                    session.setLastVideoFeed(data);
                    JSONArray feedsarray = new JSONArray(data);
                    ArrayList<FeedItem> lfeedList = new ArrayList<FeedItem>();
                    for (int i = 0; i < feedsarray.length(); i++) {
                        JSONObject feed_object = feedsarray.getJSONObject(i);
                        FeedItem feedItem = new FeedItem();
                        if (feed_object.has(TAG_HEADING)) {
                            feedItem.setHeading(feed_object.getString(TAG_HEADING));
                        }
                        if (feed_object.has(TAG_VIDEO)) {
                            feedItem.setVideoid(feed_object.getString(TAG_VIDEO));
                        }

                        if (feed_object.has(TAG_DESCRIPTION))

                            feedItem.setDescription(TextUtils.htmlEncode(feed_object.getString(TAG_DESCRIPTION)));
                        if (feed_object.has(TAG_FEEDVIDEOS)) {
                            JSONArray feedimagesarray = feed_object.getJSONArray(TAG_FEEDVIDEOS);
                            ArrayList<String> strList = new ArrayList<String>();
                            strList.clear();
                            for (int j = 0; j < feedimagesarray.length(); j++) {
                                JSONObject image_object = feedimagesarray.getJSONObject(j);
                                if (image_object.has(TAG_URL)) {
                                    strList.add(image_object.getString(TAG_URL));
                                }
                            }
                            feedItem.setFeedvideos(strList);

                        }
                        if (feed_object.has(TAG_FEEDAUDIOS)) {
                            JSONArray feedimagesarray = feed_object.getJSONArray(TAG_FEEDAUDIOS);
                            ArrayList<String> strList = new ArrayList<String>();
                            strList.clear();
                            for (int j = 0; j < feedimagesarray.length(); j++) {
                                JSONObject image_object = feedimagesarray.getJSONObject(j);
                                if (image_object.has(TAG_URL)) {
                                    strList.add(image_object.getString(TAG_URL));
                                }
                            }
                            feedItem.setFeedaudios(strList);

                        }
                        lfeedList.add(feedItem);

                    }
                    feedList.clear();
                    feedList.addAll(lfeedList);
                    return true;
                }
            } catch (IOException e) {

                e.printStackTrace();

            }
            catch (Exception e) {

                e.printStackTrace();
            }

            return false;

        }
        protected void onPostExecute(Boolean result) {
            if(swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
            isSwipeRefresh = false;
            if(getActivity() != null) {
                if (result == false) {

                    // Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
                    alertMessage("Unable to fetch data from server");
                } else {
                    initAdapter();

                }
            }

        }
    }
    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about).show();

    }
    private static class CustomSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        @Override
        public int getSpanSize(int i) {

//            if(i == 0 || i == 1) {
//                // grid items on positions 0 and 1 will occupy 2 spans of the grid
//                return 2;
//            } else {
//                // the rest of the items will behave normally and occupy only 1 span
            return 1;
//            }
        }
    }


}
