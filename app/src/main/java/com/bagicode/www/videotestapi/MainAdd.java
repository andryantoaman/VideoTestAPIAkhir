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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainAdd extends AppCompatActivity {

    String ID_MAHASISWA;
    EditText et_id, et_nama, et_kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ID_MAHASISWA = getIntent().getStringExtra(ModelData.id_mahasiswa);

        et_id = (EditText) findViewById(R.id.edit_id);
        et_nama = (EditText) findViewById(R.id.edit_nama);
        et_kelas = (EditText) findViewById(R.id.edit_kelas);

        final EditText edtNama = (EditText) findViewById(R.id.edit_nama);
        final EditText edtKelas = (EditText) findViewById(R.id.edit_kelas);

        Button btnAdd = (Button) findViewById(R.id.simpan);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sNama = String.valueOf(edtNama.getText());
                String sKelas = String.valueOf(edtKelas.getText());
                tambahData(sNama, sKelas);
            }
        });
    }


    public void tambahData(String nama, String kelas) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(new StringConverter())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.tambahData(nama, kelas);
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

                Toast.makeText(MainAdd.this, respon, Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainAdd.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
