package br.com.cleilsonandrade.parkapi.util;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LotParkingUtils {
  public static String generatedReceipt() {
    LocalDateTime date = LocalDateTime.now();

    String receipt = date.toString().substring(0, 19);

    return receipt.replace("-", "")
        .replace(":", "")
        .replace("T", "-");
  }
}
