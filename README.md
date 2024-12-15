# Лабораторная работа #2

Вариант: `986500`

Доработать ИС из ЛР1 следующим образом:

- Добавить в систему возможность массового добавления объектов при помощи импорта файла. Формат для импорта необходимо согласовать с преподавателем. Импортируемый файл должен загружаться на сервер через интерфейс разработанного веб-приложения.
  - При реализации логики импорта объектов необходимо реализовать транзакцию таким образом, чтобы в случае возникновения ошибок при импорте, не был создан ни один объект.
  - При импорте должна быть реализована проверка пользовательского ввода в соответствии с ограничениями предметной области из ЛР1.
  - При наличии вложенных объектов в основной объект из ЛР1 необходимо задавать значения полей вложенных объектов в той же записи, что и основной объект.
- Необходимо добавить в систему интерфейс для отображения истории импорта (обычный пользователь видит только операции импорта, запущенные им, администратор - все операции).
  - В истории должны отображаться id операции, статус ее завершения, пользователь, который ее запустил, число добавленных объектов в операции (только для успешно завершенных).
- Согласовать с преподавателем и добавить в модель из первой лабораторной новые ограничения уникальности, проверяемые на программном уровне (эти новые ограничения должны быть реализованы в рамках бизнес-логики приложения и не должны быть отображены/реализованы в БД).
- Реализовать сценарий с использованием Apache JMeter, имитирующий одновременную работу нескольких пользователей с ИС, и проверить корректность изоляции транзакций, используемых в ЛР. По итогам исследования поведения системы при ее одновременном использовании несколькими пользователями изменить уровень изоляции транзакций там, где это требуется. Обосновать изменения.
  - Реализованный сценарий должен покрывать создание, редактирование, удаление и импорт объектов.
  - Реализованный сценарий должен проверять корректность поведения системы при попытке нескольких пользователей обновить и\или удалить один и тот же объект (например, двух администраторов).
  - Реализованный сценарий должен проверять корректность соблюдения системой ограничений уникальности предметной области при одновременной попытке нескольких пользователей создать объект с одним и тем же уникальным значением.

---

Во время запуска сценария начала возникать ошибка (при обновлении StudyGroup):

```java
Unexpected exception:redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketException: Broken pipe
```

Похоже, при большом кол-ве запросов начинаются проблемы с redis-ом - начинают появляться (и не пересоздаваться!) "сломанные" соединения.

```java
@Component
public class Redis {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    private JedisPool pool;

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(200);
        poolConfig.setMinIdle(20);
        // Maximum wait time when the connections are used up
        poolConfig.setMaxWait(Duration.ofMillis(3000));
        // When a connection is returned, a check will be performed first. Once the check fails, the connection will be terminated.
        poolConfig.setTestOnReturn(false);
        return poolConfig;
    }

    private JedisPool getPool() {
        return new JedisPool(buildPoolConfig(), host, port, 2000);
    }

    public Jedis getResource() {
        if (pool == null) {
            pool = getPool();
        }

        Jedis jedis;
        try {
            jedis = pool.getResource();
            jedis.ping();
        } catch (JedisException e) {
            pool.destroy();
            pool = getPool();
            jedis = pool.getResource();
        }

        return jedis;
    }
}
```

Добавив пул соединений, изменив код получения Jedis-а, ситуация не изменилась - тогда я вспомнил, что соединения по-хорошему надо бы закрывать:

```java
var jedis = redis.getResource();
jedis.del(Cache.CACHE_KEY_TOTAL_EXPELLED_STUDENTS);
jedis.close();
```

И действительно, после закрытия ресурса во всех местах использования redis, ошибок при работе с StudyGroup более не возникало.

---

```java
@Override
@Transactional
public LocationDto create(LocationCreateDto form) {
    String name = form.getName();
    if (locationWithName(name).isPresent()) {
        throw new ValidationException("Location with name=" + name + " already exists");
    }

    return super.create(form);
}

@Override
@Transactional
public LocationDto update(int id, LocationCreateDto form) {
    String name = form.getName();
    var maybeLocation = locationWithName(name);
    if (maybeLocation.isPresent() && maybeLocation.get().getId() != id) {
        throw new ValidationException("Location with name=" + name + " already exists");
    }

    return super.update(id, form);
}
```

![result/before.png](result/before.png)

Как видно, первый подход оказался неудачным: некоторые локации успевают создаваться с одинаковым именем, из-за чего мы наблюдаем неконсистентность: возможность/невозможность создать несколько локаций с одинаковым именем зависит от того, когда мы запустили запрос.

Проблема в том, что в рамках выполнения какого-либо запроса между "проверкой на наличие" и "созданием объекта" могут "втиснуться" другие запросы, и тогда на момент создания объекта проверка окажется невалидной.

Чтобы исправить это, установим уровень изоляции `SERIALIZABLE`, повесим `Retryable` для того, чтобы дать запросу шанс на исполнение, добавим обработчик исключения транзакции:

```java
@Override
@Retryable(
    retryFor = { CannotAcquireLockException.class },
    notRecoverable = { ValidationException.class },
    maxAttempts = 5,
    backoff = @Backoff(delay = 100)
)
@Transactional(isolation = Isolation.SERIALIZABLE)
public LocationDto create(LocationCreateDto form) {
    String name = form.getName();
    if (locationWithName(name).isPresent()) {
        throw new ValidationException("Location with name=" + name + " already exists");
    }

    return super.create(form);
}

@Override
@Retryable(
    retryFor = { CannotAcquireLockException.class },
    notRecoverable = { ValidationException.class },
    maxAttempts = 5,
    backoff = @Backoff(delay = 100)
)
@Transactional(isolation = Isolation.SERIALIZABLE)
public LocationDto update(int id, LocationCreateDto form) {
    String name = form.getName();
    var maybeLocation = locationWithName(name);
    if (maybeLocation.isPresent() && maybeLocation.get().getId() != id) {
        throw new ValidationException("Location with name=" + name + " already exists");
    }

    return super.update(id, form);
}

@Recover
public LocationDto handleCreateCannotAcquireLockException(CannotAcquireLockException e, LocationCreateDto form) {
    throw new ValidationException("Could not acquire lock for creating location: " + form.getName());
}

@Recover
public LocationDto handleUpdateCannotAcquireLockException(CannotAcquireLockException e, int id, LocationCreateDto form) {
    throw new ValidationException("Could not acquire lock for updating location: " + form.getName());
}
```

И действительно, теперь создается только 1 объект, все остальные успешно получают ожидаемый Bad Request:

![result/after.png](result/after.png)
