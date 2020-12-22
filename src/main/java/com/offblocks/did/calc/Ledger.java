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

import java.util.Arrays;
import java.util.List;

/**
 * Supported blockchains and ledgers with links to supported ledger specific DID implementations and
 * versions
 */
public enum Ledger {
    FACTOM(FactomDidV1.INSTANCE);

    private final List<DidSupport> didSupports;

    Ledger(DidSupport... didSupports) {
        this.didSupports = Arrays.asList(didSupports);
    }

    /**
     * Supported DID methods and versions on the specific ledger
     *
     * @return Supported DIDs
     */
    public List<DidSupport> getSupportedDids() {
        return didSupports;
    }

    /**
     * Get the supported DID implementation by spec version for this network. Please note this can
     * be problematic if the network supports multiple different DID methods having overlapping
     * versions
     *
     * @param specVersion The Spec version
     * @return DID support
     */
    public DidSupport getSupportedDid(String specVersion) {
        return getSupportedDids().stream()
            .filter(didSupport -> didSupport.ledgerDidSpecVersion().equalsIgnoreCase(specVersion))
            .findFirst().orElseThrow(() -> new IllegalArgumentException(
                String.format("Version '%s' not supported on ledger %s", specVersion, name())));
    }

    /**
     * Get the ledger based on the DID
     *
     * @param didSupport DID Support
     * @return Ledger
     */
    public static Ledger fromSupportedDid(DidSupport didSupport) {
        for (Ledger ledger : Ledger.values()) {
            if (ledger.getSupportedDids().contains(didSupport)) {
                return ledger;
            }
        }
        throw new IllegalArgumentException(
            String.format("DID method %s not supported by any known ledger implementation",
                didSupport));
    }
}
