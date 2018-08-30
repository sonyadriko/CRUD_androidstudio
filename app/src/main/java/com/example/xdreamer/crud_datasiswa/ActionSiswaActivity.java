package com.example.xdreamer.crud_datasiswa;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionSiswaActivity extends AppCompatActivity {

    @BindView(R.id.edit_value_name)
    EditText edtNama;
    @BindView(R.id.edit_value_alamat)
    EditText edtAlamat;
    @BindView(R.id.edit_value_profile)
    ImageView imageView;
    @BindView(R.id.button_update)
    Button btnEdit;
    @BindView(R.id.button_delete)
    Button btnDelete;

    private SlidrInterface slidrInterface;
    private SiswaModel siswaModel;
    private ArrayList<Image> imageArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_siswa);
        ButterKnife.bind(this);
        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.colorPrimaryDark);
        slidrInterface = Slidr.attach(this, primary, secondary);

        //Berfungsi untuk mempackages image menjadi lebih kecil
        Bundle bundle = getIntent().getExtras();
        siswaModel = bundle.getParcelable(MainAdapter.EXTRA_SISWA);

        //untuk mendapatkan data dari database dan ditaruh di form edit
        edtNama.setText(siswaModel.getNama());
        edtAlamat.setText(siswaModel.getAlamat());

        //Bergungsi sama tapi untuk mendapatkan foto dari database
        Glide.with(this)
                .load(siswaModel.getPathFoto())
                .into(imageView);

        Toast.makeText(this, "Slide untuk kembali", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.edit_value_profile)
    public void onImageViewClicked() {

        //Berfungsi untuk mengambil gambar dari internal memory
        ImagePicker.with(this)
                .setCameraOnly(false)
                .setKeepScreenOn(true)
                .setFolderMode(true)
                .setMaxSize(10)
                .setMultipleMode(false)
                .setFolderTitle("Albums")
                .setSelectedImages(imageArrayList)
                .setAlwaysShowDoneButton(true)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //melakukan pengecekan apakah datanya tidak null
        if (data != null){
            //jika ada data di parcel lalu dimasukkan ke arrayList
            imageArrayList = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            //lalu mengambil gambar dari yang dipilih berdasarkan urutan (position)
            siswaModel.setPathFoto(imageArrayList.get(0).getPath());

            //Glide berfungsi Untuk meload gambar ke UI/XML
            Glide.with(this)
                    .load(imageArrayList.get(0).getPath())
                    .into(imageView);
        }
    }

    @OnClick(R.id.button_update)
    public void onBtnEditClicked() {
        siswaModel.setNama(edtNama.getText().toString());
        siswaModel.setAlamat(edtAlamat.getText().toString());
        siswaModel.setPathFoto(siswaModel.getPathFoto());

        SiswaApp.db.userDao().update(siswaModel);
        Intent i = new Intent(ActionSiswaActivity.this, MainActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.button_delete)
    public void onBtnDeleteClicked() {
        SiswaApp.db.userDao().deleteUsers(siswaModel);
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}