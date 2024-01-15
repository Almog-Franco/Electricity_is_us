package com.hit.server;

import java.util.Map;

public class Response {
    private String header;
    private Map<String, Object> body;

    public Response(String header, Map<String, Object> body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public boolean addValues(String key,Object value){
        if(key != null && value != null){
            this.body.put(key,value);
            return true;
        }

        return false;
    }

    public String toString(){
        return ("Response " + "[header= " + this.header + ", body=" + this.body );
    }
}
