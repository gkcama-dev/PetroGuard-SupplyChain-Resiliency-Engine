package edu.petroresilience.engine.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class SupplyChainController {

    private final ChatModel chatModel;

    public SupplyChainController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ask-ai")
    public String askAI(@RequestParam(defaultValue = "What is the capacity of the Sapugaskanda Refinery?") String message) {

        String rules = "You are an expert Petroleum Supply Chain Engine. " +
                "1. To answer questions about refineries or facilities, you MUST call 'getFacilityDetails'. " +
                "2. Once you call the tool, you will receive real-time data from our Neo4j database. " +
                "3. Treat the tool's output as the ABSOLUTE TRUTH. " +
                "4. DO NOT say 'I need to query again' or 'Assumed answer'. " +
                "5. Provide the capacity directly (e.g., 'The capacity is 50,000 barrels').";

        // Here, we register the tool names correctly so the AI model can call them when needed
        Prompt prompt = new Prompt(
                List.of(new SystemMessage(rules), new UserMessage(message)),
                OpenAiChatOptions.builder()
                        .toolNames("getFacilityDetails", "checkSanctions")
                        .build()
        );

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}



