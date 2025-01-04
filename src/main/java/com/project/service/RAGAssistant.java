package com.project.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface RAGAssistant {

    @SystemMessage(
        """
                You are a helpful assistant. Try to respond in a fair and warm manner.
                If you don't know answer, just tell it.
                """
    )
    String chat(@MemoryId Long memoryId, @UserMessage String userMessage);
}
