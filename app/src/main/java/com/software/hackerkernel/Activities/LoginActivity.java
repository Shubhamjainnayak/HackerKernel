package com.software.hackerkernel.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.software.hackerkernel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
EditText sign_up_username,sign_up_password;
Button loginclick;
String str_sign_up_usernames,str_sign_up_password;
ProgressDialog loading;
int internet;
LinearLayout linearLayout_parent;
    public static final String SHARED_PREF_NAME = "user_info";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    String loginurl="http://139.59.87.150/demo/Shree_Sai_Mall/public/api/user-login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_up_username=findViewById(R.id.sign_up_username);
        sign_up_password=findViewById(R.id.sign_up_password);
        linearLayout_parent=findViewById(R.id.linearLayout_parent);
        loginclick=findViewById(R.id.loginclick);


        loginclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_sign_up_usernames=  sign_up_username.getText().toString().trim();
                str_sign_up_password=sign_up_password.getText().toString().trim();
                if(isValidEmail(str_sign_up_usernames) && (!str_sign_up_password.isEmpty() && str_sign_up_password.length()>6))
                {  if(checkConnectivity()==1)
                { userloginfunction(str_sign_up_usernames,str_sign_up_password);
                  //  startActivity(new Intent(LoginActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                    //finish();
                }

                   else
                {
                    Snackbar snackbar = Snackbar.make(linearLayout_parent, "please Check Internet Connection...", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                }else
                {
                    if(!isValidEmail(str_sign_up_usernames))
                        sign_up_username.setError("Invaild Email");
                    if(str_sign_up_password.isEmpty())
                        sign_up_password.setError("Invaild Password");
                    if(str_sign_up_password.isEmpty())
                        sign_up_password.setError("The password must be at least 6 characters");
                }

            }
        });
    }
    private void userloginfunction(final String str_sign_up_usernames,final String str_sign_up_password) {

        loading = ProgressDialog.show(LoginActivity.this, "Please Wait", "Please Wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();

                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                        editor.putString("email", str_sign_up_usernames);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                                editor.putString("email", str_sign_up_usernames);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                                finish();
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams()  {

                Map<String, String> prams = new HashMap<>();
                prams.put("type", "a");
                prams.put("email", str_sign_up_usernames);
                prams.put("password", str_sign_up_password);
                return prams;
            }
        };
        final RequestQueue requestQueue = Volley.newRequestQueue(this,new HurlStack());
        requestQueue.add(stringRequest).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
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
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private int checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            internet = 0;
        } else {
            internet = 1;
        }
        return internet;
    }
}
