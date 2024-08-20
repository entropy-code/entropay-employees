package com.entropyteam.entropay.employees.calendar;

/**
 * Couldn't find an implementation for this enum in the google API:
 * <a href="https://developers.google.com/apps-script/reference/calendar/event-color">Enum EventColor</a>
 * so I'm creating this enum to represent the colors that can be used in the google calendar.
 */
enum EventColor {
    PALE_BLUE("1"),
    PALE_GREEN("2"),
    MAUVE("3"),
    PALE_RED("4"),
    YELLOW("5"),
    ORANGE("6"),
    CYAN("7"),
    GRAY("8"),
    BLUE("9"),
    GREEN("10"),
    RED("11");

    private final String colorId;

    EventColor(String colorId) {
        this.colorId = colorId;
    }

    public String getColorId() {
        return colorId;
    }
}
