package com.example.pkl.crudmahasiswa;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class Activity_Edit extends AppCompatActivity {

    com.rengwuxian.materialedittext.MaterialEditText et_nim,et_nama,et_prodi,et_nohp;
    String nim,nama,prodi,nohp;
    Button btn_submit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__edit);

        et_nim          = findViewById(R.id.et_noinduk);
        et_nama             = findViewById(R.id.et_nama);
        et_prodi           = findViewById(R.id.et_alamat);
        et_nohp             = findViewById(R.id.et_hobi);
        btn_submit          = findViewById(R.id.btn_submit);

        progressDialog      = new ProgressDialog(this);

        getDataIntent();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Menambahkan Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                nim = et_nim.getText().toString();
                nama = et_nama.getText().toString();
                prodi = et_prodi.getText().toString();
                nohp = et_nohp.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validasiData();
                    }
                },1000);
            }
        });

    }

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            et_nim.setText(bundle.getString("nim"));
            et_nama.setText(bundle.getString("nama"));
            et_prodi.setText(bundle.getString("prodi"));
            et_nohp.setText(bundle.getString("nohp"));
        }else{
            et_nim.setText("");
            et_nama.setText("");
            et_prodi.setText("");
            et_nohp.setText("");
        }

    }

    void validasiData(){
        if(nim.equals("") || nama.equals("") || prodi.equals("") || nohp.equals("")){
            progressDialog.dismiss();
            Toast.makeText(Activity_Edit.this, "Periksa kembali data yang anda masukkan !", Toast.LENGTH_SHORT).show();
        }else {
            updateData();
        }
    }

    void updateData(){
        AndroidNetworking.post("http://172.30.160.1:81/update.php")
                .addBodyParameter("nim",""+nim)
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("prodi",""+prodi)
                .addBodyParameter("nohp",""+nohp)
                .setTag("Update Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                new AlertDialog.Builder(Activity_Edit.this)
                                        .setMessage("Berhasil Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_OK,i);
                                                Activity_Edit.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(Activity_Edit.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                Activity_Edit.this.finish();
                                            }
                                        })
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_back){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
