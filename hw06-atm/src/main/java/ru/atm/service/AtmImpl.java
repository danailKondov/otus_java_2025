package ru.atm.service;

import ru.atm.entity.Banknote;
import ru.atm.enums.BanknoteValue;
import ru.atm.service.interfaces.Atm;
import ru.atm.service.interfaces.BanknoteStorage;
import ru.atm.service.validator.SumValidator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AtmImpl implements Atm {

    private static final int MAX_SIZE = 100;

    private final BanknoteStorage storage;
    private final SumValidator sumValidator;

    public AtmImpl() {
        this.storage = new BanknoteStorageImpl(Arrays.stream(BanknoteValue.values())
                .sorted(Comparator.comparing(BanknoteValue::getValue).reversed())
                .collect(Collectors.toMap(
                        Function.identity(),
                        value -> new BanknoteHolderImpl(value, MAX_SIZE),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new)));
        this.sumValidator = new SumValidator();
    }

    @Override
    public void putBanknote(Banknote banknote) {
        storage.putBanknote(banknote);
    }

    @Override
    public List<Banknote> getSum(int sum) {
        sumValidator.checkSum(sum);
        return storage.getSumOfBanknotes(sum);
    }

    @Override
    public int getRemainingSum() {
        return storage.getRemainingSum();
    }
}
