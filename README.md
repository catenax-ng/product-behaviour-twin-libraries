# Behaviour Twin Libraries

## Branch debug-edc(-???)

This branch provides a minimal example for streaming over EDC (SEDC).

Unlike in the branch all-in-one, the SEDC components here are only used for unidirectional streaming.
The use case is stripped down to only include the streaming aspect without databases, data models, checks, ...  
This is done to reduce complexity during debugging.

The implemented use case is:

    (1) OEM collector opens streaming channel to peakload service (requests asset)
    (2) peakload service streams infinitely results
    (3) received results are logged

There are two variants that can be configured:

    (a) direct connection (debugging both services without EDCs)
    (b) connection via EDCs where the dataplane of the peakload service is involved
