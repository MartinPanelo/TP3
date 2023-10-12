package com.martintecno.tp3.ui.registro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.martintecno.tp3.R;
import com.martintecno.tp3.databinding.ActivityRegistroBinding;
import com.martintecno.tp3.ui.model.Usuario;

public class RegistroActivity extends AppCompatActivity {

    private RegistroActivityViewModel mv;

    private ActivityRegistroBinding binding;

    private Usuario usuarioActual = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String correo = getIntent().getStringExtra("correo");

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mv = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistroActivityViewModel.class);

        binding.BTNGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mv.ActualizarRegistrar(binding.ETDni.getText().toString(),
                        binding.ETApellido.getText().toString(),
                        binding.ETNombre.getText().toString(),
                        binding.ETCorreo.getText().toString(),
                        binding.ETContraseA.getText().toString());


            }
        });


        mv.getBotonM().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.BTNGuardar.setText(s);
            }
        });

        mv.getTituloM().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.TVCartel.setText(s);
            }
        });

        mv.getUsuarioM().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.ETDni.setText(usuario.getDni());
                binding.ETApellido.setText(usuario.getApellido());
                binding.ETNombre.setText(usuario.getNombre());
                binding.ETCorreo.setText(usuario.getCorreo());
                binding.ETContraseA.setText(usuario.getContraseña());
                usuarioActual = usuario;
                mv.leerFotoArchivo(usuarioActual.getFoto());
            }
        });

        binding.BTNFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivityForResult es otra forma de iniciar una activity, pero esperando desde donde la llamé un resultado
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }

            }
        });


        mv.getFoto().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.IMGPerfil.setImageBitmap(bitmap);
            }
        });





        /*-------------------*/
        mv.cargarSesion(correo);




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mv.respuetaDeCamara(requestCode, resultCode, data, 1,usuarioActual);
    }
}