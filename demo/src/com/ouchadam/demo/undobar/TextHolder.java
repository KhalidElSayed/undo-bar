package com.ouchadam.demo.undobar;

import com.ouchadam.undobar.Undoable;

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
