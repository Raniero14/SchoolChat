package it.raniero.schoolchat.database;

import it.raniero.schoolchat.database.mysql.types.Message;

import java.util.Set;

public interface IMessageDao {

    void createTables();
    Set<Message> getMessagesFromChatId(long chatId);

    Set<Message> getMessagesFromSenderId(long senderId);

    void createMessage(Message.MessageType type, long senderId, long chatId, String text);

    Set<Message> getMessages(long senderId,long chatId);

}
