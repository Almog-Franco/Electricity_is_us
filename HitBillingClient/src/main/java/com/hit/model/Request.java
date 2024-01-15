package com.hit.model;

import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String header;
    private Map<String, String> body;

    public Request(String header, Map<String, String> body) {
        this.header = header;
        this.body = body;
    }

    public Request() {

    }

    public Map<String, String> getBody() {
        return this.body;
    }

    public void addToBody(String key,String value) {
        this.body.put(key,value);
    }

    public String getHeaders() {
        return this.header;
    }

    public String getHeader(){
        return this.header;
    }


    public String toString() {
        return "Request [header =" + this.header + ", body =" + this.body + "]";
    }
}
