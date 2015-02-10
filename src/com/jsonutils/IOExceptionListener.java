package com.jsonutils;

import java.io.IOException;

/**
 * Created by CM on 1/25/2015.
 */
public interface IOExceptionListener {
    void onIOError(IOException e);
}
