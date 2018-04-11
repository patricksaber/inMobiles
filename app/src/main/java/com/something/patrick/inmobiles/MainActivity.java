package com.something.patrick.inmobiles;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> items;
    private int count=0;
    private Timer runTime;
    TextView timerLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerLoad    = findViewById(R.id.timerLoad);
        recyclerView = findViewById(R.id.recycler_view);

        // setting to improve performance if you know that changes
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Item>> call = api.getItems();

        call.enqueue(new Callback<List<Item>>() {

            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                List<Item> remoteItems = response.body();
                ContentValues values = new ContentValues();
                for (int i = 0; i < remoteItems.size(); i++) {

                    Item item = remoteItems.get(i);

                    values.put(ItemsProvider.COL_TITLE, (item.getTitle()));
                    values.put(ItemsProvider.COL_DESCRIPTION, (item.getDescription()));
                    values.put(ItemsProvider.COL_LINK, (item.getLink()));
                    Uri uri = getContentResolver().insert(ItemsProvider.CONTENT_URI, values);


                    Toast.makeText(getBaseContext(), uri.toString() + " Inserted!", Toast.LENGTH_LONG).show();
                }
            }
// OnFailure  Load data already saved
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Attention: New remote items cannot be fetched: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });

        // Show all the items sorted by friend's name
        String URL = "content://com.something.patrick.inmobiles.ItemsProvider/items";
        Uri itemsUri = Uri.parse(URL);
        Cursor c = getContentResolver().query(itemsUri, null, null, null, "title");
        List<Item> items = new ArrayList<>();
        if (c.moveToFirst()) {
            do{
                Item item = new Item(
                        c.getInt(c.getColumnIndex(ItemsProvider.COL_ID)),
                        c.getString(c.getColumnIndex(ItemsProvider.COL_LINK)),
                        c.getString(c.getColumnIndex(ItemsProvider.COL_TITLE)),
                        c.getString(c.getColumnIndex(ItemsProvider.COL_DESCRIPTION))
                );
                items.add(item);
            } while (c.moveToNext());
        }
        // specify an adapter
        adapter = new Adapter(items);
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();                                //Counting the time on start application
        runTime = new Timer();
        runTime.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerLoad.setText("Application's running time: " + count +" s");
                        count++;
                    }
                });
            }
        }, 1000, 1000);

    }

    @Override
    protected void onDestroy() {    //On Destroy
        super.onDestroy();

        runTime.cancel();

    }

    @Override
    protected void onRestart() {    //On restart
        super.onRestart();
        runTime.cancel();
    }

    @Override
    protected void onStop() {       //On stop
        super.onStop();
        runTime.cancel();
    }

    @Override
    protected void onPause() {      //On pause
        super.onPause();
        runTime.cancel();
    }

}
