package ru.atm.service;

import ru.atm.entity.Banknote;
import ru.atm.service.interfaces.Atm;

import java.util.List;

public class AtmUI {

    private final Atm atm;

    public AtmUI() {
        this.atm = new AtmImpl();
    }

    public void putBanknote(Banknote banknote) {
        atm.putBanknote(banknote);
    }

    public List<Banknote> getSum(int sum) {
        return atm.getSum(sum);
    }

    public int getRemainingSum() {
        return atm.getRemainingSum();
    }
}
