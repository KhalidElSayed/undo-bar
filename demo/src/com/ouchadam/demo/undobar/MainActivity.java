package com.ouchadam.demo.undobar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ouchadam.undobar.R;
import com.ouchadam.undobar.UndoBar;
import com.ouchadam.undobar.Undoable;

public class MainActivity extends Activity implements View.OnClickListener {

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
        initSetTextButton();
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

        showUndoBar(new TextHolder(previousText));
    }

    private void showUndoBar(TextHolder textHolder) {
        new UndoBar<TextHolder>(this, undoCallback).show(textHolder);
    }

    private final UndoBar.Callback<MainActivity.TextHolder> undoCallback = new UndoBar.Callback<TextHolder>() {
        @Override
        public void onUndo(TextHolder what) {
            Toast.makeText(MainActivity.this, "set text to : " + what.getMessage(), Toast.LENGTH_SHORT).show();
            label.setText(what.getMessage());
        }
    };

    public class TextHolder implements Undoable {

        private final String message;

        public TextHolder(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String label() {
            return "Text set.";
        }

    }

}

