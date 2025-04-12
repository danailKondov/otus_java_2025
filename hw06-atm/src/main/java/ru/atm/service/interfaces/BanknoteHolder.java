package ru.atm.service.interfaces;

import ru.atm.entity.Banknote;

import java.util.List;

public interface BanknoteHolder {

    void putBanknote(Banknote banknote);
    List<Banknote> getBanknotesForSum(int sum, boolean removeFromHolder);
    int getSumAllBanknotes();
}
