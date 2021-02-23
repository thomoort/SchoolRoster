package com.company;

import java.util.Locale;

public class Period {

    public enum Day {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY
    }

    public enum Block {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }


    private Day day;
    private Block block;

    public Period(Day day, Block block) {
        this.day = day;
        this.block = block;
    }

    public Day getDay() {
        return day;
    }

    public Block getBlock() {
        return block;
    }

    public String getBlockString() {
        switch (block) {
            case FIRST -> {
                return "8:00 - 9:30";
            }
            case SECOND -> {
                return "9:45 - 11:15";
            }
            case THIRD -> {
                return "11:30 - 13:00";
            }
            case FOURTH -> {
                return "14:30 - 16:00";
            }
            default -> {
                return "No Block";
            }
        }
    }

    public String getDayString() {
        switch (day) {
            case MONDAY -> {
                return "Monday";
            }
            case TUESDAY -> {
                return "Tuesday";
            }
            case WEDNESDAY -> {
                return "Wednesday";
            }
            case THURSDAY -> {
                return "Thursday";
            }
            case FRIDAY -> {
                return "Friday";
            }
            default -> {
                return "No Day";
            }
        }
    }

    public static int getBlockInt(Block block) {
        switch (block) {
            case FIRST -> {
                return 0;
            }
            case SECOND -> {
                return 1;
            }
            case THIRD -> {
                return 2;
            }
            case FOURTH -> {
                return 3;
            }
            default -> {
                return -1;
            }
        }
    }


    public static int getDayInt(Day day) {
        switch (day) {
            case MONDAY -> {
                return 0;
            }
            case TUESDAY -> {
                return 1;
            }
            case WEDNESDAY -> {
                return 2;
            }
            case THURSDAY -> {
                return 3;
            }
            case FRIDAY -> {
                return 4;
            }
            default -> {
                return -1;
            }
        }
    }

    @Override
    public String toString() {
        return "Period is from %s on %s.".formatted(getBlockString(), getDayString().toLowerCase(Locale.ROOT));
    }
}
