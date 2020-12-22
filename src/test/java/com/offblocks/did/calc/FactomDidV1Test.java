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

import static com.offblocks.did.calc.FactomDidV1.ExternalIds.IDENTITY_CHAIN;
import static com.offblocks.did.calc.FactomDidV1.ExternalIds.MANAGED;
import static com.offblocks.did.calc.FactomDidV1.ExternalIds.OFFBLOCKS;
import static com.offblocks.did.calc.FactomDidV1.ExternalIds.V1;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

import com.offblocks.did.calc.FactomDidV1.Entry;
import com.sphereon.libs.blockchain.commons.Digest;
import com.sphereon.libs.blockchain.commons.Digest.Algorithm;
import com.sphereon.libs.blockchain.commons.Digest.Encoding;
import java.util.List;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FactomDidV1Test {

  public static final String FACTOM = "factom";
  public static String TEST_EMAIL = "test@off-blocks.com";
  public static String HMAC_KEY = "fb64c8ee-f696-4aab-a766-3afab7836fd3";
  public static final String HEX_INPUT = Digest.getInstance()
      .getHashAsString(Algorithm.SHA_256, HMAC_KEY, Encoding.HEX);

  @Test
  public void testDidMethods() {
    // Valid Mainnet network names
    Assertions.assertEquals(FACTOM, FactomDidV1.INSTANCE.didMethod(FactomDidV1.MAINNET));
    Assertions.assertEquals(FACTOM, FactomDidV1.INSTANCE.didMethod(""));
    Assertions.assertEquals(FACTOM, FactomDidV1.INSTANCE.didMethod(null));
    Assertions.assertEquals(FACTOM, FactomDidV1.INSTANCE.didMethod("Main"));
    Assertions.assertEquals(FACTOM, FactomDidV1.INSTANCE.didMethod("MainNet"));
    Assertions.assertEquals(FACTOM, FactomDidV1.INSTANCE.didMethod("MAINNET"));

    // Valid testnet or custom networks
    Assertions.assertEquals("factom:testnet", FactomDidV1.INSTANCE.didMethod(FactomDidV1.TESTNET));
    Assertions.assertEquals("factom:custom", FactomDidV1.INSTANCE.didMethod("custom"));
    Assertions.assertEquals("factom:custom", FactomDidV1.INSTANCE.didMethod("Custom"));
    Assertions.assertEquals("factom:custom", FactomDidV1.INSTANCE.didMethod(" Custom "));
    Assertions.assertEquals("factom:alsovalid", FactomDidV1.INSTANCE.didMethod("Alsovalid "));
  }


  @Test
  public void testFactomExternalIdValues() {
    Assertions.assertLinesMatch(
        List.of(IDENTITY_CHAIN,
            OFFBLOCKS,
            V1,
            HEX_INPUT),
        FactomDidV1.ExternalIds.constructIdentity(HEX_INPUT, IdentityType.DEVICE));

    Assertions.assertLinesMatch(
        List.of(IDENTITY_CHAIN,
            OFFBLOCKS,
            V1,
            HEX_INPUT,
            MANAGED),
        FactomDidV1.ExternalIds.constructIdentity(HEX_INPUT, IdentityType.MANAGED));
  }


  @Test
  public void testHmacHexGeneration() {
    Assertions.assertEquals(new HmacUtils(HMAC_SHA_256, HMAC_KEY).hmacHex(TEST_EMAIL),
        DidSupport.getHmacHex(TEST_EMAIL, HMAC_KEY));

    // Test generic HmacHex Generation with invalid values
    Assertions
        .assertThrows(IllegalArgumentException.class, () -> DidSupport.getHmacHex(null, HMAC_KEY));
    Assertions
        .assertThrows(IllegalArgumentException.class,
            () -> DidSupport.getHmacHex(TEST_EMAIL, null));
  }

  @Test
  public void testFactomDidV1SupportingMethods() {
    Assertions.assertEquals(FactomDidV1.FACTOM_DID_SPEC_VERSION,
        FactomDidV1.INSTANCE.ledgerDidSpecVersion());
    Assertions.assertEquals(Ledger.FACTOM, FactomDidV1.INSTANCE.ledger());

    // FactomDidV1 is a signleton with fixed hashcode
    Assertions.assertEquals(-2102665653, FactomDidV1.INSTANCE.hashCode());

    // Construct an identity without hmac Hex or identitytype should return an error
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> FactomDidV1.ExternalIds.constructIdentity(null, IdentityType.DEVICE));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> FactomDidV1.ExternalIds.constructIdentity(HEX_INPUT, null));

    // duh, it is a constant. Here for test coverage and as regression protection
    Assertions.assertEquals(1L, Entry.IDENTITY_VERSION);

  }
}
