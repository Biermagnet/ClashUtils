package de.michi.clashutils.input.commands;

public enum CommandPermission {
    USER(0), ADVANCED(1), MODERATOR(2), ADMIN(3), DEV(4);

    CommandPermission(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public boolean hasPermission(int perm) {
        return this.getValue() >= perm;
    }

    public boolean hasPermission(CommandPermission needed) {
        return this.getValue() >= needed.getValue();
    }

    public boolean hasPermission(int current, int needed) {
        return current <= needed;
    }

    public static CommandPermission getPermissionByID(int id) {
        switch (id) {
            case 1:
                return ADVANCED;
            case 2:
                return MODERATOR;
            case 3:
                return ADMIN;
            case 4:
                return DEV;
            default:
                return USER;
        }
    }
}
