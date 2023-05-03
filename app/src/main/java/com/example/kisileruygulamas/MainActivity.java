package com.example.kisileruygulamas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ArrayList<Kisiler> kisilerArrayList;
    private KisilerAdapter adapter;
    private KisilerDaoInterface kisilerDif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        kisilerDif = ApiUtils.getKisilerDaoInterfeace();

        toolbar.setTitle("Kişiler Uygulaması");
        setSupportActionBar(toolbar);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        tumKisiler();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertGoster();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_ara);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit",query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("onQueryTextChange",newText);
        tumKisilerArama(newText);
        return false;
    }

    public void alertGoster() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.alert_tasarim,null);

        EditText editTextAd = view.findViewById(R.id.editTextAd);
        EditText editTextTel = view.findViewById(R.id.editTextTel);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Kişi Ekle");
        ad.setView(view);

        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel = editTextTel.getText().toString().trim();

                kisilerDif.kisiEkle(kisi_ad,kisi_tel).enqueue(new Callback<KisilerCevap>() {
                    @Override
                    public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {
                        tumKisiler();
                    }

                    @Override
                    public void onFailure(Call<KisilerCevap> call, Throwable t) {

                    }
                });

                Toast.makeText(getApplicationContext(),kisi_ad+" - "+kisi_tel,Toast.LENGTH_SHORT).show();

            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ad.create().show();
    }

    public void tumKisiler(){
        kisilerDif.tumKisiler().enqueue(new Callback<KisilerCevap>() {
            @Override
            public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {
                List<Kisiler> liste = response.body().getKisiler();

                adapter = new KisilerAdapter(MainActivity.this,liste,kisilerDif);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<KisilerCevap> call, Throwable t) {

            }
        });
    }

    public void tumKisilerArama(String kelime){
        kisilerDif.kisiArama(kelime).enqueue(new Callback<KisilerCevap>() {
            @Override
            public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {
                List<Kisiler> liste = response.body().getKisiler();

                adapter = new KisilerAdapter(MainActivity.this,liste,kisilerDif);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<KisilerCevap> call, Throwable t) {

            }
        });
    }
}