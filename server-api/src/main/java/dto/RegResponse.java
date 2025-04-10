package dto;

public class RegResponse {

    public boolean state;

    public RegResponse(boolean state) {
        this.state = state;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
