package com.something.patrick.inmobiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<String> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        listView=(ListView)findViewById(R.id.listView) ;

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Product>> call = api.getProducts();

        call.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();
//                int productsNb = products.size();

                ArrayList<Product> mProducts = new ArrayList<Product>();
                for (int i = 0; i < products.size() ; i++) {
                    Product product = new Product(
                            products.get(i).getId(),
                            products.get(i).getLink(),
                            products.get(i).getTitle(),
                            products.get(i).getDescription()
                    );
                    mProducts.add(product);
                }
//                ArrayList<Product> z = new ArrayList<Product>(products);

//                for (int i = 0; i<productsNb; i++){
//                    mProducts.add(products.get(i).getTitle());
//                    mProducts.add(products.get(i).getDescription());
//                    mProducts.add(products.get(i).getLink());
//            }
                mAdapter = new MyAdapter(mProducts);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "noooooooooo", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
