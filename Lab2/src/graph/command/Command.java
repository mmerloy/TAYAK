package graph.command;

import graph.command.errors.IllegalCommandFormatException;

public class Command {
    private String from;
    private Character character;
    private String to;

    public Command(String from, Character character, String to) {
        this.from = from;
        this.character = character;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public Character getCharacter() {
        return character;
    }

    public String getTo() {
        return to;
    }
}