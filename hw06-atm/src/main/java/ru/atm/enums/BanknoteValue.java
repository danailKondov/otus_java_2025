package ru.atm.enums;

import lombok.Getter;

@Getter
public enum BanknoteValue {
    TEN(10, "сто"),
    FIFTY(50, "пятьдесят"),
    ONE_HUNDRED(100, "сто"),
    TWO_HUNDRED(200, "двести"),
    FIVE_HUNDRED(500, "пятьсот"),
    ONE_THOUSAND(1000, "тысяча"),
    TWO_THOUSAND(2000, "две тысячи"),
    FIVE_THOUSAND(5000, "пять тысяч");

    private final int value;
    private final String description;

    BanknoteValue(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
