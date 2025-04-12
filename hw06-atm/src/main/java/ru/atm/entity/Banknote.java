package ru.atm.entity;

import ru.atm.enums.BanknoteValue;

public record Banknote(BanknoteValue banknoteValue, String serialNumber) {}
