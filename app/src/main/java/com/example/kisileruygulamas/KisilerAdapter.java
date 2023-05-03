package com.example.kisileruygulamas;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu> {
    private Context mContext;
    private List<Kisiler> kisilerList;
    private KisilerDaoInterface kisilerDif;

    public KisilerAdapter(Context mContext, List<Kisiler> kisilerList, KisilerDaoInterface kisilerDif) {
        this.mContext = mContext;
        this.kisilerList = kisilerList;
        this.kisilerDif = kisilerDif;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_kard_tasarim,parent,false);
        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {
      final Kisiler kisi = kisilerList.get(position);

        holder.textViewKisiBilgi.setText(kisi.getKisiAd()+" - "+kisi.getKisiTel());

        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(mContext,holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_sil:
                                Snackbar.make(holder.imageViewNokta,"Kişi Silinsin mi ?",Snackbar.LENGTH_SHORT)
                                        .setAction("Evet", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        kisilerDif.kisiSil(Integer.parseInt(kisi.getKisiId())).enqueue(new Callback<KisilerCevap>() {
                                            @Override
                                            public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {
                                                tumKisiler();
                                            }

                                            @Override
                                            public void onFailure(Call<KisilerCevap> call, Throwable t) {

                                            }
                                        });

                                        notifyDataSetChanged();
                                    }
                                }).show();
                                return true;
                            case R.id.action_guncelle:
                                alertGoster(kisi);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return kisilerList.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        private TextView textViewKisiBilgi;
        private ImageView imageViewNokta;


        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);

            textViewKisiBilgi = itemView.findViewById(R.id.textViewKisiBilgi);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);


        }
    }

    public void alertGoster(Kisiler kisi) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.alert_tasarim,null);

        EditText editTextAd = view.findViewById(R.id.editTextAd);
        EditText editTextTel = view.findViewById(R.id.editTextTel);

        editTextAd.setText(kisi.getKisiAd());
        editTextTel.setText(kisi.getKisiTel());

        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setTitle("Kişi Güncelle");
        ad.setView(view);

        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel = editTextTel.getText().toString().trim();

                kisilerDif.kisiGuncelle(Integer.parseInt(kisi.getKisiId()),kisi_ad,kisi_tel).enqueue(new Callback<KisilerCevap>() {
                    @Override
                    public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {
                        tumKisiler();
                    }

                    @Override
                    public void onFailure(Call<KisilerCevap> call, Throwable t) {

                    }
                });

                notifyDataSetChanged();

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
                kisilerList = response.body().getKisiler();

                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<KisilerCevap> call, Throwable t) {

            }
        });
    }
}
