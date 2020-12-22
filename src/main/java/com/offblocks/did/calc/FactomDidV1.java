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

import static com.offblocks.did.calc.DidSupport.getHmacHex;
import static com.offblocks.did.calc.DidSupport.isEmpty;
import static java.util.Arrays.asList;

import com.sphereon.libs.blockchain.commons.Operations;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FactomDidV1 implements DidSupport {


  public static final String FACTOM_DID_SPEC_VERSION = "1.0";
  public static final String FACTOM = "factom";
  protected static final String TESTNET = "testnet";

  public static final FactomDidV1 INSTANCE = new FactomDidV1();

  private FactomDidV1() {
  }

  /**
   * This class represents the External Ids as known on the Factom Blockchain
   */
  public static class ExternalIds {

    public static final String OFFBLOCKS = "Off-Blocks";
    public static final String V1 = "v1";
    public static final String IDENTITY_CHAIN = "IdentityChain";
    public static final String MANAGED = "web";

    /**
     * Construct a list of external Ids based on the HMAC hex value and the identity type
     *
     * @param hmacHex This is the HMAC hex from the e-mail address and HMAC secret key
     * @param type    The identity type
     * @return The list of external Ids
     */
    public static List<String> constructIdentity(final String hmacHex, IdentityType type) {
      if (isEmpty(hmacHex)) {
        throw new IllegalArgumentException(
            "An HMAC is required to create an identity chain");
      } else if (type == null) {
        throw new IllegalArgumentException(
            "An Identity Type is required to create an identity chain");
      }
      List<String> externalIds = new ArrayList<>(asList(
          IDENTITY_CHAIN,
          OFFBLOCKS,
          V1,
          hmacHex));
      if (type == IdentityType.MANAGED) {
        externalIds.add(MANAGED);
      }

      return externalIds;
    }

  }

  /**
   * The Entry (body) value support for Identity chains on Factom
   */
  public static class Entry {


    public static final long IDENTITY_VERSION = 1;

  }

  /**
   * Returns the DID method part of a DID based on the network name
   *
   * @param networkName The blockchain network name
   * @return The DID method
   */
  public String didMethod(String networkName) {
    switch (networkName(networkName)) {
      case MAINNET:
        return FACTOM;
      case TESTNET:
      case FACTOM + ":" + TESTNET:
        return String.format("%s:%s", FACTOM, TESTNET);
      default:
        return String.format("%s:%s", FACTOM, networkName(networkName));
    }
  }

  @Override
  public String ledgerDidSpecVersion() {
    return FACTOM_DID_SPEC_VERSION;
  }

  @Override
  public String calculateDid(String networkName, String emailAddress, String hmacKey,
      IdentityType identityType) {

    List<String> identityExtIds = ExternalIds
        .constructIdentity(getHmacHex(emailAddress, hmacKey), identityType);
    String chainId = Operations.getInstance()
        .generateChainIdFromValues(identityExtIds.stream().map(s -> s.getBytes(
            StandardCharsets.UTF_8)).collect(Collectors.toList()));
    return calculateDid(networkName, chainId);
  }


  @Override
  public Ledger ledger() {
    return Ledger.FACTOM;
  }

  @Override
  public boolean equals(Object o) {
    return this == o;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass().getName());
  }
}
