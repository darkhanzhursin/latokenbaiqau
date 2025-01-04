package com.project.config;

import com.project.bot.TelegramBot;
import com.project.service.EmbeddingComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramConfig {

    private final EmbeddingComponent embeddingComponent;

    @Bean
    public TelegramBot telegramBot(@Value("${bot.name}") String botName,
                                   @Value("${bot.token}") String botToken) {
        TelegramBot telegramBot = new TelegramBot(botName, botToken);
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            log.info("Loading single document start");
            embeddingComponent.loadSingleDocument();
            log.info("Loading single document end");
        } catch (TelegramApiException e) {
            log.error("Exception during registration telegram api: {}", e.getMessage());
        }
        return telegramBot;
    }
}
