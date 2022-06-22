package com.example.newsfresh;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NewsItemClicked{
    public NewsListAdapter newsListAdapter;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchData();
        newsListAdapter= new NewsListAdapter(this, this);
        recyclerView.setAdapter(newsListAdapter);
    }

    private void fetchData() {
        String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=46daf94e77154190ba123753bc763df2";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray newsJsonArray= null;
                        try {
                            newsJsonArray = response.getJSONArray("articles");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayList<News> newsArrayList=new ArrayList<News>();
                        for(int i=0;i<newsJsonArray.length();i++)
                        {
                            JSONObject newsJsonObject= null;
                            try {
                                newsJsonObject = newsJsonArray.getJSONObject(i);
                                Log.d("myapp","The response is "+newsJsonObject.getString("author"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            News news= null;
                            try {
                                news = new News(
                                        newsJsonObject.getString("title"),
                                        newsJsonObject.getString("author"),
                                        newsJsonObject.getString("url"),
                                        newsJsonObject.getString("urlToImage")
                                );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            newsArrayList.add(news);
                        }
                        newsListAdapter.updateNews(newsArrayList);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("User-Agent","Mozilla/5.0");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void OnItemClicked(News item) {
        String url = item.url;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}