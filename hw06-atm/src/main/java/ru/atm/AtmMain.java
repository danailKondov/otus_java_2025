package ru.atm;

import ru.atm.entity.Banknote;
import ru.atm.enums.BanknoteValue;
import ru.atm.service.AtmUI;

import java.util.List;

public class AtmMain {

    public static void main(String[] args) {
        AtmUI atmUI = new AtmUI();

        atmUI.putBanknote(new Banknote(BanknoteValue.FIFTY, "sn1"));
        atmUI.putBanknote(new Banknote(BanknoteValue.FIVE_HUNDRED, "sn2"));
        atmUI.putBanknote(new Banknote(BanknoteValue.ONE_HUNDRED, "sn3"));
        atmUI.putBanknote(new Banknote(BanknoteValue.TWO_THOUSAND, "sn3"));

        List<Banknote> sum = atmUI.getSum(550);
        System.out.println(sum);
        System.out.println(atmUI.getRemainingSum());
    }
}
