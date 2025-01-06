package com.project.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface RAGAssistant {

    @SystemMessage(
        """
                You are a helpful assistant. Try to respond in a fair and warm manner.
                Don't mention about the name if the document, like 'Culture.pdf' ans so on.
                """
    )
    String chat(@MemoryId Long memoryId, @UserMessage String userMessage);

    @SystemMessage(
        """
            Now, generate a question in user language to test candidate based on the context.
            If answer is not correct, just tell it.
            """
    )
    String generateTest(@MemoryId Long memoryId, @UserMessage String userMessage);

    @SystemMessage(
        """
            Check if it is the user answer to the previous question
            """
    )
    boolean isUserAnswer(@MemoryId Long memoryId, @UserMessage String userMessage);
}
