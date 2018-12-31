This module contains data model types that are used across Salus Telemetry applications.

# Generating Java source from Avro/Protobuf schemas

Some of the data model types are generated from Avro and Protobuf schemas located in
- [src/main/avro](src/main/avro)
- [src/main/proto](src/main/proto)

The Java source code is generated during a normal Maven build; however, the specific phase
`generate-sources` can be used to only perform the code generation:

```bash
mvn generate-sources
```