package com.notapp.myapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends Activity {

    EditText etNote;
    Button btnAdd;
    ListView listNotes;
    ArrayList<String> notes = new ArrayList<>();
    ArrayAdapter<String> adapter;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNote = findViewById(R.id.etNote);
        btnAdd = findViewById(R.id.btnAdd);
        listNotes = findViewById(R.id.listNotes);
        prefs = getSharedPreferences("notes", MODE_PRIVATE);

        loadNotes();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notes);
        listNotes.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = etNote.getText().toString().trim();
                if (!text.isEmpty()) {
                    notes.add(text);
                    adapter.notifyDataSetChanged();
                    saveNotes();
                    etNote.setText("");
                }
            }
        });

        listNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> p, View v, int pos, long id) {
                notes.remove(pos);
                adapter.notifyDataSetChanged();
                saveNotes();
                Toast.makeText(MainActivity.this, "تم الحذف", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    void saveNotes() {
        prefs.edit().putString("data", String.join("\n§§\n", notes)).apply();
    }

    void loadNotes() {
        String data = prefs.getString("data", "");
        if (!data.isEmpty()) {
            for (String s : data.split("\n§§\n")) notes.add(s);
        }
    }
}