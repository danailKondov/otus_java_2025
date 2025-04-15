package ru.atm.service.validator;


import java.util.List;

public class SumValidator {

    private final List<SumCheck> checkers;

    public SumValidator() {
        this.checkers = List.of(new SumNotZeroCheck(), new SumAvailableCheck());
    }

    public void checkSum(int sum) {
        checkers.forEach(checker -> checker.check(sum));
    }
}
