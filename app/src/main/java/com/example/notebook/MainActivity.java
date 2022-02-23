package com.example.notebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText title, notes;
    Button insert, update, delete, deleteall, view, viewall;
    DBHelper DB;
    private static final int REQUEST_CODE_SPEECH = 1000;
    private static final int REQUEST_CODE_SPEECH2 = 1001;
    String[] micperm;
    final int STORAGE_REQ_CODE = 5788;

    TextToSpeech textToSpeech, textToSpeech2;
    ImageButton voicebtn, voicebtn2, delbtn1, delbtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.nametext);
        notes = findViewById(R.id.notestext);

        insert = findViewById(R.id.btninsert);
        update = findViewById(R.id.btnupdate);
        delete = findViewById(R.id.btndelete);
        deleteall = findViewById(R.id.btndeleteall);
        view = findViewById(R.id.btnview);
        viewall = findViewById(R.id.btnviewall);

        micperm = new String[]{Manifest.permission.RECORD_AUDIO};

        DB = new DBHelper(this);

        //new text to speech for arabic language only
        textToSpeech = new TextToSpeech(getApplicationContext() , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    //select lang
                    Locale loc = new Locale("ar-EG");
                    int lang = textToSpeech.setLanguage(loc);
                } }
        });

        //new text to speech for english language only
        textToSpeech2 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    //select lang
                    int lang = textToSpeech2.setLanguage(Locale.ENGLISH);
                } }
        });

        voicebtn = (ImageButton) findViewById(R.id.imgbtn);
        voicebtn2 = (ImageButton) findViewById(R.id.imgbtn2);

        delbtn1= (ImageButton) findViewById(R.id.remove1);
        delbtn2= (ImageButton) findViewById(R.id.remove2);

        //permission of mic
        checkPermission();
        //initialize speech recognizer
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "ar_EG");
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) { }
            @Override
            public void onBeginningOfSpeech() { }
            @Override
            public void onRmsChanged(float v) { }
            @Override
            public void onBufferReceived(byte[] bytes) { }
            @Override
            public void onEndOfSpeech() { }
            @Override
            public void onError(int i) { }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying all matches
                if (matches != null)
                    title.append(" "+ matches.get(0));
            }
            @Override
            public void onPartialResults(Bundle bundle) { }
            @Override
            public void onEvent(int i, Bundle bundle) { }
        });

        voicebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //when button is not pressed
                        mSpeechRecognizer.stopListening();
                        title.setHint("العنوان..");
                        break;
                    //when button is pressed
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        title.setHint("تسجيل...");
                        break;
                }
                return false;
            } });

        //initialize speech recognizer
        final SpeechRecognizer mSpeechRecognizer1 = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //detect arabic language
        mSpeechRecognizerIntent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "ar_EG");
        mSpeechRecognizer1.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) { }
            @Override
            public void onBeginningOfSpeech() { }
            @Override
            public void onRmsChanged(float v) { }
            @Override
            public void onBufferReceived(byte[] bytes) { }
            @Override
            public void onEndOfSpeech() { }
            @Override
            public void onError(int i) { }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches1 = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying all matches
                if (matches1 != null)
                    notes.append(" "+ matches1.get(0));
            }
            @Override
            public void onPartialResults(Bundle bundle) { }
            @Override
            public void onEvent(int i, Bundle bundle) { }
        });
        voicebtn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //when button is not pressed
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer1.stopListening();
                        notes.setHint("الموضوع..");
                        break;
                    //when button is pressed
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer1.startListening(mSpeechRecognizerIntent1);
                        notes.setHint("تسجيل...");
                        break;
                }
                return false;
            } });

        //to remove any words in the edit text
        delbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               title.setText("");
            }
        });
        delbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.setText("");
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String noteText = notes.getText().toString();

                //call insertdata
                Boolean checkinsertion = DB.insertdata(titleText,noteText);
                if (checkinsertion == true && !titleText.isEmpty() && !noteText.isEmpty()) {
                            Toast.makeText(MainActivity.this, "New Note Inserted", Toast.LENGTH_SHORT).show();
                            int speech = textToSpeech2.speak("New Note Inserted",TextToSpeech.QUEUE_FLUSH,null);
                }else {
                    Toast.makeText(MainActivity.this, "New Note Not Inserted, Title and note can't be empty", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("New Note Not Inserted, Title and note can't be empty",TextToSpeech.QUEUE_FLUSH,null);
                }
                textToSpeech.stop();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String noteText = notes.getText().toString();

                //call updatedata
                Boolean checkupdate= DB.updatedata(titleText,noteText);
                if (checkupdate == true) {
                    Toast.makeText(MainActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("Note Updated",TextToSpeech.QUEUE_FLUSH,null);
                }else {
                    Toast.makeText(MainActivity.this, "Note Not Updated, title must not be empty", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("Note Not Updated, title must not be empty",TextToSpeech.QUEUE_FLUSH,null);
                }
                textToSpeech.stop();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();

                //call deletedata
                Boolean checkdeletion = DB.deletedata(titleText);
                if (checkdeletion == true) {
                    Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("Note Deleted",TextToSpeech.QUEUE_FLUSH,null);
                }else {
                    Toast.makeText(MainActivity.this, "Note Not Deleted, title must not be empty", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("Note Not Deleted, title must not be empty",TextToSpeech.QUEUE_FLUSH,null);
                }
                textToSpeech.stop();
            }
        });

        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call deletealldata
                Boolean checkdeletion = DB.deletealldata();
                if (checkdeletion == true) {
                    Toast.makeText(MainActivity.this, "Notes Deleted", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("Notes Deleted",TextToSpeech.QUEUE_FLUSH,null);
                }else {
                    Toast.makeText(MainActivity.this, "Notes Not Deleted, title must not be empty", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("Notes Not Deleted, title must not be empty",TextToSpeech.QUEUE_FLUSH,null);
                }
                textToSpeech.stop();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();

                //call viewdata
                Cursor res = DB.viewdata(titleText);

                //if there is no notes
                if (res.getCount() == 0){
                    Toast.makeText(MainActivity.this, "No Notes available", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("No Notes available",TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    //put title in column of index 0 and note in column of index 1 in a buffer
                    buffer.append("العنوان: "+ res.getString(0) +"\n");
                    buffer.append("الموضوع:  "+ res.getString(1) +"\n\n");
                }

                //show buffer in dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Note ");
                builder.setMessage(buffer.toString());
                builder.show();

                //speech of title and notes in rate 0.5
                textToSpeech.setSpeechRate((float) 0.5);
                //flush to clear the queue
                int speech = textToSpeech.speak(buffer.toString(),TextToSpeech.QUEUE_FLUSH,null);

            }
        });

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call viewalldata
                Cursor res = DB.viewalldata();

                //if there is no notes
                if (res.getCount() == 0){
                    Toast.makeText(MainActivity.this, "No Notes available", Toast.LENGTH_SHORT).show();
                    int speech = textToSpeech2.speak("No Notes available",TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    //put title in column of index 0 and note in column of index 1 in a buffer
                    buffer.append("العنوان: "+ res.getString(0) +" \n");
                    buffer.append("الموضوع: "+ res.getString(1) +" \n\n");
                }

                //show buffer in dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Notes ");
                builder.setMessage(buffer.toString());
                builder.show();

                //speech of title and notes in rate 0.5
                textToSpeech.setSpeechRate((float) 0.5);
               //flush to clear the queue
                int speech = textToSpeech.speak(buffer.toString(),TextToSpeech.QUEUE_FLUSH,null);
            }  });
    }

    @Override
    protected void onPause() {
        textToSpeech.stop();
        super.onPause();
    }

    @Override
    protected void onStop() {
        textToSpeech.stop();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, micperm, STORAGE_REQ_CODE);
            } }
    }
}
//call speak function when press on title mic
       /* voicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        //call speak2 function when press on notes mic
        voicebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak2();
            }
        });*/

 /*   private void speak(){
        int speech = textToSpeech2.speak("Title",TextToSpeech.QUEUE_FLUSH,null);

        //intent to show speech of text
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar_EG");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        //start intent
        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH);
        }catch (Exception e){
            //if there is error
            Toast.makeText(MainActivity.this, " "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            int speech1 = textToSpeech2.speak(" "+e.getMessage(),TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    private void speak2(){
        int speech = textToSpeech2.speak("Note",TextToSpeech.QUEUE_FLUSH,null);

        //intent to show speech of text
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar_EG");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        //start intent
        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH2);
        }catch (Exception e){
            //if there is error
            Toast.makeText(MainActivity.this, " "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            int speech1 = textToSpeech2.speak(" "+e.getMessage(),TextToSpeech.QUEUE_FLUSH,null);
        }
    }
*/
//receive voice in edit text
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            //request code of title mic
            case REQUEST_CODE_SPEECH:{
                if(resultCode == RESULT_OK && null!= data){
                    //collect spoken text in array list
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to edit text
                    String results = result.get(0);
                    if(title.getText().toString() != null){
                        title.append(" "+results);
                    }
                    //title.setText(results);
                    Toast.makeText(this, "text is created", Toast.LENGTH_SHORT).show();
                    int speech1 = textToSpeech2.speak("text is created",TextToSpeech.QUEUE_FLUSH,null);
                }
                break;
            }
            //request code of notes mic
            case REQUEST_CODE_SPEECH2:{
                if(resultCode == RESULT_OK && null!= data){
                    //collect spoken text in array list
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to edit text
                    String results = result.get(0);
                    if(notes.getText().toString() != null){
                        notes.append(" "+results);
                    }
                    Toast.makeText(this, "text is created", Toast.LENGTH_SHORT).show();
                    int speech1 = textToSpeech2.speak("text is created",TextToSpeech.QUEUE_FLUSH,null);
                }
                break;
            }
        } }*/