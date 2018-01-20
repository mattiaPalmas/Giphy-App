package com.exerciseapp.mattiapalmas.giphysampleapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiphyFragment extends Fragment {

    private List<GiphyModule> listItems;
    private RecyclerView.Adapter adapter;
    private RecyclerView recycleView;
    private View view;
    private SearchView searchView;

    public GiphyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_giphy, container, false);

        loadRecyclerViewData();
        onSearchApply();
        return view;
    }


    // Adding giphy to recyclerview
    private void loadRecyclerViewData() {
        listItems = new ArrayList<>();
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadGiphyFromApi("http://api.giphy.com/v1/gifs/trending?api_key=RAIzgn1AXLAj6rQ57Sl97OwH92xh4s5f&limit=1000");
    }


    // On search Giphy clicked
    private void onSearchApply() {
        searchView = view.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadGiphyFromApi("http://api.giphy.com/v1/gifs/search?q="+ s +"&api_key=RAIzgn1AXLAj6rQ57Sl97OwH92xh4s5f&limit=1000");
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    // Calling Api to get Giphy
    private void loadGiphyFromApi(String request){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            listItems.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objData = array.getJSONObject(i);
                                JSONObject arrayImg = objData.optJSONObject("images");
                                JSONObject ogjImg = arrayImg.getJSONObject("fixed_height");
                                GiphyModule item = new GiphyModule(
                                        ogjImg.getString("url"),false
                                );
                                listItems.add(item);
                            }
                            adapter = new AdaptorRecycleViewGiphy(listItems,getActivity().getApplicationContext());
                            recycleView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "error connecting api: " +error);
                        Log.d("point", "point2332");
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        progressDialog.dismiss();
    }
}
