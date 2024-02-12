
## Сущности

### DTO

В репозитории находится несколько классов, отображающих основные сущности
базы данных. БД не использует пользователей и дополнительные данные, которые
(в теории могли бы использоваться).

1. `assertionbit.trainapi.entities.RouteEntity` - Сущность отображающая маршруты из точки А в Б
2. `assertionbit.trainapi.entities.SitEntity` - Место в вагоне, не имеет под собой ничего, кроме собой себя
3. `assertionbit.trainapi.entities.TicketEntity` - Сущность билета, его можно забронировать и вернуть
4. `assertionbit.trainapi.entities.TrainEntity` - Сущность поезда, содержит информацию о вагонах
5. `assertionbit.trainapi.entities.WagonEntity` - Сущность вагона, содержит информацию о местах

### DAO

Репозитории данных. Выступают интерфейсом между БД и API. Не выполняют очистки, так как считалось,
что данную роль будет выполнять БД.

1. `assertionbit.trainapi.repositories.RoutesRepository` - Интерфейс работы с маршрутами
2. `assertionbit.trainapi.repositories.SitsRepository` - Интерфейс работы с местами
3. `assertionbit.trainapi.repositories.TicketRepository` - Интерфейс работы с билетами, группами билетов
4. `assertionbit.trainapi.repositories.TrainRepository` - Интерфейс работы с поездами
5. `assertionbit.trainapi.repositories.WagonRepository` - Интерфейс работы с вагонами

### Services

Тут только один сервис - `ApiService`. Его задача раздача данных и входящая обработка. Сервис не
отвечает за персистентность данных, но валидирует входящие запросы и сверяет с поставленной задачей.

## Использование

Примеры в [examples](./examples) папке

## How to start

`./gradlew bootRun`

