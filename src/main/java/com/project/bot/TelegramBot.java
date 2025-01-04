package com.project.bot;

import com.project.service.RAGAssistant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;

    @Autowired
    private RAGAssistant ragAssistant;

    public TelegramBot(String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            var chatId = message.getChatId();
            log.info("Message received: {}", message.getChatId());
            var messageText = message.getText();
            var response = ragAssistant.chat(chatId, messageText);
            var testQuestion = ragAssistant.generateTest(chatId, messageText);
            try {
                if (isUserResponseToTheQuestion(chatId, message)) {
                    execute(new SendMessage(chatId.toString(), response));
                    return;
                }
                execute(new SendMessage(chatId.toString(), response));
                execute(new SendMessage(chatId.toString(), testQuestion));
            } catch (TelegramApiException e) {
                log.error("Exception during processing telegram api: {}", e.getMessage());
            }
        }
    }

    /**
     * Determines if the given message is a user's response to a question.
     *
     * @param message the message to evaluate
     * @return true if the message is a response to a question, false otherwise
     */
    private boolean isUserResponseToTheQuestion(Long chatId,Message message) {
        return ragAssistant.isUserAnswer(chatId, message.getText());
    }


    @Override
    public String getBotUsername() {
        return this.botName;
    }
}
