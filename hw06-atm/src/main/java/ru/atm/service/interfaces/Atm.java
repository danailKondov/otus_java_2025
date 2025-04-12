package ru.atm.service.interfaces;

import ru.atm.entity.Banknote;

import java.util.List;

public interface Atm {

    void putBanknote(Banknote banknote);
    List<Banknote> getSum(int sum);
    int getRemainingSum();
}
