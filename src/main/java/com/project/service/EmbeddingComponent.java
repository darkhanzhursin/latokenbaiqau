package com.project.service;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

import com.project.config.AIConfig;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmbeddingComponent {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;

    //private final AIConfig config;


    /**
     * This method loads a document from a file and processes it into chunks.
     * Then it uses the embedding model to generate embeddings for the chunks.
     * Finally, it adds the embeddings to the embedding store.
     */
    public void loadSingleDocument() {
        // Get the current directory
        String currentDir = System.getProperty("user.dir");
        // Path to the file to be loaded
        String fileName = "/Culture.pdf";
        // Load the document from the file
        Document document = loadDocument(currentDir + fileName, new ApachePdfBoxDocumentParser());

        // Create an ingester
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
            // Split the document into chunks
            .documentSplitter(DocumentSplitters.recursive(300, 10))
            // Use the embedding model to generate the embeddings
            .embeddingModel(embeddingModel)
            // Store the embeddings in the embedding store
            .embeddingStore(embeddingStore)
            .build();

        // Add the embeddings to the store
        embeddingStoreIngestor.ingest(document);
    }
}
