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

import com.generalbytes.batm.server.extensions.ICryptoAddressValidator;
import com.generalbytes.batm.server.extensions.ICurrencies;
import com.generalbytes.batm.server.extensions.IExchange;
import com.generalbytes.batm.server.extensions.IExtension;
import com.generalbytes.batm.server.extensions.IPaperWalletGenerator;
import com.generalbytes.batm.server.extensions.IPaymentProcessor;
import com.generalbytes.batm.server.extensions.IRateSource;
import com.generalbytes.batm.server.extensions.IWallet;
import com.generalbytes.batm.server.extensions.extra.digibyte.sources.FixPriceRateSource;
import com.generalbytes.batm.server.extensions.extra.digibyte.sources.livecoin.LiveCoinRateSource;
import com.generalbytes.batm.server.extensions.extra.digibyte.wallets.digibyted.DigiByteRPCWallet;
import com.generalbytes.batm.server.extensions.watchlist.IWatchList;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class DigiByteExtension implements IExtension {

  @Override
  public String getName() {
    return "BATM DigiByte extra extension";
  }

  @Override
  public IExchange createExchange(String exchangeLogin) {
    return null;
  }

  @Override
  public IPaymentProcessor createPaymentProcessor(String paymentProcessorLogin) {
    return null; //no payment processors available
  }

  @Override
  public IWallet createWallet(String walletLogin) {
    if (walletLogin != null && !walletLogin.trim().isEmpty()) {
      StringTokenizer st = new StringTokenizer(walletLogin, ":");
      String walletType = st.nextToken();

      if ("digibyted".equalsIgnoreCase(walletType)) {
        //"digibyted:protocol:user:password:ip:port:accountname"

        String protocol = st.nextToken();
        String username = st.nextToken();
        String password = st.nextToken();
        String hostname = st.nextToken();
        String port = st.nextToken();
        String accountName = "";
        if (st.hasMoreTokens()) {
          accountName = st.nextToken();
        }

        if (protocol != null && username != null && password != null && hostname != null
            && port != null && accountName != null) {
          String rpcURL =
              protocol + "://" + username + ":" + password + "@" + hostname + ":" + port;
          return new DigiByteRPCWallet(rpcURL, accountName);
        }
      }

    }
    return null;
  }

  @Override
  public ICryptoAddressValidator createAddressValidator(String cryptoCurrency) {
    if (ICurrencies.DGB.equalsIgnoreCase(cryptoCurrency)) {
      return new DigiByteAddressValidator();
    }
    return null;
  }

  @Override
  public IPaperWalletGenerator createPaperWalletGenerator(String cryptoCurrency) {
    return null;
  }

  @Override
  public IRateSource createRateSource(String sourceLogin) {
    if (sourceLogin != null && !sourceLogin.trim().isEmpty()) {
      StringTokenizer st = new StringTokenizer(sourceLogin, ":");
      String exchangeType = st.nextToken();
      if ("digibytefix".equalsIgnoreCase(exchangeType)) {
        BigDecimal rate = BigDecimal.ZERO;
        if (st.hasMoreTokens()) {
          try {
            rate = new BigDecimal(st.nextToken());
          } catch (Throwable e) {
          }
        }
        String preferedFiatCurrency = ICurrencies.USD;
        if (st.hasMoreTokens()) {
          preferedFiatCurrency = st.nextToken().toUpperCase();
        }
        return new FixPriceRateSource(rate, preferedFiatCurrency);
      } else if ("livecoin".equalsIgnoreCase(exchangeType)) {
        String preferedFiatCurrency = ICurrencies.USD;
        if (st.hasMoreTokens()) {
          preferedFiatCurrency = st.nextToken().toUpperCase();
        }
        return new LiveCoinRateSource(preferedFiatCurrency);
      }
    }
    return null;
  }

  @Override
  public Set<String> getSupportedCryptoCurrencies() {
    Set<String> result = new HashSet<String>();
    result.add(ICurrencies.DGB);
    return result;
  }

  @Override
  public Set<String> getSupportedWatchListsNames() {
    return null;
  }

  @Override
  public IWatchList getWatchList(String name) {
    return null;
  }
}