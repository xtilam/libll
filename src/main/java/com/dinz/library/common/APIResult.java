package com.dinz.library.common;

import org.springframework.data.util.Pair;

public class APIResult {
    private APIResultMessage message;
    private Object data;

    public APIResult(APIResultMessage message, Object data) {
        this.message = message;
        this.data = data;
    }
    
    public APIResult(Pair<Integer, String> message, Object data) {
        this.message = APIResultMessage.of(message);
        this.data = data;
    }
    
    public APIResult(Pair<Integer, String> message, String messageContent, Object data) {
        this.message = APIResultMessage.of(message);
        this.message.setContent(messageContent);
        this.data = data;
    }

    public APIResultMessage getMessage() {
        return message;
    }

    public void setMessage(APIResultMessage message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}