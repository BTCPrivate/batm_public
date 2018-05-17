package com.generalbytes.batm.server.extensions.extra.bitcoinprivate;

import com.generalbytes.batm.server.coinutil.AddressFormatException;
import com.generalbytes.batm.server.coinutil.Base58;
import com.generalbytes.batm.server.extensions.ICryptoAddressValidator;

/**
 * Created by Pega88 on 06/05/2018.
 */
public class BitcoinprivateAddressValidator implements ICryptoAddressValidator {
    @Override
    public boolean isAddressValid(String address) {

        //TODO - z addrs
        //TODO - add+test support for BTC style (1,3) and ZCL style (t1, t3) addrs

        if (address.startsWith("b1") || address.startsWith("b3")) {
            try {
                Base58.decodeToBigInteger(address);
                Base58.decodeChecked(address);
            } catch (AddressFormatException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mustBeBase58Address() {
        return true;
    }

    @Override
    public boolean isPaperWalletSupported() {
        return false;
    }
}
