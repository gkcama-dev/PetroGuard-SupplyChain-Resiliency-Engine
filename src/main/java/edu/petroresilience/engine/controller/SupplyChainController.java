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

        String rules = "You are a Strategic Petroleum Advisor. " +
                "1. When asked about importing oil from a country, FIRST call 'checkSanctions'. " +
                "2. If the country is BLOCKED, call 'findAlternativeSuppliers' to find safe options. " +
                "3. CRITICAL RULE: If the tool returns a CRITICAL WARNING or no suppliers, YOU MUST NOT invent, guess, or search for alternative countries. " +
                "4. EMERGENCY PROTOCOL: If no safe routes are available, declare a '🔴 SUPPLY CHAIN EMERGENCY: MARITIME BLOCKADE'. Recommend halting imports and immediately utilizing the strategic reserves at our current refinery. " +
                "5. PRICE LOGIC: The base oil price is $80 per barrel. If safe alternatives exist, calculate final price as: Base Price + Shipping Cost + 10% Supply Shock Premium. " +
                "6. Provide a final strategic recommendation based ONLY on the data provided by the tools.";

        // Here, we register the tool names correctly so the AI model can call them when needed
        Prompt prompt = new Prompt(
                List.of(new SystemMessage(rules), new UserMessage(message)),
                OpenAiChatOptions.builder()
                        .toolNames("getFacilityDetails", "checkSanctions","findAlternativeSuppliers")
                        .build()
        );

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}



