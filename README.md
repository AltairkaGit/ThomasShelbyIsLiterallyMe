﻿# 300Notes Backend

# 300lines_MISIS 💗 Моя любимая команда 💗

## Кривцов Кирилл - Backend

## Демидович Сергей - Android

## Дорохов Иван - Android

## Кузнецова Дарья - Дизайн и стиль

-----------------------------------

## Видео-демонстрация

### Увы, гитхаб не дает загрузить видосы, но мы нашли выход и кинули их на диск!!
### https://disk.yandex.ru/d/ZiRL0io2qDRtOg

## Мобильное приложение здесь

### https://github.com/seregatheone/challengeHack


# О проекте:

В наше нелегкое время бывает так трудно провести время с дорогими и близкими людьми. Работа, учеба, дела - знакомо аболютно каждому. 

Наш сервис предоставляет возможность провести досуг с друзьями и родными путем совместного прослушивания музыки - одного из самых приятных занятий.

Мы дали вам возможность наслаждаться общением и контентом так, что даже расстояние не может стать помехой. 

Благодаря использованию новых технологий скорость загрузки стала выше, а задержки - меньше.

Ждем вас в нашем сервисе!!


# Запуск: 
1. Склонировать репозиторий


    git clone git@github.com:AltairkaGit/ThomasShelbyIsLiterallyMe.git
    

2. Создать в папке файл env.properties

   
    DB_SOURCE=dbContainer:port

    DB_NAME=DbName

    DB_USER=DbUser
    
    DB_PASSWORD=DbPassword
   
    DB_SCHEMA=DbSchema
   
    JWT_TOKEN_SECRET=secret
 
3.  Воспользоваться docker-compose:

    
    docker-compose --env-file=./env.properties -f docker-compose.yml up -d


## Документация

### API доступно по url http://94.45.223.241:46877/api/v2/

### Сваггер: http://94.45.223.241:46877/swagger-ui/index.html#/

## WebSockets

### Подключение

1. Подключение по ws://94.45.223.241:46876/ws (также есть поддержка SockJS)
2. При подключении нужно послать либо Authorization header в виде Authorization: token, либо cookie в виде Authorization=token; path=/
3. Далее используется протокол STOMP для обмена сообщениями

В приложении для подписки и пушей испольуется единый префикс /app

## Room

#### Лидер:

Чтобы добавить трек в очередь воспользуйтель методом send STOMP-клиента по пути /app/room/{roomId}/tracks/add [trackId]

Чтобы приостановить прослушивание треков воспользуйтель методом send STOMP-клиента по пути /app/room/{roomId}/tracks/pause

Чтобы возобновить прослушивание треков воспользуйтель методом send STOMP-клиента по пути /app/room/{roomId}/tracks/release

#### Гости:

Чтобы предложить послушать трек в очередь прослушивания воспользуйтель методом send STOMP-клиента по пути app/room/{roomId}/tracks/offer [trackId]


#### Для всех:

### Приглашения

Чтобы получать события о перемещениях участников группы воспоспользуйтесь subscribe STOMP-клиента по пути /app/queue/room/{roomId}/users
Формат сообщений: "{event}:{userId}"

events: accept, decline, left

Лидер может отправить приглашение в комнату воспользовавшись send STOMP-клиента по пути /app/room/{roomId}/users/offer/send [userId]

Лидер может выгнать участника из группы воспользовавшись send STOMP-клиента по пути /app/room/{roomId}/users/remove [userId]

Пользователи могут покинуть группу воспользовавшись send STOMP-клиента по пути /app/room/{roomId}/users/leave

Пользователи могут принять приглашение воспользовавшись send STOMP-клиента по пути /app/room/{roomId}/users/offer/accept [roomId]

Пользователи могут отклонить приглашение воспользовавшись send STOMP-клиента по пути /app/room/{roomId}/users/offer/decline [roomId]

##### Голосовая связь WebRTC

Чтобы подписаться на получение SDP собеседников воспользуйтесь методом subscribe STOMP-клиента по пути /app/queue/room/{roomId}/voiceChat

Чтобы отправить SDP собеседникам воспользуйтесь методом send STOMP-клиента по пути /app/queue/room/{roomId}/voiceChat [SDP]

##### Чат

Чтобы подписаться на сообщения чата воспользуйтесь методом subscribe STOMP-клиента по пути /app/queue/room/{roomId}/chat

Чтобы отправить сообщение в чат воспользуйтесь методом send STOMP-клиента по пути /app/room/{roomId}/chat [Content]
