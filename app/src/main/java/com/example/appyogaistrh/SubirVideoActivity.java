package com.example.appyogaistrh;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SubirVideoActivity extends AppCompatActivity {


    private static final int File = 1 ;
    DatabaseReference database;
    DatabaseReference mDatabase;
    private EditText titulo,descripcion,dificultad;
    long current_time;
    ArrayList<Correos> listaCorreos;
    ProgressBar bar;
    Button boton;
    TextView textView;

    @SuppressLint("NonConstantResourceId")
    //@BindView(R.id.uploadImageView)
    ImageView mUploadImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subir_video_activity);

        ButterKnife.bind(this);

        titulo= findViewById(R.id.etTitulo);
        descripcion= findViewById(R.id.etDescripcion);
        dificultad= findViewById(R.id.etDificultad);
        bar=(ProgressBar)findViewById(R.id.progressBar3_login);
        boton= findViewById(R.id._SubirVideo);
        textView= findViewById(R.id.textViewSubirVideo);

        mDatabase= FirebaseDatabase.getInstance().getReference();

        listaCorreos=new ArrayList<>();



       // mUploadImageView.setOnClickListener(v -> fileUpload());
    }

    public void fileUpload(View view) {


        if(titulo.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty() || dificultad.getText().toString().isEmpty()) {

            Toast.makeText(this, "Por favor, introduzca todos los campos", Toast.LENGTH_LONG).show();

        }else {

            database = FirebaseDatabase.getInstance().getReference();
            current_time = System.currentTimeMillis();

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, File);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == File){

            if(resultCode == RESULT_OK){
                bar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                boton.setVisibility(View.INVISIBLE);

                Uri FileUri = data.getData();



                StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Videos App Yoga ISTRH");

                final StorageReference file_name = Folder.child("Video_"+current_time);


                file_name.putFile(FileUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {

                    Map<String,Object> hashMap = new HashMap<>();
                    hashMap.put("url", String.valueOf(uri));
                    hashMap.put("titulo", titulo.getText().toString());
                    hashMap.put("descripcion", descripcion.getText().toString());
                    hashMap.put("dificultad", dificultad.getText().toString());
                    database.child("BD_Videos").push().setValue(hashMap);



                    obtenerCorreos();




                }));

            }

        }

    }

    public void obtenerCorreos(){
        mDatabase.child("BD_Correos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    mDatabase.child("BD_Correos").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            Correos correos = snapshot.getValue(Correos.class);
                            String correo = correos.getCorreo();
                            String mensajeRegistro="DATOS DEL VÍDEO: <br>Nombre del vídeo: "+titulo.getText().toString()+"<br>Descripción del vídeo: "+descripcion.getText().toString()+
                                    "<br>Dificultad del vídeo: "+dificultad.getText().toString();

                            EnviarCorreo(mensajeRegistro,correo);


                            Log.e("Datos:",""+snapshot.getValue());


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }

                Toast.makeText(getApplicationContext(),"Vídeo subido correctamente",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SubirVideoActivity.this,AdministradorActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }


    //Método para el botón de enviar
    public void EnviarCorreo(String mensaje,String correo){

        sendEmailWithGmail(
                "mobilewapsemisor@gmail.com",
                "administrador",
                correo,
                "Nuevo vídeo publicado de la App Yoga ISTRH",
                mensaje);



    }


    /**
     * Send email with Gmail service.
     */
    private void sendEmailWithGmail(final String recipientEmail, final String recipientPassword,
                                    String to, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(recipientEmail, recipientPassword);
            }
        });

        SubirVideoActivity.SenderAsyncTask task = new SubirVideoActivity.SenderAsyncTask(session, recipientEmail, to, subject, message);
        task.execute();
    }

    /**
     * AsyncTask to send email
     */
    private class SenderAsyncTask extends AsyncTask<String, String, String> {

        private String from, to, subject, message;
        private ProgressDialog progressDialog;
        private Session session;

        public SenderAsyncTask(Session session, String from, String to, String subject, String message) {
            this.session = session;
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(_4b3a_NuevoEstadoAnimoActivity.this, "", "Creando registro de estado de ánimo", true);
            //progressDialog.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                javax.mail.Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                mimeMessage.setSubject(subject);
                mimeMessage.setContent(message, "text/html; charset=utf-8");
                Transport.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //progressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //ntext(), "Estado de ánimo registrado", Toast.LENGTH_SHORT).show();

        }


    }

}