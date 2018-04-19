/*************************************************************************************
 * Copyright (C) 2014-2016 GENERAL BYTES s.r.o. All rights reserved.
 *
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of
 * the GPL2 ("Copyleft").
 *
 * Contact information
 * -------------------
 *
 * GENERAL BYTES s.r.o.
 * Web      :  http://www.generalbytes.com
 *
 ************************************************************************************/
package com.generalbytes.batm.server.extensions.extra.digibyte;

import com.generalbytes.batm.server.coinutil.AddressFormatException;
import com.generalbytes.batm.server.coinutil.Base58;
import com.generalbytes.batm.server.extensions.ICryptoAddressValidator;

public class DigiByteAddressValidator implements ICryptoAddressValidator {

  @Override
  public boolean isAddressValid(String address) {
    char[] addressChars = address.toCharArray();
    //Check for invalid characters; ensure alphanumeric and no fobidden characters exist
    for (char addressCharacter : addressChars) {
      if (!(((addressCharacter >= '0' && addressCharacter <= '9') ||
          (addressCharacter >= 'a' && addressCharacter <= 'z') ||
          (addressCharacter >= 'A' && addressCharacter <= 'Z')) &&
          addressCharacter != 'l' && addressCharacter != 'I' &&
          addressCharacter != '0' && addressCharacter != 'O')) {
        return false;
      }
    }
    try {
      Base58.decodeToBigInteger(address);
      Base58.decodeChecked(address);
      return true;
    } catch (AddressFormatException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean isPaperWalletSupported() {
    return false;
  }

  @Override
  public boolean mustBeBase58Address() {
    return true;
  }
}