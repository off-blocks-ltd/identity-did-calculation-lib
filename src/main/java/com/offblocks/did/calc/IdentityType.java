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

/**
 * Off-Blocks user identities can be one of two types:
 * <ul>
 *      <li>Managed: The DID associated when a user signs from the Webportal using a browser only</li>
 *      <li>Device: The DID assocated when a user signs from a mobile device with their keys stored in
 *          protected storage on the device</li>
 * </ul>
 */
public enum IdentityType {
  /**
   * Off-Blocks Managed/Web identity
   */
  MANAGED(Discriminator.MANAGED),

  /**
   * Device identity, private keyPair in protected storage on the device
   */
  DEVICE(Discriminator.DEVICE);

  private final String discriminator;

  /**
   * Constructor
   *
   * @param discriminator The discriminator value for JPA
   */
  IdentityType(String discriminator) {
    this.discriminator = discriminator;
  }

  /**
   * Get the discriminator as a static string for JPA
   *
   * @return The discriminator value
   */
  public String discriminator() {
    return discriminator;
  }

  /**
   * Here since we use types/discriminators in JPA/persistence which can only be static strings in
   * Java Annotations and we want to use enums in other classes
   */
  public interface Discriminator {

    String MANAGED = "MANAGED";
    String DEVICE = "DEVICE";
  }
}
