package com.dev.agent.enums;

public enum CommandEnum {
    LS("ls"),
    MKDIR("mkdir"),
    RM("rm"),
    DOWN("down"),
    COPY("copy"),
    RENAME("rename"),
    OPEN("open"),
    SAVE("save"),
    LISTDOWN("listdown");
    private final String command;

    CommandEnum(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static CommandEnum fromString(String cmd) {
        for (CommandEnum c : CommandEnum.values()) {
            if (c.command.equalsIgnoreCase(cmd)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Comando inválido: " + cmd);
    }
}
