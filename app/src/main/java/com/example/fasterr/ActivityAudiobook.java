package com.example.fasterr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ohoussein.playpause.PlayPauseView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityAudiobook extends AppCompatActivity {

    private TextToSpeech engine;
    private ArrayList<List<String>> mp;
    private String bookName, toSpeak,mins,sec;
    private String sentences[];
    private SeekBar seek;
    private ImageView cover,skip_back,skip_front;
    private int count=0;
    private TextView book_name, author_name, speed,time_elapsed,time_remaining;
    int time=0,minutes=0,seconds=0;
    TextView audiobook_subtitles;
    int curr=0,mCurrentPosition=0,maxS=0,incr=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiobook);
        Intent intent = getIntent();
        bookName = intent.getStringExtra("bookName");
        cover = findViewById(R.id.audiobook_cover);
        speed = findViewById(R.id.speed);
        book_name = findViewById(R.id.audio_heading);
        skip_back = findViewById(R.id.skip_back);
        skip_front = findViewById(R.id.skip_front);
        toSpeak="";
        author_name = findViewById(R.id.audio_author);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("books/" + bookName.replace(" ", ""));
        GlideApp.with(ActivityAudiobook.this)
                .load(storageReference)
                .into(cover);

        engine = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    engine.setLanguage(Locale.US);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Engine error",Toast.LENGTH_SHORT).show();
                }
            }
        });

        skip_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
                engine.speak("",TextToSpeech.QUEUE_FLUSH, params);
                curr=curr>4?curr-=4:0;
                engine.stop();
                final String t = timer(-15);
                mCurrentPosition--;
                time_elapsed.setText(t);
                speech();
            }
        });

        skip_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curr<maxS-1)
                {
                    curr+=2;
                }
                else
                {
                    curr=maxS;
                }
                engine.stop();
                count+=15;
                final String t = timer(15);
                time_elapsed.setText(t);
                speech();
            }
        });

        engine.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(final String utteranceId) {
                ActivityAudiobook.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if(!utteranceId.equals(" ") && !utteranceId.equals("."))
                            audiobook_subtitles.setText(utteranceId+".");

                        speech();
                    }
                });

            }

            @Override
            public void onDone(String utteranceId) {

            }

            @Override
            public void onError(String utteranceId) {

            }
        });


        seek = findViewById(R.id.song_progress);
        time_elapsed = findViewById(R.id.time_elapsed);
        time_remaining=findViewById(R.id.time_remaining);

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(engine.isSpeaking()) {
                    if(count>incr) {
                        seek.setProgress(mCurrentPosition);
                        mCurrentPosition++;
                        count=0;
                    }
                    final String t = timer(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time_elapsed.setText(t);
                        }
                    });
                }
            }
        },0,1000);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");
        ref.child(bookName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book_name.setText(dataSnapshot.child("bookName").getValue().toString());
                author_name.setText(dataSnapshot.child("author").getValue().toString());
                mp = new ArrayList<>();
                mp = (ArrayList<List<String>>) dataSnapshot.child("mp").getValue();
                AddItemsToRecyclerViewArrayList();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final PlayPauseView view = findViewById(R.id.play_pause_view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.toggle();
                if (!engine.isSpeaking())
                    speech();
                else {
                    curr=curr>1?curr-=2:0;
                    engine.stop();
                }
            }
        });

        audiobook_subtitles = findViewById(R.id.audio_summary_text);

        final ImageView audiobooks_captions = findViewById(R.id.audiobooks_captions);

        audiobooks_captions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audiobook_subtitles.getVisibility()==View.GONE)
                {
                    audiobook_subtitles.setVisibility(View.VISIBLE);
                    cover.setVisibility(View.GONE);
                    audiobooks_captions.setImageResource(R.drawable.baseline_subtitles_24);
                }
                else
                {
                    audiobook_subtitles.setVisibility(View.GONE);
                    cover.setVisibility(View.VISIBLE);
                    audiobooks_captions.setImageResource(R.drawable.baseline_subtitles_off_24);
                }
            }
        });

        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed.getText().toString().equals("1.0x")) {
                    speed.setText("1.25x");
                    engine.setSpeechRate(1.25f);
                } else if (speed.getText().toString().equals("1.25x")) {
                    engine.setSpeechRate(1.5f);
                    speed.setText("1.5x");
                } else if (speed.getText().toString().equals("1.5x")) {
                    engine.setSpeechRate(1.75f);
                    speed.setText("1.75x");
                } else if (speed.getText().toString().equals("1.75x")) {
                    engine.setSpeechRate(2f);
                    speed.setText("2x");
                } else if (speed.getText().toString().equals("2x")) {
                    engine.setSpeechRate(0.5f);
                    speed.setText("0.5x");
                } else if (speed.getText().toString().equals("0.5x")) {
                    engine.setSpeechRate(0.75f);
                    speed.setText("0.75x");
                } else if (speed.getText().toString().equals("0.75x")) {
                    engine.setSpeechRate(1f);
                    speed.setText("1.0x");
                }
            }
        });


        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private String timer(int secs)
    {
        time+=secs;
        if(time<0)
            time=0;
        minutes = time/60;
        seconds=time%60;
        mins=String.valueOf(minutes);
        sec=String.valueOf(seconds);
        if(seconds<10)
            sec="0"+sec;
        count++;
        return (mins+":"+sec);
    }

    public void AddItemsToRecyclerViewArrayList() {
        for (int i = 0; i < mp.size() - 1; i++) {
            toSpeak = toSpeak.concat("Page " + (i + 1) + ". ");
            toSpeak = toSpeak.concat(mp.get(i + 1).get(0));
            toSpeak = toSpeak.concat(" ");
            toSpeak = toSpeak.concat(mp.get(i + 1).get(1));
            toSpeak = toSpeak.concat(". ");
        }
        toSpeak = toSpeak.concat("The end");
        sentences=toSpeak.split("\\.\\s+");
        int s = toSpeak.length() / 15;
        incr = s/100;
        int m = s/60;
        int se =s%60;
        String mi=String.valueOf(m);
        String secs=String.valueOf(se);
        if(se<10)
            secs="0"+secs;
        count++;
        time_remaining.setText(mi+":"+secs);
        maxS=sentences.length;
    }

    private void speech() {
        if(curr>=maxS)
        {
            engine.stop();
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, sentences[curr]);
        engine.speak(sentences[curr], TextToSpeech.QUEUE_ADD, params);
        curr++;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        engine.shutdown();
    }

}

