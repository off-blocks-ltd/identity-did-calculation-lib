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

import com.offblocks.did.calc.IdentityType.Discriminator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LedgerTest {


  @Test
  public void testFactomSpecVersions() {
    // Start with a supported spec version
    Assertions.assertEquals(FactomDidV1.INSTANCE,
        Ledger.FACTOM.getSupportedDid(FactomDidV1.FACTOM_DID_SPEC_VERSION));

    // Now for illegal spec versions
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> Ledger.FACTOM.getSupportedDid(null)
    );
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> Ledger.FACTOM.getSupportedDid("0.0")
    );
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> Ledger.FACTOM.getSupportedDid(FactomDidV1.FACTOM_DID_SPEC_VERSION + " ")
    );
  }

  @Test
  public void testLedgerRetrievalForMethodAndDidSpecVersion() {
    Assertions.assertEquals(Ledger.FACTOM, Ledger.fromSupportedDid(FactomDidV1.INSTANCE));

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Ledger.fromSupportedDid(new DidSupport() {
        @Override
        public Ledger ledger() {
          return null;
        }

        @Override
        public String didMethod(String networkName) {
          return "doesnotexist";
        }

        @Override
        public String ledgerDidSpecVersion() {
          return "0.0";
        }

        @Override
        public String calculateDid(String networkName, String emailAddress, String hmacKey,
            IdentityType identityType) {
          return "did:doesnotexist:value";
        }
      });
    });
  }


  @Test
  public void testIdentityTypes () {
    Assertions.assertEquals(Discriminator.DEVICE, IdentityType.DEVICE.discriminator());
    Assertions.assertEquals(Discriminator.MANAGED, IdentityType.MANAGED.discriminator());
  }
}
