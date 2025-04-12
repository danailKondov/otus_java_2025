package ru.atm.service;

import ru.atm.entity.Banknote;
import ru.atm.enums.BanknoteValue;
import ru.atm.exceptions.BanknoteHolderEmptyException;
import ru.atm.service.interfaces.BanknoteHolder;
import ru.atm.service.interfaces.BanknoteStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BanknoteStorageImpl implements BanknoteStorage {

    private final Map<BanknoteValue, BanknoteHolder> storage;

    public BanknoteStorageImpl(Map<BanknoteValue, BanknoteHolder> storage) {
        this.storage = storage;
    }

    public void putBanknote(Banknote banknote) {
        storage.get(banknote.banknoteValue())
                .putBanknote(banknote);
    }

    public List<Banknote> getSumOfBanknotes(int sum) {
        check(sum);
        return getBanknotes(sum);
    }

    public int getRemainingSum() {
        return storage.values().stream()
                .mapToInt(BanknoteHolder::getSumAllBanknotes)
                .sum();
    }

    private void check(int sum) {
        checkEnoughFunds(sum);
        checkBanknotesEnough(sum);
    }

    private List<Banknote> getBanknotes(int sum) {
        List<Banknote> result = new ArrayList<>();
        int remainingSum = sum;
        for (BanknoteHolder holder : storage.values()) {
            List<Banknote> banknotes = holder.getBanknotesForSum(remainingSum, true);
            result.addAll(banknotes);
            int sumFromHolder = getSumOfBanknotes(banknotes);
            remainingSum = remainingSum - sumFromHolder;
        }
        return result;
    }

    private void checkBanknotesEnough(int sum) {
        int remainingSum = sum;
        for (BanknoteHolder holder : storage.values()) {
            List<Banknote> banknotes = holder.getBanknotesForSum(remainingSum, false);
            int sumFromHolder = getSumOfBanknotes(banknotes);
            remainingSum = remainingSum - sumFromHolder;
        }
        if (remainingSum != 0) {
            throw new BanknoteHolderEmptyException("Недостаточно банкнот нужного номинала в банкомате");
        }
    }

    private void checkEnoughFunds(int sum) {
        int sumAllBanknotes = getRemainingSum();
        if (sum > sumAllBanknotes) {
            throw new BanknoteHolderEmptyException("Недостаточно средств в банкомате");
        }
    }

    private int getSumOfBanknotes(List<Banknote> banknotes) {
        return banknotes.stream()
                .mapToInt(banknote -> banknote.banknoteValue().getValue())
                .sum();
    }
}
