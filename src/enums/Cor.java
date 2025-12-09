package enums;

public enum Cor {
	BRANCO("\u001b[37m"),
    AZUL("\u001B[34m"),
    VERMELHO("\u001B[31m"),
    AMARELO("\u001B[33m"),
    VERDE("\u001B[32m"),
    ROSA("\u001b[95m"),
    RESET("\u001B[0m");

    private final String ansiCode;

    Cor(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    public String getAnsiCode() {
        return ansiCode;
    }
}