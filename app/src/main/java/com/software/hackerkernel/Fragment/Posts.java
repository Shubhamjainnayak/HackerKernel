package com.software.hackerkernel.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.software.hackerkernel.Adapter.PhotoAdapter;
import com.software.hackerkernel.Adapter.PostAdapter;
import com.software.hackerkernel.Adapter.RecyclerTouchListener;
import com.software.hackerkernel.Model.PhotoModel;
import com.software.hackerkernel.Model.PostModel;
import com.software.hackerkernel.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Posts extends Fragment {
    private RecyclerView recyclerViewposts;
    private List<PostModel> postModelArrayList= new ArrayList<>();
    private RelativeLayout no_result_found_post;
    private ProgressDialog loading;

    public Posts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_posts, container, false);
        recyclerViewposts=view.findViewById(R.id.recyclerViewposts);
        no_result_found_post=view.findViewById(R.id.no_result_found_post);
        recyclerViewposts.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerViewposts.setLayoutManager(manager);
        recyclerViewposts.setNestedScrollingEnabled(false);
        if(checkConnectivity()==1){
            getallphotos();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.linearLayout_post), "please Check Internet Connection...", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        recyclerViewposts.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewposts, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PostModel postModels = postModelArrayList.get(position);
                opendailogfunction(postModels.getId());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return  view;
    }

    private void opendailogfunction(String id) {
        loading = ProgressDialog.show(getContext(), "Please Wait", "Please Wait", false, false);
        String URL_POST = "https://jsonplaceholder.typicode.com/photos/"+id;
        StringRequest stringRequest = new StringRequest(URL_POST,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {

                       loading.dismiss();
                        try {
                          JSONObject photo_object   = new JSONObject(response);
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.dialog_post);
                            ImageView imageView_dialog=dialog.findViewById(R.id.imageView_dialog);
                            TextView dialog_title=dialog.findViewById(R.id.dialog_title);
                            Picasso.with(getContext()).load(photo_object.getString("url")).into(imageView_dialog);
                            dialog_title.setText(photo_object.getString("title"));
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setCancelable(true);
                            Window window = dialog.getWindow();
                            assert window != null;
                            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

    private void getallphotos() {
        recyclerViewposts.setVisibility(View.VISIBLE);

        no_result_found_post.setVisibility(View.GONE);
        loading = ProgressDialog.show(getContext(), "Please Wait", "Please Wait", false, false);
        String URL_POST = "https://jsonplaceholder.typicode.com/posts";
        StringRequest stringRequest = new StringRequest(URL_POST,
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
                                postModelArrayList.add(new PostModel(
                                        photo_object.getString("userId"),
                                        photo_object.getString("id"),
                                        photo_object.getString("title"),
                                        photo_object.getString("body")
                                ));

                            }

                            //creating adapter object and setting it to recyclerview
                            PostAdapter adapter = new PostAdapter(getContext(), postModelArrayList);
                            recyclerViewposts.setAdapter(adapter);
                            if(recyclerViewposts.getAdapter().getItemCount()==0)
                            {
                                recyclerViewposts.setVisibility(View.GONE);

                                no_result_found_post.setVisibility(View.VISIBLE);

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
