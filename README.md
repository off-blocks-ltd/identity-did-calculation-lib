# Off-Blocks Identity DID Calculation and proof

![Logo](https://www.off-blocks.com/assets/images/logos/logo-text-primary.svg)

![Java CI with Maven](https://github.com/off-blocks-ltd/identity-did-calculation-lib/workflows/Java%20CI%20with%20Maven/badge.svg)

## About

This library allows external parties to check how Off-Blocks generates Factom DID V1 chains using a
secret HMAC key.

Whenever there would be a dispute about whether an Off-Blocks user has created a signature on chain
this library could be used to determine whether an identity chain belongs to an Off-Blocks account
or not.

Typically, you could just present a challenge digest to be signed by the Off-Blocks user. If the
signature can be validated using a Public Key in the disputed Identity chain, you know there is not
really a basis for the dispute as the user has access to the accompanying private key which is in
secure storage on a device. However, if more proof is wanted, this library discloses how Off-Blocks
generated Factom chains and uses them from an Identity that is tied to the e-mail address of the
specific user.

All that is needed is the e-mail address of the Off-Blocks user as well as the Off-Blocks user
disclosing his/her HMAC key value for verification. This value is normally not disclosed, but the
user can choose to disclose the value in case of a dispute.

## How does it work technically?

The HMAC key is used as a secret input key for a standards
based [HMAC hash calculation](https://en.wikipedia.org/wiki/HMAC). The e-mail address (to lowercase)
is then used as the message to be authenticated using the SHA-256 algorithm. The result is encoded
as HEX value and used in the Identity Chain generation on the Factom Blockchain network. Given the
nature of an HMAC you cannot derive the e-mail address, nor the secret input key from the resulting
hash value. You will need both the secret key, and the e-mail address to perform the one-way hash
function. The secret is only known to the Off-Blocks user, and he/she is the one that could
prove the Identity Chain and thus the corresponding Public Keys and DIDs belonging to him/her without
a doubt.

This library is used internally by Off-Blocks. Others should not have to use it. We provide it in
case people really want to verify a DID by hand in case of disputes. The class to be used is
_FactomDidV1.java_ and the method
is `calculateDid(String networkName, String emailAddress, String hmacKey, IdentityType identityType)`

## Questions?

If you have any questions, do not hesitate to contact us.
