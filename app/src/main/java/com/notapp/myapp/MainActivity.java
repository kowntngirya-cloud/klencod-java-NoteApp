package com.notapp.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends Activity {

    static final String SEP = "\n§§\n";
    static final int[] COLORS = {
            0xFF6C4DF6, 0xFF14B8A6, 0xFFF59E0B, 0xFFEC4899, 0xFF3B82F6
    };

    EditText etNote;
    Button btnAdd;
    ListView listNotes;
    TextView tvCount, tvEmpty;
    ArrayList<String> notes = new ArrayList<>();
    NoteAdapter adapter;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNote = findViewById(R.id.etNote);
        btnAdd = findViewById(R.id.btnAdd);
        listNotes = findViewById(R.id.listNotes);
        tvCount = findViewById(R.id.tvCount);
        tvEmpty = findViewById(R.id.tvEmpty);
        prefs = getSharedPreferences("notes", MODE_PRIVATE);

        loadNotes();
        adapter = new NoteAdapter(this);
        listNotes.setAdapter(adapter);
        refreshUi();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = etNote.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(MainActivity.this, "اكتب شيئاً أولاً", Toast.LENGTH_SHORT).show();
                    return;
                }
                notes.add(0, text);
                etNote.setText("");
                saveNotes();
                refreshUi();
            }
        });
    }

    void refreshUi() {
        adapter.notifyDataSetChanged();
        tvCount.setText(notes.size() + " ملاحظات");
        tvEmpty.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    void saveNotes() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notes.size(); i++) {
            if (i > 0) sb.append(SEP);
            sb.append(notes.get(i));
        }
        prefs.edit().putString("data", sb.toString()).apply();
    }

    void loadNotes() {
        String data = prefs.getString("data", "");
        if (!data.isEmpty()) {
            for (String s : data.split(SEP)) notes.add(s);
        }
    }

    class NoteAdapter extends BaseAdapter {
        LayoutInflater inflater;

        NoteAdapter(Context c) { inflater = LayoutInflater.from(c); }

        public int getCount() { return notes.size(); }
        public Object getItem(int i) { return notes.get(i); }
        public long getItemId(int i) { return i; }

        public View getView(final int pos, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.item_note, parent, false);

            ((TextView) convertView.findViewById(R.id.tvNote)).setText(notes.get(pos));
            convertView.findViewById(R.id.strip)
                    .setBackgroundColor(COLORS[pos % COLORS.length]);

            convertView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    notes.remove(pos);
                    saveNotes();
                    refreshUi();
                    Toast.makeText(MainActivity.this, "تم الحذف", Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }
}