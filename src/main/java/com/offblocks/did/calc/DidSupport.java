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


import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

import org.apache.commons.codec.digest.HmacUtils;

public interface DidSupport {

  /**
   * Const for typical public mainnet ledger network
   */
  String MAINNET = "mainnet";

  /**
   * Implementations must return which ledger/blockchain they support
   *
   * @return their supported ledger
   */
  Ledger ledger();

  /**
   * Generated the DID method specific part for the Ledger, version and blockchain network name
   *
   * @param networkName The blockchain network name
   * @return The DID method
   */
  String didMethod(String networkName);

  /**
   * The Ledger specific DID specification version. Typically DID methods on different ledeger have
   * their own versioning. This is here so we can support multiple versions at the same time
   *
   * @return The ledger DID spec version
   */
  String ledgerDidSpecVersion();

  /**
   * Calculates the DID, based on the network name and the method Specific ID of the DID
   *
   * @param networkName      The ledger network name
   * @param methodSpecificId The ledger specific method ID to identity the DID
   * @return The DID
   */
  default String calculateDid(String networkName, String methodSpecificId) {
    return String.format("did:%s:%s", didMethod(networkName), methodSpecificId);
  }

  /**
   * Calculates the full Off-Blocks DID, based on the network name, e-mail address of the Off-Blocks
   * user, their HMAC (SHA256) keyPair/secret and the Identity Type.
   *
   * <p>
   * The HMAC keyPair/secret that the Off-Blocks user has to disclose to prove their DID, without
   * having to do a challenge/response proving private-keyPair possession. The Off-Blocks user can
   * disclose this value in case of (legal) conflict/disputes. It will be used as the keyPair in an
   * HMAC calculation (SHA256) using the e-mail address
   * </p>
   *
   * <p>
   * Off-Blocks has 2 identity types on chain and thus at least 2 DIDs are associated with a user:
   * </p>
   * <ul>
   *     <li>Managed: The DID associated when a user signs from the Webportal using a browser only</li>
   *     <li>Device: The DID assocated when a user signs from a mobile device with their keys stored in
   *         protected storage on the device</li>
   * </ul>
   * The identity types are included in the calculation typically
   *
   * @param networkName  The Ledger network name, typically mainnet for the public network
   * @param emailAddress The e-mail address of the Off-Blocks user
   * @param hmacKey      The HMAC keyPair/secret that the Off-Blocks user has to disclose to prove
   *                     their DID, without having to do a challenge/response proving
   *                     private-keyPair access. The Off-Blocks user can disclose this value in case
   *                     of (legal) conflict/disputes
   * @return The calculated DID belonging to the network, user e-mail address and identity type
   */
  String calculateDid(String networkName, String emailAddress, String hmacKey,
      IdentityType identityType);


  /**
   * Sanatizes a supplied network name. Remove spaces, transforms it to lowercase and returns
   * mainnet in case of an empty network name
   *
   * @param suppliedNetworkName The user supplied network name
   * @return Sanatized network name
   */
  default String networkName(String suppliedNetworkName) {
    if (suppliedNetworkName == null || "".equals(suppliedNetworkName.trim()) ||
        MAINNET.equalsIgnoreCase(suppliedNetworkName.trim()) || "main"
        .equalsIgnoreCase(suppliedNetworkName.trim())) {
      return MAINNET;
    }
    return suppliedNetworkName.toLowerCase().trim();
  }


  static String getHmacHex(String emailAddress, String hmacKey) {
    if (isEmpty(emailAddress) || isEmpty(hmacKey)) {
      throw new IllegalArgumentException(
          "We need an e-mail address and Hmac Key to construct the SHA_256 Hmac");
    }
    return new HmacUtils(HMAC_SHA_256, hmacKey)
        .hmacHex(emailAddress.toLowerCase().trim());
  }

  static boolean isEmpty(String input) {
    return input == null || "".equals(input.trim());
  }

}
