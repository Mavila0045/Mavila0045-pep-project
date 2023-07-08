package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public List<Message> retrieveAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public List<Message> retrieveAllMessagesForUser(int posted_by) {
        return this.messageDAO.getMessagesByUser(posted_by);
    }

    public Message retrieveMessagebyMessage_id(int message_id) {
        return this.messageDAO.getMessageById(message_id);
    }

    public Message createMessage(Message message) {
        return this.messageDAO.insertMessage(message);
    }

    public Message deleteMessageByMessage_Id(int id) {
        Message message = this.messageDAO.getMessageById(id);
        if(message != null) {
            this.messageDAO.deleteMessageById(id);
            return message;
        }
        
        return null;
    }

    public Message updateMessageByMessage_Id(int id, Message message) {
        this.messageDAO.updateMessage(id, message);
        return this.messageDAO.getMessageById(id);
    }
}
