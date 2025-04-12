package ru.atm.service.validator;

import ru.atm.exceptions.RequiredSumIsNotAvailable;

public class SumAvailableCheck implements SumCheck {

    @Override
    public void check(int sum) {
        if (sum % 10 != 0) {
            throw new RequiredSumIsNotAvailable("Запрошенная сумма должна быть кратна 10");
        }
    }
}
