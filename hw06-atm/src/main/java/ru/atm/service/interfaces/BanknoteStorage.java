package ru.atm.service.interfaces;

import ru.atm.entity.Banknote;

import java.util.List;

public interface BanknoteStorage {

    void putBanknote(Banknote banknote);
    List<Banknote> getSumOfBanknotes(int sum);
    int getRemainingSum();
}
