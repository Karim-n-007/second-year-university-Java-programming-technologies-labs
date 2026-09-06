# Автосалон — информационная система

Back-end информационной системы мультибрендового автосалона: каталог автомобилей с фильтрами,
конфигуратор комплектаций с проверкой совместимости узлов, два типа заказов со своими жизненными
циклами, запись на тест-драйв и разграничение сценариев по ролям пользователей.

`Java 23` · `Gradle 8.12` · `JUnit 5` · `Mockito` · `JaCoCo` · `Docker`

---

## Содержание

- [О проекте](#о-проекте)
- [Пользователи и сценарии](#пользователи-и-сценарии)
- [Архитектура](#архитектура)
- [Структура проекта](#структура-проекта)
- [Доменная модель](#доменная-модель)
- [Конфигуратор автомобиля](#конфигуратор-автомобиля)
- [Жизненный цикл заказов](#жизненный-цикл-заказов)
- [Тест-драйв](#тест-драйв)
- [Хранилище данных](#хранилище-данных)
- [Фильтрация каталога](#фильтрация-каталога)
- [Обработка ошибок](#обработка-ошибок)
- [Тестирование](#тестирование)
- [Запуск](#запуск)
- [Демонстрационные данные](#демонстрационные-данные)

---

## О проекте

Система моделирует работу частного мультибрендового автосалона. В ней есть каталог моделей
автомобилей, склад запчастей (узлов), клиенты и сотрудники с ролями, заказы и заявки на тест-драйв.

Ключевая часть системы — **конфигуратор**: клиент собирает автомобиль из вариантов узлов,
система проверяет совместимость каждого варианта с конкретной моделью и считает итоговую стоимость
как «базовая цена модели + сумма доплат за выбранные узлы».

Основные свойства реализации:

| Свойство | Решение |
|---|---|
| Архитектура | Трёхслойная: представление → бизнес-логика → домен, инфраструктура подключается через интерфейсы |
| Хранилище | In-memory репозитории поверх `HashMap` / `HashSet`, без БД |
| Внедрение зависимостей | Ручная сборка графа объектов в композиционном корне `ApplicationContext` |
| Деньги | `BigDecimal` со шкалой 2; поддерживаются отрицательные доплаты (например, −30 000 ₽ за механическую КПП) |
| Валидация | Предметные исключения: `DomainValidationException`, `IncompatibleComponentException`, `EntityNotFoundException` |
| Переходы статусов | Таблицы допустимых переходов внутри enum-ов, недопустимый переход отклоняется доменом |
| Интерфейс | Консольное меню, разное для каждой роли |
| Сборка | Gradle, запуск через `application`-плагин и Docker-образ |
| Тесты | JUnit 5 + Mockito, покрытие контролируется JaCoCo (порог 70%) |

---

## Пользователи и сценарии

```mermaid
flowchart LR
    client(["Клиент"])
    manager(["Менеджер автосалона"])
    warehouse(["Администратор склада"])
    sysadmin(["Администратор системы"])

    subgraph system["Система автосалона"]
        uc1["Просмотр каталога<br/>и фильтрация"]
        uc2["Карточка автомобиля"]
        uc3["Заказ автомобиля<br/>в наличии"]
        uc4["Заказ автомобиля<br/>с комплектацией"]
        uc5["Запись на тест-драйв"]
        uc6["Просмотр заказов"]
        uc7["Смена статуса заказа"]
        uc8["Управление списком<br/>авто для тест-драйва"]
        uc9["Просмотр заявок<br/>на тест-драйв"]
        uc10["Ведение автомобилей<br/>и запчастей"]
        uc11["Совместимость<br/>узлов и моделей"]
        uc12["CRUD любой сущности"]
    end

    client --> uc1
    client --> uc2
    client --> uc3
    client --> uc4
    client --> uc5
    client --> uc6

    manager --> uc1
    manager --> uc2
    manager --> uc6
    manager --> uc7
    manager --> uc8
    manager --> uc9

    warehouse --> uc2
    warehouse --> uc10
    warehouse --> uc11

    sysadmin --> uc12
```

Меню консоли соответствует ролям один в один:

| Роль | Класс меню | Что доступно |
|---|---|---|
| Клиент | `ClientMenu` | Каталог с фильтрами, карточка авто, оба типа заказов, запись на тест-драйв, свои заказы и заявки |
| Менеджер автосалона | `ManagerMenu` | Каталог, заказы обоих типов с фильтрами, смена статусов, список авто для тест-драйва, заявки |
| Администратор склада | `WarehouseAdministratorMenu` | Добавление и изменение автомобилей и запчастей, цены, совместимость узлов с моделями |
| Администратор системы | `SystemAdministratorMenu` | Создание, изменение и удаление клиентов, сотрудников, автомобилей, заказов и заявок |

---

## Архитектура

Зависимости направлены строго внутрь: представление знает о бизнес-слое, бизнес-слой — о домене
и об **интерфейсах** репозиториев, инфраструктура реализует эти интерфейсы. Домен не зависит ни от чего.

```mermaid
flowchart TB
    subgraph presentation["Слой представления · ru.nursafin.presentation"]
        app["CarDealershipApplication<br/>точка входа"]
        ctx["ApplicationContext<br/>композиционный корень"]
        demo["DemoDataInitializer<br/>демо-данные"]
        menus["Меню ролей<br/>ClientMenu · ManagerMenu<br/>WarehouseAdministratorMenu · SystemAdministratorMenu"]
        io["ConsoleIo · ConsoleFormatter<br/>ввод-вывод и форматирование"]
    end

    subgraph application["Бизнес-слой · ru.nursafin.application"]
        services["Сервисы<br/>CarModelService · заказы · запчасти<br/>TestDriveService · пользователи"]
        assignment["EmployeeAssignment<br/>автоназначение менеджера"]
        filters["Фильтры<br/>CarModelFilter · OrderFilter · TestDriveFilter"]
        repoApi["Интерфейсы репозиториев"]
    end

    subgraph domain["Доменный слой · ru.nursafin.domainModel"]
        entities["Сущности<br/>CarModel · SparePart · заказы<br/>TestDriveRequest · Client · Employee"]
        vo["Value objects<br/>Money · Power · EngineDisplacement"]
        statuses["Статусы и правила переходов"]
        exceptions["Предметные исключения"]
    end

    subgraph infrastructure["Инфраструктура · ru.nursafin.infrastructure"]
        repoImpl["In-memory репозитории"]
        storage["Хранилища<br/>HashMap · HashSet"]
    end

    app --> ctx
    ctx --> demo
    ctx --> menus
    menus --> io
    menus --> services
    menus --> filters
    services --> assignment
    services --> repoApi
    services --> entities
    filters --> entities
    entities --> vo
    entities --> statuses
    entities --> exceptions
    repoImpl -. реализует .-> repoApi
    repoImpl --> storage
    ctx -. связывает .-> repoImpl
```

**Почему так:** бизнес-слой обращается к хранилищу только через интерфейсы
(`CarModelRepository`, `OrderReadyCarRepository`, `TestDriveRequestRepository` и др.), поэтому
in-memory реализацию можно заменить на БД, не трогая сервисы, а в unit-тестах репозитории
подменяются моками.

---

## Структура проекта

```
src/main/java/ru/nursafin/
├── domainModel/                  # доменный слой: сущности и правила
│   ├── entities/
│   │   ├── car/                  # CarModel + CarModelBuilder
│   │   ├── order/                # OrderReadyCarModel, OrderCustomCarModel
│   │   ├── sparePart/            # SparePart и 6 типов узлов с билдерами
│   │   ├── testDrive/            # TestDriveRequest + статусы заявки
│   │   └── valueObjects/         # Money, Power, EngineDisplacement
│   ├── statuses/                 # ReadyCarOrderStatus, CustomCarOrderStatus
│   ├── users/                    # Client, Employee, EmployeeRole
│   └── exceptions/               # предметные исключения
│
├── application/                  # бизнес-слой
│   ├── services/                 # сценарии использования
│   ├── repositories/             # интерфейсы доступа к данным
│   └── filters/                  # объекты-фильтры для выборок
│
├── infrastructure/               # реализация хранилища
│   ├── repositories/             # in-memory репозитории
│   └── db/dataBase/…             # структуры хранения
│
└── presentation/                 # слой представления
    ├── ApplicationContext        # сборка зависимостей
    ├── DemoDataInitializer       # наполнение демо-данными
    ├── CarDealershipApplication  # main
    └── console/                  # меню, ввод-вывод, форматирование
```

---

## Доменная модель

```mermaid
classDiagram
    class CarModel {
        UUID id
        String name
        String brand
        String color
        Money basePrice
        Drive drive
    }

    class SparePart {
        <<interface>>
        UUID getId()
        Money getPrice()
        Set~UUID~ getCompatibleCars()
        void addCompatibleCar(UUID)
    }

    class Body {
        BodyType bodyType
    }
    class Engine {
        Power power
        EngineDisplacement displacement
        FuelType fuelType
    }
    class Gearbox {
        GearboxType type
    }
    class SteeringWheel {
        SteeringWheelType type
        SteeringWheelMaterial material
    }
    class Interior {
        InteriorType interiorType
        String color
    }
    class Wheels {
        WheelSeason season
    }

    class OrderReadyCarModel {
        UUID id
        UUID clientId
        UUID employeeId
        Money priceAtCreateOrderMoment
        ReadyCarOrderStatus status
        changeStatus(status)
    }

    class OrderCustomCarModel {
        UUID id
        UUID clientId
        UUID employeeId
        Money priceAtCreateOrderMoment
        CustomCarOrderStatus status
        changeStatus(status)
    }

    class TestDriveRequest {
        UUID id
        UUID clientId
        UUID carModelId
        LocalDateTime startDateTime
        TestDriveRequestStatus status
        changeStatus(status)
    }

    class Client {
        UUID id
        String name
        LocalDate birthday
        String number
        String email
    }

    class Employee {
        UUID id
        String name
        EmployeeRole role
        hasRole(role)
    }

    class Money {
        BigDecimal value
        plus(other)
        minus(other)
        isNegative()
    }

    SparePart <|.. Body
    SparePart <|.. Engine
    SparePart <|.. Gearbox
    SparePart <|.. SteeringWheel
    SparePart <|.. Interior
    SparePart <|.. Wheels

    CarModel *-- Body : базовый узел
    CarModel *-- Engine : базовый узел
    CarModel *-- Gearbox : базовый узел
    CarModel *-- SteeringWheel : базовый узел
    CarModel *-- Interior : базовый узел
    CarModel *-- Wheels : базовый узел

    OrderReadyCarModel --> CarModel : заказанный автомобиль
    OrderCustomCarModel --> CarModel : модель
    OrderCustomCarModel --> SparePart : выбранные узлы

    OrderReadyCarModel ..> Client : clientId
    OrderReadyCarModel ..> Employee : employeeId
    OrderCustomCarModel ..> Client : clientId
    OrderCustomCarModel ..> Employee : employeeId
    TestDriveRequest ..> Client : clientId
    TestDriveRequest ..> CarModel : carModelId

    CarModel --> Money : basePrice
    SparePart --> Money : доплата
```

### Сущности

| Сущность | Назначение |
|---|---|
| `CarModel` | Модель автомобиля: бренд, модель, цвет, базовая стоимость, привод и шесть базовых узлов |
| `SparePart` | Общий контракт узла: цена-доплата и множество совместимых моделей |
| `OrderReadyCarModel` | Заказ автомобиля в наличии: клиент, менеджер, автомобиль, цена на момент заказа, статус |
| `OrderCustomCarModel` | Заказ с комплектацией: то же плюс шесть выбранных узлов |
| `TestDriveRequest` | Заявка на тест-драйв: клиент, автомобиль, дата и время, статус |
| `Client` | Клиент салона |
| `Employee` | Сотрудник с ролью `SALES_MANAGER`, `WAREHOUSE_ADMINISTRATOR` или `SYSTEM_ADMINISTRATOR` |
| `Money` | Денежная величина на `BigDecimal`, допускает отрицательные доплаты |

---

## Конфигуратор автомобиля

Комплектация — это набор из шести узлов. У каждой модели есть базовый вариант каждого узла с доплатой
0 ₽, а альтернативные варианты имеют собственную доплату, которая может быть и отрицательной.
Совместимость задаётся бизнес-правилами салона: каждый вариант узла хранит список моделей,
на которые его разрешено ставить.

**Итоговая стоимость = базовая стоимость модели + сумма доплат выбранных узлов.**

```mermaid
sequenceDiagram
    actor Client as Клиент
    participant Menu as ClientMenu
    participant Service as CustomCarModelOrderService
    participant Repos as Репозитории
    participant Assign as EmployeeAssignment
    participant Order as OrderCustomCarModel

    Client->>Menu: выбрать модель и шесть узлов
    Menu->>Service: createOrder(clientId, carModelId, узлы…)

    Service->>Service: проверить, что все узлы выбраны
    Note over Service: пропущен узел →<br/>DomainValidationException

    Service->>Repos: findById для клиента, модели и узлов
    Note over Repos: неизвестный id →<br/>EntityNotFoundException

    loop по каждому узлу
        Service->>Service: узел совместим с моделью?
        Note over Service: нет →<br/>IncompatibleComponentException
    end

    Service->>Service: базовая цена + сумма доплат
    Service->>Assign: assignSalesManager()
    Assign-->>Service: случайный менеджер продаж
    Service->>Order: создать заказ в статусе PLACED
    Service->>Repos: save(order)
    Service-->>Menu: идентификатор заказа
    Menu-->>Client: заказ оформлен и его состав
```

### Пример: BMW 320i

Базовая комплектация — доплата 0 ₽ за каждый узел, базовая цена модели 3 000 000 ₽.

| Узел | Вариант | Доплата | Совместимость |
|---|---|---|---|
| Колёса | 17'' Standard | 0 ₽ | 320i, 330i, A4 Avant |
| Колёса | 18'' Aero | +45 000 ₽ | 320i, 330i |
| Колёса | 19'' M-Sport | +95 000 ₽ | 320i, 330i |
| Трансмиссия | Automatic 8AT | 0 ₽ | 320i, 330i, A4 Avant |
| Трансмиссия | Manual 6MT | −30 000 ₽ | 320i, 330i |
| Руль | Sport leather (Standard) | 0 ₽ | 320i, 330i, A4 Avant |
| Руль | M-Sport heated | +25 000 ₽ | 320i, 330i |
| Интерьер | Fabric Graphite | 0 ₽ | 320i, 330i, A4 Avant |
| Интерьер | Leather Dakota | +110 000 ₽ | 320i, 330i |
| Интерьер | Sport Performance | +160 000 ₽ | только 330i |

**Корректная конфигурация:** 19'' M-Sport + Automatic 8AT + M-Sport heated + Leather Dakota

```
3 000 000 + 95 000 + 25 000 + 110 000 = 3 230 000 ₽
```

**Отрицательная доплата:** базовая комплектация с Manual 6MT

```
3 000 000 − 30 000 = 2 970 000 ₽
```

**Ошибка совместимости:** интерьер Sport Performance для 320i

```
IncompatibleComponentException: Spare part <id> is not compatible with this car model: <id>
```

**Пропущенный узел:** не выбран интерьер

```
DomainValidationException: missing required component "Interior"
```

---

## Жизненный цикл заказов

Статусы вынесены в enum-ы с таблицей допустимых переходов, а сама смена статуса выполняется
методом `changeStatus` доменной сущности — недопустимый переход отклоняется независимо от того,
кто его инициировал.

### Заказ автомобиля в наличии

```mermaid
stateDiagram-v2
    [*] --> PLACED : оформлен
    PLACED --> APPROVED_BY_MANAGER : согласован менеджером
    APPROVED_BY_MANAGER --> AWAITING_PAYMENT : ожидает оплаты
    AWAITING_PAYMENT --> PAID : оплачен
    PAID --> READY_FOR_PICKUP : готов к выдаче
    READY_FOR_PICKUP --> COMPLETED : завершён
    COMPLETED --> [*]

    PLACED --> CANCELLED : отменён
    APPROVED_BY_MANAGER --> CANCELLED : отменён
    AWAITING_PAYMENT --> CANCELLED : отменён
    CANCELLED --> [*]
```

### Заказ автомобиля с комплектацией

```mermaid
stateDiagram-v2
    [*] --> PLACED : оформлен
    PLACED --> APPROVED_BY_WAREHOUSE : согласован складом
    APPROVED_BY_WAREHOUSE --> AWAITING_PAYMENT : ожидает оплаты
    AWAITING_PAYMENT --> PAID : оплачен
    PAID --> AWAITING_DELIVERY : ожидает доставки
    AWAITING_DELIVERY --> READY_FOR_PICKUP : готов к выдаче
    READY_FOR_PICKUP --> COMPLETED : завершён
    COMPLETED --> [*]

    PLACED --> CANCELLED : отменён
    APPROVED_BY_WAREHOUSE --> CANCELLED : отменён
    AWAITING_PAYMENT --> CANCELLED : отменён
    CANCELLED --> [*]
```

Отмена возможна только до оплаты; `COMPLETED` и `CANCELLED` — финальные состояния.
Менеджер на заказ назначается автоматически: `EmployeeAssignment` выбирает случайного сотрудника
с ролью `SALES_MANAGER`, и если таких сотрудников нет — заказ не создаётся.

---

## Тест-драйв

Менеджер ведёт список автомобилей, доступных для тест-драйва; клиент записывается только на
автомобиль из этого списка.

```mermaid
stateDiagram-v2
    [*] --> REQUESTED : заявка создана
    REQUESTED --> CONFIRMED : подтверждена менеджером
    CONFIRMED --> COMPLETED : тест-драйв проведён
    REQUESTED --> CANCELLED : отменена
    CONFIRMED --> CANCELLED : отменена
    COMPLETED --> [*]
    CANCELLED --> [*]
```

Правила при создании заявки:

```mermaid
flowchart TD
    start["requestTestDrive(clientId, carModelId, дата и время)"] --> f1{"все поля заполнены?"}
    f1 -- нет --> e1["DomainValidationException"]
    f1 -- да --> f2{"клиент и автомобиль существуют?"}
    f2 -- нет --> e2["EntityNotFoundException"]
    f2 -- да --> f3{"автомобиль в списке<br/>для тест-драйва?"}
    f3 -- нет --> e3["DomainValidationException"]
    f3 -- да --> f4{"дата в будущем?"}
    f4 -- нет --> e4["DomainValidationException"]
    f4 -- да --> f5{"слот свободен?"}
    f5 -- нет --> e5["DomainValidationException"]
    f5 -- да --> ok["Заявка сохранена<br/>в статусе REQUESTED"]
```

Текущее время сервис берёт из внедряемого `Clock`, поэтому проверка «дата в будущем» детерминированно
воспроизводится в тестах.

---

## Хранилище данных

Данные живут в памяти процесса: репозитории работают поверх простых коллекций, изолируя бизнес-слой
от способа хранения.

```mermaid
flowchart LR
    subgraph app["Бизнес-слой"]
        s1["CarModelService"]
        s2["Сервисы заказов"]
        s3["TestDriveService"]
        s4["Сервисы запчастей"]
        s5["ClientService · EmployeeService"]
    end

    subgraph api["Интерфейсы репозиториев"]
        i1["CarModelRepository"]
        i2["OrderReadyCarRepository<br/>OrderCustomCarRepository"]
        i3["TestDriveRequestRepository<br/>TestDriveCarRepository"]
        i4["*SparePartRepository"]
        i5["ClientRepository<br/>EmployeeRepository"]
    end

    subgraph infra["In-memory реализация"]
        r1["InMemoryCarModelRepository"]
        r2["InMemoryOrder*Repository"]
        r3["InMemoryTestDrive*Repository"]
        r4["InMemory*Repository запчастей"]
        r5["InMemoryClientRepository<br/>InMemoryEmployeeRepository"]
    end

    subgraph store["Структуры хранения"]
        m1["HashMap: UUID → CarModel"]
        m2["HashMap: UUID → Order"]
        m3["HashMap: UUID → TestDriveRequest<br/>HashSet: UUID"]
        m4["HashMap: UUID → SparePart"]
        m5["HashMap: UUID → Client<br/>HashMap: UUID → Employee"]
    end

    s1 --> i1 --> r1 --> m1
    s2 --> i2 --> r2 --> m2
    s3 --> i3 --> r3 --> m3
    s4 --> i4 --> r4 --> m4
    s5 --> i5 --> r5 --> m5
```

Все репозитории следуют единому контракту `save` / `findById` / `findAll` / `deleteById`;
`findById` для отсутствующего идентификатора бросает `EntityNotFoundException`.

---

## Фильтрация каталога

`SuitableCars` собирает выборку из репозитория и применяет фильтры через Stream API.
Пустое поле фильтра означает «критерий не применяется».

| Критерий | Поле фильтра |
|---|---|
| Цена автомобиля | `minBasePrice`, `maxBasePrice`, `minTotalPrice`, `maxTotalPrice` |
| Бренд | `brand` |
| Модель | `model` — доступна только при заданном бренде |
| Цвет автомобиля | `color` |
| Кузов | `bodyType` |
| Тип топлива | `fuelType` |
| Мощность двигателя | `minPower`, `maxPower` |
| Объём двигателя | `minEngineDisplacement`, `maxEngineDisplacement` |
| Тип КПП | `gearboxType` |
| Привод | `drive` |
| Цвет салона | `interiorColor` |

Попытка отфильтровать по модели без выбранного бренда отклоняется:
`DomainValidationException: Model filter is available only when brand is chosen`.

---

## Обработка ошибок

| Исключение | Когда возникает | Примеры сообщений |
|---|---|---|
| `DomainValidationException` | Нарушены правила домена или не заполнены обязательные данные | `missing required component "Interior"`, `missing required field "client"`, `Test drive can be booked only for the future` |
| `IncompatibleComponentException` | Выбранный узел не совместим с моделью автомобиля | `Spare part <id> is not compatible with this car model: <id>` |
| `EntityNotFoundException` | Сущность с указанным идентификатором отсутствует | `CarModel with id <id> not found` |

Консоль перехватывает исключения на уровне меню и печатает `Error: <сообщение>`, не прерывая сессию.

---

## Тестирование

- **158 тестов** в 17 классах: JUnit 5 + Mockito.
- **Покрытие строк — около 91%**, порог проверяется автоматически.
- `jacocoTestCoverageVerification` подключён к задаче `check`: сборка падает, если покрытие строк
  опустится ниже **70%**.

Что покрыто:

| Уровень | Проверяется |
|---|---|
| Юнит-тесты сервисов | Логика на моках репозиториев: расчёт стоимости, совместимость, назначение менеджера, фильтры, валидация |
| Доменные тесты | Арифметика и округление `Money`, таблицы переходов статусов, билдеры узлов |
| Сценарные тесты | Сквозные сценарии на реальных in-memory репозиториях: заказ, конфигурация, тест-драйв, CRUD |
| Тесты консоли | Меню прогоняются со сценарным вводом и проверкой вывода, включая ошибочные данные |

```bash
./gradlew test                      # прогон тестов
./gradlew jacocoTestReport          # отчёт: build/reports/jacoco/test/html/index.html
./gradlew build                     # сборка + тесты + проверка порога покрытия
```

---

## Запуск

### Локально

```bash
./gradlew installDist
./build/install/car-dealership/bin/car-dealership
```

### В Docker

```bash
docker build -f docker/Dockerfile -t car-dealership .
docker run -it --rm car-dealership
```

Образ собирается в два этапа: сборка дистрибутива на JDK-образе и запуск на JRE-образе.

После старта приложение наполняется демонстрационными данными и открывает меню выбора роли:

```
=== Car dealership ===
1) Sign in as client
2) Sign in as sales manager
3) Sign in as warehouse administrator
4) Sign in as system administrator
0) back
```

---

## Демонстрационные данные

При старте создаются каталог, склад узлов, сотрудники и клиенты — систему можно проверять сразу.

**Автомобили**

| Бренд и модель | Цвет | Базовая цена | Привод | Кузов | Двигатель |
|---|---|---|---|---|---|
| BMW 320i | white | 3 000 000 ₽ | задний | седан | 2.0 Turbo, бензин, 184 л.с. |
| BMW 330i | black | 3 600 000 ₽ | полный | седан | 2.0 Turbo, бензин, 184 л.с. |
| Audi A4 Avant | blue | 3 200 000 ₽ | передний | универсал | 2.0 D, дизель, 190 л.с. |

**Сотрудники**

| Имя | Роль |
|---|---|
| Ivan Sokolov | `SALES_MANAGER` |
| Petr Volkov | `SALES_MANAGER` |
| Anna Orlova | `WAREHOUSE_ADMINISTRATOR` |
| Maria Titova | `SYSTEM_ADMINISTRATOR` |

**Клиенты:** Alexey Ivanov, Olga Smirnova.
**Доступны для тест-драйва:** BMW 320i и BMW 330i.
