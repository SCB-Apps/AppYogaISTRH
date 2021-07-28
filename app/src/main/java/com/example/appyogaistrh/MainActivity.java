package com.example.appyogaistrh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    TextInputLayout t1,t2;
    TextView tvregistro, tvacceso;
    ProgressBar bar;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        t1=(TextInputLayout)findViewById(R.id.email_login);
        t2=(TextInputLayout)findViewById(R.id.pwd_login);
        bar=(ProgressBar)findViewById(R.id.progressBar3_login);
        mAuth = FirebaseAuth.getInstance();
        tvregistro= findViewById(R.id.textViewRegistro);
        tvacceso= findViewById(R.id.textViewAccediendo);
        

        t1.getEditText().setText("");
        t2.getEditText().setText("");




    }

    public void signinhere(View view)
    {
        bar.setVisibility(View.VISIBLE);
        tvacceso.setVisibility(View.VISIBLE);
        tvregistro.setVisibility(View.INVISIBLE);
        String email=t1.getEditText().getText().toString();
        String password=t2.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            bar.setVisibility(View.INVISIBLE);
                            tvacceso.setVisibility(View.INVISIBLE);
                            tvregistro.setVisibility(View.VISIBLE);
                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            if(mAuth.getCurrentUser().getEmail().equals("appyogaistrh@gmail.com")){
                                Intent intent=new Intent(MainActivity.this,AdministradorActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent=new Intent(MainActivity.this,UsuarioActivity.class);
                                startActivity(intent);
                            }

                        } else
                        {
                            bar.setVisibility(View.INVISIBLE);
                            tvacceso.setVisibility(View.INVISIBLE);
                            tvregistro.setVisibility(View.VISIBLE);
                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            Toast.makeText(getApplicationContext(),"Email o contraseña incorrectos",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });



    }


    public void gotosingup(View view)
    {
        startActivity(new Intent(MainActivity.this,RegistroActivity.class));
    }




}