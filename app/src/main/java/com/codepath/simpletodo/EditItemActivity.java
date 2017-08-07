package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {
    private static final String KEY_CURR_TEXT = "KEY_CURR_TEXT";
    private static final String KEY_MODIFIED_TEXT = "KEY_MODIFIED_TEXT";
    private static final int MODIFIED_TEXT_OK = 999;

    EditText etModifiedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        initEditItemActivity();
    }

    public void onSaveData(View view) {
        String modifiedText = etModifiedText.getText().toString();

        Intent resultIntent = new Intent();

        resultIntent.putExtra(KEY_MODIFIED_TEXT, modifiedText);

        setResult(MODIFIED_TEXT_OK, resultIntent);

        finish();
    }

    private void initEditItemActivity() {
        etModifiedText = (EditText) findViewById(R.id.etModifyText);
        String editText = (String) getIntent().getCharSequenceExtra(KEY_CURR_TEXT);

        etModifiedText.setText(editText);
    }
}
