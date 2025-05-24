# ADR 0005: Use of Eureka Server for Service Discovery

## Status
Accepted

## Context
We need a centralized service discovery mechanism to support:
- Dynamic scaling of microservices
- Automatic service registration/deregistration
- Client-side load balancing
- Resilience against network location changes

## Decision
We will use Netflix Eureka Server as our service discovery solution because:
1. Tight integration with Spring Cloud ecosystem
2. Mature solution with extensive community support
3. Simple setup process
4. Compatible with our existing Spring Boot infrastructure

## Consequences

### Positive
- Centralized service registry reduces configuration complexity
- Enables horizontal scaling of services
- Automatic health monitoring via heartbeats
- Simplifies inter-service communication

### Negative
- Adds new infrastructure component to maintain
- Requires careful cluster configuration for production
- Netflix no longer actively developing Eureka

### Interaction with Config Server
1. **Dependency**: Discovery Server requires Config Server for initial configuration
2. **Boot Sequence**:
   - Config Server must be running first
   - Discovery Server fetches its config during bootstrap phase
3. **Separation of Concerns**:
   - Config Server: Manages configurations
   - Discovery Server: Manages service registry
4. **Network Flow**:
   - Initial configuration fetch (Discovery → Config)
   - Subsequent operation: Independent once configured
