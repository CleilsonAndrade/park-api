package br.com.cleilsonandrade.parkapi.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LotParkingUtils {
  private static final double FIRST_15_MINUTES = 5.00;
  private static final double FIRST_60_MINUTES = 9.25;
  private static final double ADDITIONAL_15_MINUTES = 1.75;

  private static final double DISCOUNT_PERCENTAGE = 0.30;

  public static String generatedReceipt() {
    LocalDateTime date = LocalDateTime.now();

    String receipt = date.toString().substring(0, 19);

    return receipt.replace("-", "")
        .replace(":", "")
        .replace("T", "-");
  }

  public static BigDecimal calculateCost(LocalDateTime entry, LocalDateTime sale) {
    long minutes = entry.until(sale, ChronoUnit.MINUTES);
    double total = 0.0;

    if (minutes <= 15) {
      total = FIRST_15_MINUTES;
    } else if (minutes <= 60) {
      total = FIRST_60_MINUTES;
    } else {
      long adicionalMinutes = minutes - 60;
      Double totalParts = ((double) adicionalMinutes / 15);
      if (totalParts > totalParts.intValue()) {
        total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * (totalParts.intValue() + 1));
      } else {
        total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * totalParts.intValue());
      }
    }

    return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
  }

  public static BigDecimal calculateDiscount(BigDecimal cost, long numberTimes) {
    BigDecimal desconto = ((numberTimes > 0) && (numberTimes % 10 == 0))
        ? cost.multiply(new BigDecimal(DISCOUNT_PERCENTAGE))
        : new BigDecimal(0);
    return desconto.setScale(2, RoundingMode.HALF_EVEN);
  }
}
