package com.exerciseapp.mattiapalmas.giphysampleapp.Presenters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.exerciseapp.mattiapalmas.giphysampleapp.Modules.GiphyModule;
import com.exerciseapp.mattiapalmas.giphysampleapp.R;
import com.exerciseapp.mattiapalmas.giphysampleapp.Views.MainActivity;

import java.util.List;

/**
 * Created by mattia palmas on 2018-01-19.
 */

public class AdaptorRecyclerViewFavourite extends RecyclerView.Adapter<AdaptorRecyclerViewFavourite.ViewHolder> {

    List<GiphyModule> giphy;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pref_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    public AdaptorRecyclerViewFavourite(List<GiphyModule> giphy, Context context) {
        this.giphy = giphy;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GiphyModule listItem = giphy.get(position);


        String x = "<!DOCTYPE html><html><body><img src=\""+ listItem.getGifUrl() +"\" width=\"250px\" height=\"150px\"></body></html>";
        holder.git_pref_image_view.loadData(x, "text/html", "utf-8");

        holder.git_pref_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("You want to delete it from favourites?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listItem.setFavourite(false);
                        MainActivity.favouriteGiphyList.remove(listItem);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                AlertDialog dialog = builder.create();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (giphy == null){
            return 0;
        } else {
            return giphy.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private WebView git_pref_image_view;

        public ViewHolder(View itemView) {
            super(itemView);
            git_pref_image_view = itemView.findViewById(R.id.git_pref_image_view);
        }
    }
}