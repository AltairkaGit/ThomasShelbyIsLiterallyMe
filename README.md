# 300Notes Backend

## REST API

### API доступно по url http://94.45.223.241:46877/api/v2/

### Сваггер: http://94.45.223.241:46877/swagger-ui/index.html#/

Используем *-rest-controllers-v2(/api/v2/*)

## WebSockets

### Подключение

1. Подключение по ws://94.45.223.241:46876/ws (также есть поддержка SockJS)
2. При подключении нужно послать либо Authorization header в виде Authorization: token, либо cookie в виде Authorization=token; path=/
3. Далее используется протокол STOMP для обмена сообщениями

В приложении для подписки и пушей испольуется единый префикс /app

### Room

#### Chat

Чтобы подписаться на сообщения чата нужно использовать метод subscribe STOMP-клиента по пути /app/queue/room/{roomId}/chat

Чтобы отправить сообщение в чат нужно использовать метод send STOMP-клиента по пути /app/room/{roomId}/chat

#### UserEvents

Чтобы подписаться на уведомления о новых сообщениях нужно использовать метод subscribe STOMP-клиента по пути /app/queue/chat/messages/new
Формат сообщений: "{chatId}:{messageId}"

Чтобы подписаться на уведомления о новых сообщениях нужно использовать метод subscribe STOMP-клиента по пути /app/queue/chat/messages/new
Формат сообщений: "{chatId}:{messageId}"

#### WebRTC

Чтобы подписаться на получение SDP собеседников нужно использовать метод subscribe STOMP-клиента по пути /app/queue/room/{roomId}/voiceChat

Чтобы отправить SDP собеседникам нужно использовать метод send STOMP-клиента по пути /app/queue/room/{roomId}/voiceChat

