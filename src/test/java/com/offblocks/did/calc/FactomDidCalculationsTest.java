/*
 * Off-Blocks Identity chain and DID calculation support library
 *     Copyright (C) 2020 Off-Blocks Ltd.
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */

package com.offblocks.did.calc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FactomDidCalculationsTest {

  public static String TEST_EMAIL = "test@off-blocks.com";
  public static String HMAC_KEY = "fb64c8ee-f696-4aab-a766-3afab7836fd3";

  @Test
  public void testFactomMainnetDidGeneration() {
    Assertions.assertEquals("ab8ea099d6aee1c24e42603183a73ac4e21d29be4b97a406541cfd964001a0ab",
        DidSupport.getHmacHex(TEST_EMAIL, HMAC_KEY));

    DidSupport didSupport = Ledger.FACTOM.getSupportedDid(FactomDidV1.FACTOM_DID_SPEC_VERSION);
    Assertions
        .assertEquals("did:factom:c871268106bdf9c018016153a8b188dc3569f3c82ee16c131ba7f599599800d7",
            didSupport
                .calculateDid(FactomDidV1.MAINNET, TEST_EMAIL, HMAC_KEY, IdentityType.MANAGED));
    Assertions
        .assertEquals("did:factom:26e0721c408ae52988f51cca24ff4979fb561cbaa88ba84d44c9619388e541f6",
            didSupport
                .calculateDid(FactomDidV1.MAINNET, TEST_EMAIL, HMAC_KEY, IdentityType.DEVICE));
  }

  @Test
  public void testFactomTestDidGeneration() {
    DidSupport didSupport = Ledger.FACTOM.getSupportedDid(FactomDidV1.FACTOM_DID_SPEC_VERSION);
    Assertions
        .assertEquals("did:factom:testnet:c871268106bdf9c018016153a8b188dc3569f3c82ee16c131ba7f599599800d7",
            didSupport
                .calculateDid(FactomDidV1.TESTNET, TEST_EMAIL, HMAC_KEY, IdentityType.MANAGED));
    Assertions
        .assertEquals("did:factom:testnet:26e0721c408ae52988f51cca24ff4979fb561cbaa88ba84d44c9619388e541f6",
            didSupport
                .calculateDid(FactomDidV1.TESTNET, TEST_EMAIL, HMAC_KEY, IdentityType.DEVICE));
  }

}
