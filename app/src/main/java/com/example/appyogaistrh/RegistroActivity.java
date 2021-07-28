package com.example.appyogaistrh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    TextInputLayout t1,t2;
    ProgressBar bar;
    FirebaseAuth mAuth;
    TextView tvregistro;
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        t1=(TextInputLayout)findViewById(R.id.email_login);
        t2=(TextInputLayout)findViewById(R.id.pwd_login);
        bar=(ProgressBar)findViewById(R.id.progressBar3_login);
        mAuth = FirebaseAuth.getInstance();
        tvregistro= findViewById(R.id.textViewRegistrando);
    }

    public void singup(View view)
    {
        bar.setVisibility(View.VISIBLE);
        tvregistro.setVisibility(View.VISIBLE);

        String email=t1.getEditText().getText().toString();
        String password=t2.getEditText().getText().toString();

        if(email.isEmpty()==false && password.length()>=6)
        {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                bar.setVisibility(View.INVISIBLE);
                                tvregistro.setVisibility(View.INVISIBLE);
                                Intent intent=new Intent(RegistroActivity.this,MainActivity.class);
                                intent.putExtra("email",mAuth.getCurrentUser().getEmail());
                                intent.putExtra("uid",mAuth.getCurrentUser().getUid());
                                guardaCorreoUsuario(mAuth.getCurrentUser().getEmail());
                                startActivity(intent);
                                t1.getEditText().setText("");
                                t2.getEditText().setText("");
                                Toast.makeText(getApplicationContext(),"Registro correcto",Toast.LENGTH_LONG).show();
                            } else
                            {
                                bar.setVisibility(View.INVISIBLE);
                                tvregistro.setVisibility(View.INVISIBLE);
                                t1.getEditText().setText("");
                                t2.getEditText().setText("");
                                Toast.makeText(getApplicationContext(),"Error en el registro",Toast.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });

        }
        else
        {
            bar.setVisibility(View.INVISIBLE);
            tvregistro.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Por favor introduzca los datos válidos",Toast.LENGTH_LONG).show();
        }
    }

    public void guardaCorreoUsuario(String correo){
        database = FirebaseDatabase.getInstance().getReference();
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("correo", correo);
        database.child("BD_Correos").push().setValue(hashMap);
    }
}
