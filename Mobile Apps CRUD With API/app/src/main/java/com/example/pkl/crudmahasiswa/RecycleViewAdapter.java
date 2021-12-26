package com.example.pkl.crudmahasiswa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> array_nim,array_nama,array_prodi,array_nohp;
    ProgressDialog progressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nim,tv_nama,tv_prodi,tv_nohp;
        public CardView cv_main;

        public MyViewHolder(View view) {
            super(view);
            cv_main = itemView.findViewById(R.id.cv_main);
            tv_nim = itemView.findViewById(R.id.tv_noind);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_prodi = itemView.findViewById(R.id.tv_alamat);
            tv_nohp = itemView.findViewById(R.id.tv_hobi);

            progressDialog = new ProgressDialog(mContext);
        }
    }

    public RecycleViewAdapter(Context mContext, ArrayList<String> array_nim,ArrayList<String> array_nama,ArrayList<String> array_prodi,ArrayList<String> array_nohp) {
        super();
        this.mContext = mContext;
        this.array_nim = array_nim;
        this.array_nama = array_nama;
        this.array_prodi = array_prodi;
        this.array_nohp = array_nohp;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.template_rv,parent,false);
        return new RecycleViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_nim.setText(array_nim.get(position));
        holder.tv_nama.setText(array_nama.get(position));
        holder.tv_prodi.setText(array_prodi.get(position));
        holder.tv_nohp.setText(array_nohp.get(position));
        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,Activity_Edit.class);
                i.putExtra("nim",array_nim.get(position));
                i.putExtra("nama",array_nama.get(position));
                i.putExtra("prodi",array_prodi.get(position));
                i.putExtra("nohp",array_nohp.get(position));
                ((Activity_Main)mContext).startActivityForResult(i,2);
            }
        });
        holder.cv_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder((Activity_Main)mContext)
                        .setMessage("Ingin menghapus Data dengan NIM "+array_nim.get(position)+" ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Menghapus...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                AndroidNetworking.post("http://172.30.160.1:81/delete.php")
                                        .addBodyParameter("nim",""+array_nim.get(position))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                progressDialog.dismiss();
                                                try {
                                                    Boolean status = response.getBoolean("status");
                                                    Log.d("statuss",""+status);
                                                    String result = response.getString("result");
                                                    if(status){
                                                        if(mContext instanceof Activity_Main){
                                                            ((Activity_Main)mContext).scrollRefresh();
                                                        }
                                                    }else{
                                                        Toast.makeText(mContext, ""+result, Toast.LENGTH_SHORT).show();
                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                anError.printStackTrace();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return array_nim.size();
    }
}
