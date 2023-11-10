package br.com.internet.bank.Utils;

import java.math.BigDecimal;

public class CalculateUtils {

    private CalculateUtils(){}

    public static BigDecimal calculateRate(BigDecimal numValue, Boolean isPlanoExclusive) {
        if(numValue.doubleValue() <= 100 || isPlanoExclusive)
            return numValue;
        if(numValue.doubleValue() > 100 && numValue.doubleValue() <= 300)
            return numValue.add(percentage(numValue, 4));
        if(numValue.doubleValue() > 300)
            return numValue.add(percentage(numValue, 1));
        return null;
    }

    public static BigDecimal percentage(BigDecimal calc, Integer porce) {
        double calcResult = calc.doubleValue() * porce / 100;
        return BigDecimal.valueOf(calcResult);
    }
}
