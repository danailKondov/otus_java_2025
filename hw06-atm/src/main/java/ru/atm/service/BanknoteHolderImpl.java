package ru.atm.service;

import ru.atm.entity.Banknote;
import ru.atm.enums.BanknoteValue;
import ru.atm.exceptions.BanknoteHolderFilledInException;
import ru.atm.service.interfaces.BanknoteHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BanknoteHolderImpl implements BanknoteHolder {

    private final BanknoteValue banknoteValue;
    private final List<Banknote> banknotes;

    private final int maxSize;

    public BanknoteHolderImpl(BanknoteValue banknoteValue, int maxSize) {
        this.banknoteValue = banknoteValue;
        this.maxSize = maxSize;
        this.banknotes = new ArrayList<>();
    }

    public void putBanknote(Banknote banknote) {
        if (maxSize > banknotes.size()) {
            banknotes.add(banknote);
        } else {
            throw new BanknoteHolderFilledInException("Заполнено хранилище банкнот номиналом " + banknoteValue.getDescription());
        }
    }

    public List<Banknote> getBanknotesForSum(int sum, boolean removeFromHolder) {
        int value = banknoteValue.getValue();
        int banknoteCount = banknotes.size();
        int countNeeded = sum/value;

        if (countNeeded != 0 && banknoteCount != 0) {
            int count = Math.min(banknoteCount, countNeeded);
            List<Banknote> banknoteResult = new ArrayList<>(banknotes.subList(0, count));
            if (removeFromHolder) {
                banknotes.removeIf(banknoteResult::contains);
            }
            return banknoteResult;
        }
        return Collections.emptyList();
    }

    public int getSumAllBanknotes() {
        return banknoteValue.getValue() * banknotes.size();
    }
}
