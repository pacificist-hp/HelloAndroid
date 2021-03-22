package com.client.music;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.pacificist.appwindow.ClientBaseService;

public class WindowService extends ClientBaseService {

    private boolean isPlay = false;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewCreated(View view) {
        view.findViewById(R.id.id_play_or_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlay = !isPlay;
                ((ImageView) view).setImageResource(isPlay ? R.mipmap.ic_pause_white_24dp
                        : R.mipmap.ic_play_arrow_white_24dp);
            }
        });

        view.findViewById(R.id.id_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WindowService.this, "previous", Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.id_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WindowService.this, "next", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onViewSizeChanged(int width, int height) {

    }
}
