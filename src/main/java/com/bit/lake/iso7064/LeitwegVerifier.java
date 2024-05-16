/*
 * Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package com.bit.lake.iso7064;

import java.util.Arrays;

public class LeitwegVerifier {
  private static final int MODULUS = 97;
  private static final int RADIX = 10;
  private static final String CHARSET = "0123456789";

  public static void main(String[] args) {
    final var givenLeitwegID = "04011000-12345-03";
    verify(givenLeitwegID);
  }

  private static void verify(String leitwegId) {
    final var checksum = Arrays.stream(leitwegId.split("-")).toList().getLast();
    final var computedChecksum = computeChecksum(leitwegId.split("-")[0] + leitwegId.split("-")[1]);
    if (!checksum.equals(computedChecksum)) throw new IllegalStateException("Checksum is not matching!");
  }

  private static String computeChecksum(String leitwegId) {
    leitwegId = leitwegId.toUpperCase();
    int p = 0;
    for (int i = 0; i < leitwegId.length(); i++) {
      int val = CHARSET.indexOf(leitwegId.charAt(i));
      if (val < 0) throw new IllegalStateException("Illegal character detected at index " + i);
      p = ((p + val) * RADIX) % MODULUS;
    }
    p = (p * RADIX) % MODULUS;
    int checksum = (MODULUS - p + 1) % MODULUS;
    int second = checksum % RADIX;
    int first = (checksum - second) / RADIX;
    return CHARSET.charAt(first) + "" + CHARSET.charAt(second);
  }
}
