package com.ouchadam.undobar;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

public class UndoBar<T> implements View.OnClickListener {

    private static final int ALPHA_MAX = 1;
    private static final int ALPHA_MIN = 0;

    private final Handler hideUndoHandler = new Handler();
    private final View undoBarView;
    private final TextView messageView;
    private final Callback<T> undoCallback;
    private final ViewPropertyAnimator animator;

    private T what;

    public interface Callback<T> {
        void onUndo(T what);
    }

    public UndoBar(ViewGroup viewToAttachTo, LayoutInflater inflater, Callback<T> callback) {
        this(inflateUndoBar(viewToAttachTo, inflater), callback);
    }

    private static View inflateUndoBar(ViewGroup viewToAttachTo, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.undo_bar, viewToAttachTo);
        return view.findViewById(R.id.undobar);
    }

    public UndoBar(View undoBarView, Callback<T> undoCallback) {
        this.animator = undoBarView.animate();
        this.undoBarView = undoBarView;
        this.undoCallback = undoCallback;
        messageView = (TextView) undoBarView.findViewById(R.id.undobar_message);
        setUndoClickListener();
        hideUndoBarImmediately();
    }

    private void setUndoClickListener() {
        undoBarView.findViewById(R.id.undobar_button).setOnClickListener(this);
    }

    private void hideUndoBarImmediately() {
        hideUndoBar(true);
    }

    @Override
    public void onClick(View v) {
        hideUndoBar(false);
        undoCallback.onUndo(what);
    }

    public void hideUndoBar(boolean immediate) {
        removePreviousHandlerCallback();
        if (!immediate) {
            animator.cancel();
            animateFadeOut();
        }
        removeBarView();
    }

    private void removeBarView() {
        undoBarView.setVisibility(View.GONE);
        undoBarView.setAlpha(ALPHA_MIN);
    }

    private void animateFadeOut() {
        setAnimationDuration(ALPHA_MIN);
    }

    public void showUndoBar(boolean immediate, CharSequence message, T previousState) {
        what = previousState;
        messageView.setText(message);
        setupHandler();
        displayUndoView(immediate);
    }

    private void setupHandler() {
        removePreviousHandlerCallback();
        setRunnableToHandler(mHideRunnable);
    }

    private void removePreviousHandlerCallback() {
        hideUndoHandler.removeCallbacks(mHideRunnable);
    }

    private boolean setRunnableToHandler(Runnable runnable) {
        return hideUndoHandler.postDelayed(runnable, undoBarView.getResources().getInteger(R.integer.undobar_hide_delay_ms));
    }

    private void displayUndoView(boolean immediate) {
        undoBarView.setVisibility(View.VISIBLE);
        if (!immediate) {
            animateIntoView();
        } else {
            undoBarView.setAlpha(ALPHA_MAX);
        }
    }

    private void animateIntoView() {
        animator.cancel();
        setAnimationDuration(ALPHA_MAX);
    }

    private ViewPropertyAnimator setAnimationDuration(float toAlpha) {
        return animator.alpha(toAlpha).setDuration(undoBarView.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideUndoBar(false);
        }
    };

}
