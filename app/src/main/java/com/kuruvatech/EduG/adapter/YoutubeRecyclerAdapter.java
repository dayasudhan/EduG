package com.kuruvatech.EduG.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.kuruvatech.EduG.CustomMediaPlayer;
import com.kuruvatech.EduG.R;
import com.kuruvatech.EduG.model.FeedItem;

import java.util.ArrayList;

/**
 * Created by ofaroque on 8/13/15.
 */
public class YoutubeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //these ids are the unique id for each video
   // String[] VideoID = {"P3mAtvs5Elc", "nCgQDjiotG0", "P3mAtvs5Elc"};
//    String[] VideoID = {"jRc391TzTQU", "Wq11cKL5qnM","LQ7KKmc7Wcw", "KWLd6eYnSKI" ,
//            "WffZCNAM5tE","hJM4Fh1D6LE","JWn-44z4ayw","KpGyPb2nqh0",
//    "OYUtK9FGNpw","CNO19isWnxg","6JkY58joCGo","GlOPwJhAulc"};
    ArrayList<FeedItem> feedList;
    Context ctx;
    public static final String API_KEY = "AIzaSyBRLKO5KlEEgFjVgf4M-lZzeGXW94m9w3U";
    public


    YoutubeRecyclerAdapter(Context context , ArrayList<FeedItem> afeedList) {
        this.ctx = context;
        feedList = afeedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        switch(viewType)
        {
            case R.layout.videofragment_item:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videofragment_item, parent, false);
                holder = new VideoInfoHolder(view);
                break;
            case R.layout.list_item:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                holder = new YoutubeVideoInfoHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                holder = new YoutubeVideoInfoHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if(feedList.get(position).getFeedvideos().size() >  0 )
        {
            return R.layout.videofragment_item;
        }
        else if(!feedList.get(position).getVideoid().equals(""))
        {
            return R.layout.list_item;
        }
        else
        {
            return R.layout.videofragment_item;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            if(holder instanceof YoutubeVideoInfoHolder ) {
                final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }

                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        ((YoutubeVideoInfoHolder)holder).relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                    }
                };
                ((YoutubeVideoInfoHolder)holder).heading.setText(feedList.get(position).getHeading());
                ((YoutubeVideoInfoHolder)holder).youTubeThumbnailView.initialize(API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(feedList.get(position).getVideoid());
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                        //write something for failure
                    }
                });
            }
            else if(holder instanceof VideoInfoHolder)
            {
                ((VideoInfoHolder)holder).heading.setText(feedList.get(position).getHeading());
                ((VideoInfoHolder)holder).progressBar.setVisibility(View.VISIBLE);
                Uri video;
                if(feedList.get(position).getFeedvideos().size()>0 )
                    video = Uri.parse(feedList.get(position).getFeedvideos().get(0));
                else
                    video = Uri.parse(feedList.get(position).getFeedaudios().get(0));

                ((VideoInfoHolder)holder).videoview.setVideoURI(video);
                ((VideoInfoHolder)holder).videoview.requestFocus();

                ((VideoInfoHolder)holder).videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        ((VideoInfoHolder)holder).videoview.requestLayout();
//                                ((VideoInfoHolder)holder).pDialog.dismiss();
                        ((VideoInfoHolder)holder).progressBar.setVisibility(View.GONE);
                        ((VideoInfoHolder)holder).videoview.seekTo(100);
                         }
                   });
            }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }


    public class VideoInfoHolder extends RecyclerView.ViewHolder{
        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;

        protected ImageView shareButton;
        VideoView videoview;
        TextView heading;
        ProgressBar progressBar ;
//        ProgressDialog pDialog;
        ImageView playButton;
        public VideoInfoHolder(View itemView) {
            super(itemView);
            heading=(TextView)itemView.findViewById(R.id.video_heading);
            shareButton=(ImageView)itemView.findViewById(R.id.imagesharebutton);
            videoview = (VideoView) itemView.findViewById(R.id.VideoView);
            playButton = (ImageView) itemView.findViewById(R.id.btnvideo_player);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            shareButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent shareIntent = new Intent();
                    shareIntent.setType("text/html");
                    String youtube_link = "https://www.youtube.com/watch?v=";
                    youtube_link = youtube_link + feedList.get(getLayoutPosition()).getVideoid();

                    youtube_link = youtube_link  + "\n";
                    youtube_link = youtube_link + ctx.getString(R.string.invitelink);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,  "Subject");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, youtube_link);
                    shareIntent.setAction(Intent.ACTION_SEND);
                    ctx.startActivity(Intent.createChooser(shareIntent, "Share it ...."));
                }



            });
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, CustomMediaPlayer.class);
                    String video;
                    if(feedList.get(getLayoutPosition()).getFeedvideos().size()>0 )
                        video =feedList.get(getLayoutPosition()).getFeedvideos().get(0);
                    else
                        video = feedList.get(getLayoutPosition()).getFeedaudios().get(0);
                    intent.putExtra("url", video);

                    ctx.startActivity(intent);
                }
            });
        }
    }
    public class YoutubeVideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;
        protected ImageView shareButton;
        VideoView videoview;
        TextView heading;

        public YoutubeVideoInfoHolder(View itemView) {
            super(itemView);

            heading=(TextView)itemView.findViewById(R.id.video_heading);
            shareButton=(ImageView)itemView.findViewById(R.id.imagesharebutton);

            playButton = (ImageView) itemView.findViewById(R.id.btnYoutube_player);
            shareButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent shareIntent = new Intent();
                     shareIntent.setType("text/html");
                    String youtube_link = "https://www.youtube.com/watch?v=";
                    youtube_link = youtube_link + feedList.get(getLayoutPosition()).getVideoid();
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,  "Subject");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, youtube_link);
                    shareIntent.setAction(Intent.ACTION_SEND);
                    ctx.startActivity(Intent.createChooser(shareIntent, "Share it ...."));
                    }



            });
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
        }

        @Override
        public void onClick(View v) {

            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, API_KEY, feedList.get(getLayoutPosition()).getVideoid(),0,true,false);
            ctx.startActivity(intent);
        }
    }
}