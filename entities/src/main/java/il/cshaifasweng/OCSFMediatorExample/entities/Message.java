package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
public class Message implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String message;
    private Object object;

    public Message() {
    }

    public Message(final String msg, final Object obj) {
        this.message = msg;
        this.object = obj;
    }

    public Message(final String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getObject() {
        return this.object;
    }

    public void setMessage(final String msg) {
        this.message = msg;
    }

    public void setObject(final Object obj) {
        this.object = obj;
    }
    public String getData(){
        return "";
    }

    @Override
    public String toString() {
        return this.message;
    }
}