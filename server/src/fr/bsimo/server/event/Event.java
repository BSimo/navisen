package fr.bsimo.server.event;

/**
 * Created by BSimo on 30/04/16.
 */
public abstract class Event {
    public String getEventName() {
        return this.getClass().getName();
    }
}
