package com.bagicode.www.videotestapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bagicode.www.videotestapi.API.ApiService;
import com.bagicode.www.videotestapi.Model.ModelData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainEdit extends AppCompatActivity {

    String ID_MAHASISWA;
    EditText et_id, et_nama, et_kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ID_MAHASISWA = getIntent().getStringExtra(ModelData.id_mahasiswa);

        et_id = (EditText) findViewById(R.id.edit_id);
        et_nama = (EditText) findViewById(R.id.edit_nama);
        et_kelas = (EditText) findViewById(R.id.edit_kelas);

        bindData();

        Button btnUbah = (Button) findViewById(R.id.ubah);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sId = String.valueOf(et_id.getText());
                String sNama = String.valueOf(et_nama.getText());
                String sKelas = String.valueOf(et_kelas.getText());

                if (sId.equals("")){
                    Toast.makeText(MainEdit.this, "Jangan Rubah ID", Toast.LENGTH_SHORT).show();
                } else if (sNama.equals("")){
                    Toast.makeText(MainEdit.this, "Silahkan isi Nama", Toast.LENGTH_SHORT).show();
                } else if (sKelas.equals("")){
                    Toast.makeText(MainEdit.this, "Silahkan isi kelas", Toast.LENGTH_SHORT).show();
                } else {
                    editData(sId, sNama, sKelas);
                }
            }
        });

        Button btnDel = (Button) findViewById(R.id.hapus);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusData(ID_MAHASISWA);
            }
        });

        // ditambahkan
        Button btnBatal = (Button) findViewById(R.id.batal);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void bindData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<ModelData>> call = service.getSingleData(ID_MAHASISWA);
        call.enqueue(new Callback<List<ModelData>>() {
            @Override
            public void onResponse(Call<List<ModelData>> call, Response<List<ModelData>> response) {

                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().size(); i++) {

                        et_id.setText(response.body().get(i).getidMahasiswa());
                        et_nama.setText(response.body().get(i).getNama());
                        et_kelas.setText(response.body().get(i).getKelas_mhs());

                    }

                }

            }

            @Override
            public void onFailure(Call<List<ModelData>> call, Throwable t) {

            }
        });
    }

    public void editData(String id, String nama, String kelas) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(new StringConverter())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.editData(id, nama, kelas);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                BufferedReader reader = null;
                String respon = "";

                try {
                    reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    respon = reader.readLine();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainEdit.this, respon, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void hapusData(String id_mhs) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(new StringConverter())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.hapusData(id_mhs);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                BufferedReader reader = null;
                String respon = "";

                try {
                    reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    respon = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainEdit.this, respon, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
