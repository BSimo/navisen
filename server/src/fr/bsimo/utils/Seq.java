package fr.bsimo.utils;

import java.io.Serializable;

/**
 * Created by BSimo on 30/04/16.
 */
public class Seq implements Serializable {
    private int next = 1;
    public synchronized int getNext() {
        return next++;
    }
}