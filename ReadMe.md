
## How to start

1. Start postgres

```shell
docker-compose up -d
```

2. Execute query in postgres console
   1. Execute `src/main/resources/schema-postgresql.sql`
   2. Execute `src/main/resources/data-postgresql.sql`

3. Run JooQ - `./gradlew jooqCodegen`
4. Run application - `./gradlew bootRun`