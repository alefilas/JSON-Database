package server.JSON;

import com.google.gson.JsonElement;

public class Answer {

    private String response;
    private String reason;
    private JsonElement value;

    public Answer(String response) {
        this.response = response;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }
}
