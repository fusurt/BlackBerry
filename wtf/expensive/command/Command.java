package wtf.expensive.command;

import wtf.expensive.utility.Utility;
import wtf.expensive.utility.util.ChatUtility;

import java.util.ArrayList;
import java.util.List;

public class Command implements Utility {
    public String command, description;

    public Command() {
        command = this.getClass().getAnnotation(CommandAnnotation.class).name();
        description = this.getClass().getAnnotation(CommandAnnotation.class).description();
    }

    public void run(String[] args) {

    }

    public void sendMessage(String message) {
        ChatUtility.addChatMessage(message);
    }

    public void error() {

    }

}
