package com.teamsoftware.materialmusic;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MusicFragment extends Fragment implements Serializable {

    RecyclerView recyclerView;
    String baseDirectory;
    ArrayList<File> songList;
    SongManager songManager;
    ProgressDialog progressDialog;
    private static Context context;
    SongRecyclerAdapter songadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_music, container, false);

        context = getActivity();

        //songList updated again
        checkPermissions();

        init(rootview);

        songadapter = new SongRecyclerAdapter(context, songList, this);

        // Create your layout manager.
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);


        recyclerView.setAdapter(songadapter);

        //Start MediaPlayer
        //Send song position and songList
        songadapter.setOnItemClickListener(new SongRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT);
                //Intent intent = new Intent(context, /*Player class*/);
                //intent.putExtra("position", position);
                //startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        return rootview;
    }

    private void checkPermissions() {
        int permission1 = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission1 == PermissionChecker.PERMISSION_GRANTED) {
            //good to go
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // write logic here b'z it is called when fragment is visible to user
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void init(View v) {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        songManager = new SongManager();
        //Path to external storage directory, in this case SD card
        baseDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading songs...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Display the progress dialog, until all songs have been fetched
        while (!songManager.getFetchStatus()) {
            progressDialog.show();
            songList = songManager.findSongList(new File(baseDirectory));
        }
        if (songList != null) {
            progressDialog.dismiss();
        }
    }
}