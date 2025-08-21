```pgsql 
server/
├─ pom.xml
├─ src/main/java/com/ailianlian/server
│  ├─ App.java
│  ├─ config/
│  │   ├─ MyBatisConfig.java
│  │   ├─ RedisConfig.java
│  │   └─ WebSocketConfig.java
│  ├─ controller/
│  │   ├─ ChatController.java
│  │   └─ CharacterController.java
│  ├─ domain/
│  │   ├─ dto/ ChatSendDTO.java  CharacterCreateDTO.java
│  │   ├─ vo/  ChatReplyVO.java  MessageVO.java  AiCharacterVO.java
│  │   └─ entity/
│  │       ├─ AiCharacter.java
│  │       ├─ Conversation.java
│  │       ├─ Message.java
│  │       ├─ UserProfile.java
│  │       └─ PromptTemplate.java
│  ├─ mapper/  (接口)
│  │   ├─ AiCharacterMapper.java
│  │   ├─ ConversationMapper.java
│  │   ├─ MessageMapper.java
│  │   ├─ UserProfileMapper.java
│  │   └─ PromptTemplateMapper.java
│  ├─ service/
│  │   ├─ DialogService.java
│  │   ├─ EmotionClient.java
│  │   ├─ LlmClient.java
│  │   ├─ MessageService.java
│  │   ├─ ProfileService.java
│  │   └─ CharacterService.java
│  └─ websocket/
│      └─ WsPushService.java
└─ src/main/resources
├─ application.yml
├─ mapper/
│   ├─ AiCharacterMapper.xml
│   ├─ ConversationMapper.xml
│   ├─ MessageMapper.xml
│   ├─ UserProfileMapper.xml
│   └─ PromptTemplateMapper.xml
└─ db/init.sql
```


