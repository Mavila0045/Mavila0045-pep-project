package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;
    public SocialMediaController() {
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::postMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages.", this::getAllMessagesHandler);
        app.get("/messages", this::getAllMessagesByUserHandler);
        app.get("/localhost:8080/messages/{message_id}", this::getMessagesByIdHandler);

        app.post("/register", this::postAccountHandler);
        app.post("/login", this::getLoginUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage == null){
            ctx.status(400);
        }
        else {
            System.out.println(mapper.writeValueAsString(createdMessage));
            ctx.json(mapper.writeValueAsString(createdMessage));
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageByMessage_Id(message_id, message);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }
        else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

        private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByMessage_Id(message_id);
        System.out.println(deletedMessage);
        ctx.json(mapper.writeValueAsString(deletedMessage));
    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.retrieveAllMessages());
    }

    private void getAllMessagesByUserHandler(Context ctx){
        ctx.json(messageService.retrieveAllMessagesForUser(Integer.parseInt(ctx.pathParam("posted_by"))));
    }

    private void getMessagesByIdHandler(Context ctx){
        ctx.json(messageService.retrieveAllMessagesForUser(Integer.parseInt(ctx.pathParam("message_id"))));
    }

    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.registerUser(account);
        if(createdAccount == null){
            ctx.status(400);
        }
        else {
            ctx.json(mapper.writeValueAsString(createdAccount));
        }
    }

    private void getLoginUser(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedIn = accountService.loginUser(account);
        if(loggedIn == null){
            ctx.status(401);
        }
        else {
            ctx.json(mapper.writeValueAsString(loggedIn));
        }
    }
}