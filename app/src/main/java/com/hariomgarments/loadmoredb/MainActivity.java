package com.hariomgarments.loadmoredb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.hariomgarments.loadmoredb.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.loadmoredb.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.loadmoredb.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.loadmoredb.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.loadmoredb.BackEnd.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private adaptor mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<model> studentList;
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<model>();
        handler = new Handler();

        loadData();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new adaptor(this, studentList, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();


        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 5;
                        AndroidNetworking.post(Apis.load_more)
                                .addBodyParameter("offset", String.valueOf(start))
                                .addBodyParameter("catagory", "hair")
                                .setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // do anything with response
                                        try {
                                            //  Toast.makeText(getActivity(), "session" + response.getBoolean("status"), Toast.LENGTH_SHORT).show();
                                            //  Log.w("wwwwwwww", "-----------" +response.getString("data"));
                                            //Toast.makeText(getActivity(), "session" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                            if (!response.getBoolean("status")) {
                                                Log.w("wwwwwwww", "-----------" + response.getString("data"));
                                                Log.w("wwwwwwww", "-----------" + response.getString("offsetvalue"));
                                                JSONArray valarray = new JSONArray(response.getString("data"));

                                                for (int i = 0; i < valarray.length(); i++) {
                                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                                    model personUtils = new model();
                                                    personUtils.setPrice(jsonobject.getString("product_name"));
                                                    personUtils.setName(jsonobject.getString("id"));
                                                    //Toast.makeText(getApplicationContext(), "session" + jsonobject, Toast.LENGTH_SHORT).show();
                                                    studentList.add(personUtils);
                                                }
                                                mAdapter.notifyDataSetChanged();
                                               // mAdapter.setLoaded();

                                            } else {
                                                Log.w("wwwwwwww", "-----------" + response.getString("data"));
                                                Log.w("wwwwwwww", "-----------" + response.getString("offsetvalue"));
                                                JSONArray valarray = new JSONArray(response.getString("data"));
                                                for (int i = 0; i < valarray.length(); i++) {
                                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                                    model personUtils = new model();
                                                    personUtils.setPrice(jsonobject.getString("product_name"));
                                                    personUtils.setName(jsonobject.getString("id"));
                                                    // Toast.makeText(getActivity(), "session" + jsonobject, Toast.LENGTH_SHORT).show();
                                                    studentList.add(personUtils);
                                                }
                                                Toast.makeText(MainActivity.this,  response.getString("message"), Toast.LENGTH_SHORT).show();
                                                mAdapter.notifyDataSetChanged();

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        //  progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "session" + error, Toast.LENGTH_SHORT).show();
                                        Log.w("------", "msg+--" + error);
                                    }
                                });
                    }
                }, 2000);

            }
        });

    }


    // load initial data
    private void loadData() {
        AndroidNetworking.post(Apis.load_more)
                .addBodyParameter("offset", "0")
                .addBodyParameter("catagory", "hair")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            //  Toast.makeText(getActivity(), "session" + response.getBoolean("status"), Toast.LENGTH_SHORT).show();
                            //  Log.w("wwwwwwww", "-----------" +response.getString("data"));
                            //Toast.makeText(getActivity(), "session" + response.getString("message"), Toast.LENGTH_SHORT).show();

                            if (!response.getBoolean("status")) {
                                Log.w("wwwwwwww", "-----------" + response.getString("data"));
                                Log.w("wwwwwwww", "-----------" + response.getString("offsetvalue"));
                                JSONArray valarray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                    model personUtils = new model();
                                    personUtils.setPrice(jsonobject.getString("product_name"));
                                    personUtils.setName(jsonobject.getString("id"));
                                    // Toast.makeText(getActivity(), "session" + jsonobject, Toast.LENGTH_SHORT).show();
                                    studentList.add(personUtils);
                                }
                                mAdapter.notifyDataSetChanged();

                            } else {
                                Log.w("wwwwwwww", "-----------" + response.getString("data"));
                                Log.w("wwwwwwww", "-----------" + response.getString("offsetvalue"));
                                JSONArray valarray = new JSONArray(response.getString("data"));
                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                    model personUtils = new model();
                                    personUtils.setPrice(jsonobject.getString("product_name"));
                                    personUtils.setName(jsonobject.getString("id"));
                                    // Toast.makeText(getActivity(), "session" + jsonobject, Toast.LENGTH_SHORT).show();
                                    studentList.add(personUtils);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //  progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "session" + error, Toast.LENGTH_SHORT).show();
                        Log.w("------", "msg+--" + error);
                    }
                });
    }


}