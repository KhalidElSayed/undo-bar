package com.ouchadam.undobar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

public class UndoBar<T extends Undoable> implements View.OnClickListener {

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

    public UndoBar(Activity activity, Callback<T> callback) {
        this(getViewGroup(activity), callback);
    }

    private static ViewGroup getViewGroup(Activity activity) {
        return (ViewGroup) activity.findViewById(android.R.id.content);
    }

    public UndoBar(ViewGroup viewToAttachTo, Callback<T> callback) {
        this(inflateUndoBar(viewToAttachTo), callback);
    }

    private static View inflateUndoBar(ViewGroup viewToAttachTo) {
        View view = getLayoutInflater(viewToAttachTo.getContext()).inflate(R.layout.undo_bar, viewToAttachTo);
        return view.findViewById(R.id.undobar);
    }

    private static LayoutInflater getLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public UndoBar(View undoBarView, Callback<T> undoCallback) {
        this.animator = undoBarView.animate();
        this.undoBarView = undoBarView;
        this.undoCallback = undoCallback;
        messageView = (TextView) undoBarView.findViewById(R.id.undobar_message);
        setUndoClickListener();
    }

    private void setUndoClickListener() {
        undoBarView.findViewById(R.id.undobar_button).setOnClickListener(this);
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
        } else {
            removeBarView();
        }
    }

    private void removeBarView() {
        undoBarView.setVisibility(View.GONE);
        undoBarView.setAlpha(ALPHA_MIN);
    }

    private void animateFadeOut() {
        setAnimationDuration(ALPHA_MIN).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                undoBarView.setVisibility(View.GONE);
            }
        });
    }

    public void show(T previousState) {
        what = previousState;
        messageView.setText(previousState.label());
        setupHandler();
        displayUndoView();
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

    private void displayUndoView() {
        undoBarView.setVisibility(View.VISIBLE);
        animateIntoView();
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
