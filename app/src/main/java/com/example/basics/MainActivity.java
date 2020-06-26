package com.example.basics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Button bPlay;
    Button bStop;
    ArrayAdapter<String> adapter;
    static String link;
    static String story;
    TextView textView;
    Button bQr;

    GifImageView image;

    static String url = "qwerty";
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bPlay = findViewById(R.id.id_play);
        bStop = findViewById(R.id.id_stop);
        image = findViewById(R.id.imageView);


        bQr = findViewById(R.id.id_qr);
        mediaPlayer = new MediaPlayer();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child("songs").child(url).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        link = dataSnapshot.child("songLink").getValue(String.class);
                        story = dataSnapshot.child("story").getValue(String.class);
                        if(story == null || link == null){
                            Toast.makeText(getApplicationContext(),"Ошибка", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getApplicationContext(),"Секунду", Toast.LENGTH_LONG).show();
                        Log.d("TAG",link + story);
                        Glide.with(getApplicationContext()).load(story).into(image);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );







        bQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QrReadActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                mediaPlayer.reset();
                }else if(link == "null"){
                    Toast.makeText(getApplicationContext(),"Ошибка", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Аудио и так не включена", Toast.LENGTH_LONG).show();}
            }
        });


        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()) {
                    try {
                        mediaPlayer.setDataSource(link);
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                        mediaPlayer.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Вы ее не слышите?", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            url = data.getStringExtra("result");
            Toast.makeText(getBaseContext(), url, Toast.LENGTH_SHORT).show();
            databaseReference.child("songs").child(url).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            link = dataSnapshot.child("songLink").getValue(String.class);
                            story = dataSnapshot.child("story").getValue(String.class);
                            Toast.makeText(getApplicationContext(),"Секунду", Toast.LENGTH_LONG).show();
                            Log.d("TAG",link + story);
                            Glide.with(getApplicationContext()).load(story).into(image);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }
            );


        }

    }
}
