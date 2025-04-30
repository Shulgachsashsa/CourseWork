package connect;

import java.io.Serializable;

public class Response implements Serializable {
    private int state;
    private Object data;

    public Response(int state, Object data) {
        this.state = state;
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public Object getData() { return data; }

    public void setState(int state) {
        this.state = state;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
