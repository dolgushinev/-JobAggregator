package util;

enum Month {

    JANUARY("янв", 1),
    FEBRUARY("фев", 2),
    MARCH("мар", 3),
    APRIL("апр", 4),
    MAY("май", 5),
    JUNE("июн", 6),
    JULY("июл", 7),
    AUGUST("авг", 8),
    SEPTEMBER("сен", 9),
    OCTOBER("окт", 10),
    NOVEMBER("ноя", 11),
    DECEMBER("дек", 12);

    private String name;
    private int number;

    Month(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}