package com.art.notas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ConfigActivity extends Activity {

    Button buttonG , buttonR;
    ImageView imageViewR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        buttonG     =   findViewById(R.id.buttonSource);
        buttonR     =   findViewById(R.id.buttonRequest);
        imageViewR  =   findViewById(R.id.imageReturn);

        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri =   Uri.parse( "https://github.com/artpin107/Notas2" );
                Intent intent   =   new Intent( Intent.ACTION_VIEW , uri );
                startActivity( intent );
            }
        });
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri =   Uri.parse( "https://t.me/artP2" );
                Intent intent   =   new Intent( Intent.ACTION_VIEW , uri );
                startActivity( intent );
            }
        });
        imageViewR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigActivity.this.finish();
            }
        });
    }

}
