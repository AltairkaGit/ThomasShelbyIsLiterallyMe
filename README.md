# 300Notes Backend

## Запуск: 
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


## REST API

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

#### Остальные:

Чтобы предложить добавить трек в очередь прослушивания воспользуйтель методом send STOMP-клиента по пути app/room/{roomId}/tracks/offer [trackId]



#### Для всех:

### Приглашения

Чтобы предложение в группу воспользуйтесь методом subscribe STOMP-клиента по пути /app/queue/chat/messages/new
Формат сообщений: "{chatId}:{messageId}"

Чтобы подписаться на уведомления о приглашениях в группы воспользуйтесь методом subscribe STOMP-клиента по пути /app/queue/chat/messages/new
Формат сообщений: "{chatId}:{messageId}"

##### Голосовая связь WebRTC

Чтобы подписаться на получение SDP собеседников воспользуйтесь методом subscribe STOMP-клиента по пути /app/queue/room/{roomId}/voiceChat

Чтобы отправить SDP собеседникам воспользуйтесь методом send STOMP-клиента по пути /app/queue/room/{roomId}/voiceChat [SDP]

##### Чат

Чтобы подписаться на сообщения чата воспользуйтесь методом subscribe STOMP-клиента по пути /app/queue/room/{roomId}/chat

Чтобы отправить сообщение в чат воспользуйтесь методом send STOMP-клиента по пути /app/room/{roomId}/chat [Content]