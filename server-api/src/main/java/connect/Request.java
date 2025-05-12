package connect;

import commands.CommandType;

import java.io.Serializable;

public class Request implements Serializable {
    private CommandType command;
    private Object data;

    public Request(CommandType command) {
        this.command = command;
    }

    public Request(CommandType command, Object data) {
        this.command = command;
        this.data = data;
    }

    public CommandType getCommand() {
        return command;
    }

    public Object getData() {
        return data;
    }

    public void setCommand(CommandType command) {
        this.command = command;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
