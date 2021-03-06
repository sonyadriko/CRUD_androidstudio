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

public class AddSiswaActivity extends AppCompatActivity {

    @BindView(R.id.edit_name)
    EditText inputNama;
    @BindView(R.id.edit_alamat)
    EditText inputAlamat;
    @BindView(R.id.img_add_profile)
    ImageView inputFoto;
    @BindView(R.id.button_add)
    Button btnSubmit;

    private SiswaModel siswaModel;
    private ArrayList<Image> imageArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_siswa);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.img_add_profile)
    public void onImageViewClicked() {
        ImagePicker.with(this)
                .setFolderMode(true)
                .setMaxSize(10)
                .setMultipleMode(false)
                .setCameraOnly(false)
                .setFolderTitle("Albums")
                .setSelectedImages(imageArrayList)
                .setKeepScreenOn(true)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageArrayList = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

            Glide.with(this)
                    .load(imageArrayList.get(0).getPath())
                    .into(inputFoto);
        }
    }

    @OnClick(R.id.button_add)
    public void onBtnSubmitClicked() {

        if (!inputNama.getText().toString().isEmpty()
                && !inputAlamat.getText().toString().isEmpty()
                && !imageArrayList.isEmpty()) {
            siswaModel = new SiswaModel();

            siswaModel.setNama(inputNama.getText().toString());
            siswaModel.setAlamat(inputAlamat.getText().toString());
            siswaModel.setPathFoto(imageArrayList.get(0).getPath().toString());

            SiswaApp.db.userDao().insertAll(siswaModel);

            Intent i = new Intent(AddSiswaActivity.this, MainActivity.class);
            i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }
}