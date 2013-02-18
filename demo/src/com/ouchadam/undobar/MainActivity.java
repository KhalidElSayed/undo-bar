package com.ouchadam.undobar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements UndoBar.Callback<MainActivity.MyObject>, View.OnClickListener {

    private UndoBar<MyObject> undoBar;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUndoBar();
        initButton();
    }

    private void initUndoBar() {
        ViewGroup parentView = (ViewGroup) findViewById(R.id.parent_view);
        undoBar = new UndoBar<MyObject>(parentView, getLayoutInflater(), this);
    }

    private void initButton() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onUndo(MyObject what) {
        Toast.makeText(this, "onUndo : " + what.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        undoBar.showUndoBar(false, "My undo message", new MyObject());
    }

    public class MyObject {

        private final String message = "Some undo logic!";

        public String getMessage() {
            return message;
        }

    }
}

