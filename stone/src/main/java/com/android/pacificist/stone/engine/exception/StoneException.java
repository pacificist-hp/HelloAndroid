package com.android.pacificist.stone.engine.exception;

import com.android.pacificist.stone.engine.ASTree;

public class StoneException extends RuntimeException {
    public StoneException(String message) {
        super(message);
    }

    public StoneException(String message, ASTree t) { // Overload function
        super(message + " " + t.location);
    }
}
