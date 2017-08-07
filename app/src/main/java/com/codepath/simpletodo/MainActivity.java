package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_CURR_TEXT = "KEY_CURR_TEXT";
    private static final String KEY_MODIFIED_TEXT = "KEY_MODIFIED_TEXT";
    private static final int MODIFIED_TEXT_OK = 999;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    int selectTextPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    private void populateArrayItems() {
        items = new ArrayList<>();

        readItems();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");

        writeItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();

                        writeItems();

                        return true;
                    }
                }
        );

        // ====================================================================================================
        // Add up on click listener to select one text for modify
        // ====================================================================================================
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        selectTextPos = pos;
                        editTextData();
                    }
                }
        );
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
//            Toast.makeText(this, "Read Text", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            items = new ArrayList<String>();
        }

            Toast.makeText(this, "Read Text", Toast.LENGTH_LONG).show();


    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ====================================================================================================
    // Get select text and pass it to EditItemActivity using INTEND
    // ====================================================================================================
    private void editTextData() {
        String selectText = itemsAdapter.getItem(selectTextPos);

        Intent editItemIntent = new Intent(getBaseContext(), EditItemActivity.class);
        editItemIntent.putExtra(KEY_CURR_TEXT, selectText);

        startActivityForResult(editItemIntent, 100);
    }

    // ====================================================================================================
    // Receive modified text from EditItemActivity using INTEND
    // and set the modified text to the List
    // ====================================================================================================
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);

        if(resultCode == MODIFIED_TEXT_OK) {
            String modifiedText = resultIntent.getStringExtra(KEY_MODIFIED_TEXT);

            items.remove(selectTextPos);
            itemsAdapter.notifyDataSetChanged();
            items.add(selectTextPos, modifiedText);
            writeItems();
        }
        else {
            Toast.makeText(getBaseContext(), "Error!!!", Toast.LENGTH_SHORT).show();
        }
    }
}