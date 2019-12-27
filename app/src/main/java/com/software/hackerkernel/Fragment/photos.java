package com.software.hackerkernel.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.software.hackerkernel.Activities.LoginActivity;
import com.software.hackerkernel.Adapter.PhotoAdapter;
import com.software.hackerkernel.Adapter.RecyclerTouchListener;
import com.software.hackerkernel.Model.PhotoModel;
import com.software.hackerkernel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class photos extends Fragment {
    private RecyclerView recyclerViewphotos;
    private List<PhotoModel> photoModelArrayList= new ArrayList<>();
    private RelativeLayout no_result_found;
    private ProgressDialog loading;
    public photos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_photos, container, false);
        recyclerViewphotos=view.findViewById(R.id.recyclerViewphotos);
        no_result_found=view.findViewById(R.id.no_result_found);
        recyclerViewphotos.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerViewphotos.setLayoutManager(manager);
        recyclerViewphotos.setNestedScrollingEnabled(false);
        if(checkConnectivity()==1){
            getallphotos();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.linearLayout_photos), "please Check Internet Connection...", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        return  view;
    }

    private void getallphotos() {
        recyclerViewphotos.setVisibility(View.VISIBLE);

        no_result_found.setVisibility(View.GONE);
        loading = ProgressDialog.show(getContext(), "Please Wait", "Please Wait", false, false);

        String URL_PHOTOS = "https://jsonplaceholder.typicode.com/photos";
        StringRequest stringRequest = new StringRequest(URL_PHOTOS,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject photo_object = array.getJSONObject(i);
                                photoModelArrayList.add(new PhotoModel(
                                        photo_object.getString("albumId"),
                                        photo_object.getString("id"),
                                        photo_object.getString("title"),
                                        photo_object.getString("url"),
                                        photo_object.getString("thumbnailUrl")
                                ));

                            }

                            //creating adapter object and setting it to recyclerview
                            PhotoAdapter adapter = new PhotoAdapter(getContext(), photoModelArrayList);
                            recyclerViewphotos.setAdapter(adapter);
                            if(recyclerViewphotos.getAdapter().getItemCount()==0)
                            {
                                recyclerViewphotos.setVisibility(View.GONE);

                                no_result_found.setVisibility(View.VISIBLE);

                            }
                            loading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext(),new HurlStack());
        requestQueue.add(stringRequest).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

    }
    private int checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        int internet;
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            internet = 0;
        } else {
            internet = 1;
        }
        return internet;
    }
}
