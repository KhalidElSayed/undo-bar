package com.ouchadam.undobar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements UndoBar.Callback<MainActivity.TextHolder>, View.OnClickListener {

    private UndoBar<TextHolder> undoBar;
    private TextView label;
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
    }

    private void initViews() {
        label = (TextView) findViewById(R.id.label);
        editText = (EditText) findViewById(R.id.edit_text);
        initUndoBar();
        initSetTextButton();
    }

    private void initUndoBar() {
        ViewGroup parentView = (ViewGroup) findViewById(R.id.undobar_container);
        undoBar = new UndoBar<TextHolder>(parentView, getLayoutInflater(), this);
    }

    private void initSetTextButton() {
        Button setTextButton = (Button) findViewById(R.id.button);
        setTextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String newText = editText.getText().toString();
        String previousText = label.getText().toString();
        label.setText(newText);
        undoBar.showUndoBar(false, "Undo set text?", new TextHolder(previousText));
    }

    @Override
    public void onUndo(TextHolder what) {
        Toast.makeText(this, "set text to : " + what.getMessage(), Toast.LENGTH_SHORT).show();
        label.setText(what.getMessage());
    }

    public class TextHolder {

        private final String message;

        public TextHolder(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }

}

