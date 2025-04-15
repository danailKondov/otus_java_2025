package ru.atm.service.validator;

import ru.atm.exceptions.RequiredSumIsZeroException;

public class SumNotZeroCheck implements SumCheck{

    @Override
    public void check(int sum) {
        if (sum == 0) {
            throw new RequiredSumIsZeroException("Запрошенная сумма равна нулю");
        }
    }
}
