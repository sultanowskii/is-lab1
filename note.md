# Про postgres, логи, batch-insert-ы

## Логирование

Конфиг у меня на локалке: `/var/lib/postgres/data/postgresql.conf` ([как найти](https://stackoverflow.com/a/3603162))

[Мини-гайд на stackoverflow](https://stackoverflow.com/a/34664326)

## Просмотр логов

```bash
> su -
$ cd /var/lib/postgres/data/log
$ cat postgresql-2024-12-08_221100.log
```

## Почему bulk insert может не работать

[Источник](https://stackoverflow.com/a/50882952)

1. `GenerationType = IDENTITY` не поддерживает bulk insert

    [Источник](https://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch15.html)

2. Не установлены настройки в `application.properties`

    ```properties
    spring.jpa.properties.hibernate.jdbc.batch_size=32
    spring.jpa.properties.hibernate.order_inserts=true
    spring.jpa.properties.hibernate.order_updates=true
    ```

    [Источник](https://vladmihalcea.com/how-to-batch-insert-and-update-statements-with-hibernate/)

3. Похоже, что spring печатает не совсем правду

    Т.к. после всех правок выше спринг продолжил печатать 2 insert-а:

    ```logs
    Hibernate: insert into location (created_at,name,owner,updated_at,updated_by,x,y,z,id) values (?,?,?,?,?,?,?,?,?)
    2024-12-08T22:37:32.179+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (1:TIMESTAMP_UTC) <- [2024-12-08T22:37:32.179302+03:00[Europe/Moscow]]
    2024-12-08T22:37:32.179+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (2:VARCHAR) <- [Moscow]
    2024-12-08T22:37:32.179+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (3:INTEGER) <- [2]
    2024-12-08T22:37:32.179+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (4:TIMESTAMP_UTC) <- [null]
    2024-12-08T22:37:32.179+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (5:INTEGER) <- [null]
    2024-12-08T22:37:32.179+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (6:FLOAT) <- [0.0]
    2024-12-08T22:37:32.179+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (7:FLOAT) <- [1.0]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (8:DOUBLE) <- [2.0]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (9:INTEGER) <- [68]
    2024-12-08T22:37:32.180+03:00 DEBUG 126257 --- [nio-8080-exec-3] org.hibernate.SQL                        : insert into location (created_at,name,owner,updated_at,updated_by,x,y,z,id) values (?,?,?,?,?,?,?,?,?)
    Hibernate: insert into location (created_at,name,owner,updated_at,updated_by,x,y,z,id) values (?,?,?,?,?,?,?,?,?)
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (1:TIMESTAMP_UTC) <- [2024-12-08T22:37:32.180217+03:00[Europe/Moscow]]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (2:VARCHAR) <- [Volgograd]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (3:INTEGER) <- [2]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (4:TIMESTAMP_UTC) <- [null]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (5:INTEGER) <- [null]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (6:FLOAT) <- [9.0]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (7:FLOAT) <- [4.0]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (8:DOUBLE) <- [7.0]
    2024-12-08T22:37:32.180+03:00 TRACE 126257 --- [nio-8080-exec-3] org.hibernate.orm.jdbc.bind              : binding parameter (9:INTEGER) <- [69]
    2024-12-08T22:37:32.181+03:00 DEBUG 126257 --- [nio-8080-exec-3] org.hibernate.SQL                        : insert into person (created_at,eye_color,hair_color,height,location_id,name,owner,updated_at,updated_by,id) values (?,?,?,?,?,?,?,?,?,?)
    ```

    Тогда как в postgres оно действительно отображалось как bulk insert:

    ```logs
    2024-12-08 22:37:32.181 MSK [126293] LOG:  execute <unnamed>: insert into location (created_at,name,owner,updated_at,updated_by,x,y,z,id) values ($1,$2,$3,$4,$5,$6,$7,$8,$9),($10,$11,$12,$13,$14,$15,$16,$17,$18)
    2024-12-08 22:37:32.181 MSK [126293] DETAIL:  parameters: $1 = '2024-12-08 22:37:32.179302+03', $2 = 'Moscow', $3 = '2', $4 = NULL, $5 = NULL, $6 = '0', $7 = '1', $8 = '2', $9 = '68', $10 = '2024-12-08 22:37:32.180217+03', $11 = 'Volgograd', $12 = '2', $13 = NULL, $14 = NULL, $15 = '9', $16 = '4', $17 = '7', $18 = '69'
    ```
