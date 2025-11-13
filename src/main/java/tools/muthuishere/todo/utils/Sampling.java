package tools.muthuishere.todo.utils;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@UtilityClass
public class Sampling {

    public static String createSamplingRequest(McpSyncServerExchange exchange, String systemPrompt, String userPrompt) {
        // Validate input parameters
        Assert.notNull(exchange, "Exchange must not be null");
        Assert.notNull(systemPrompt, "System Prompt must not be null");
        Assert.notNull(userPrompt, "User Prompt must not be null");

        String output = "";
        log.info("Creating Sampling Request");
        logSamplingStart(exchange);
        if (isSamplingCapabilityAvailable(exchange)) {
            output = performSampling(exchange, systemPrompt, userPrompt);
        }
        return output;
    }

    private static void logSamplingStart(McpSyncServerExchange exchange) {
        exchange.loggingNotification(McpSchema.LoggingMessageNotification.builder()
                .level(McpSchema.LoggingLevel.INFO)
                .data("Start sampling")
                .build());
    }

    private static boolean isSamplingCapabilityAvailable(McpSyncServerExchange exchange) {
        return exchange.getClientCapabilities().sampling() != null;
    }

    private static String performSampling(McpSyncServerExchange exchange, String systemPrompt, String userPrompt) {
        var request = McpSchema.CreateMessageRequest.builder()
                .systemPrompt(systemPrompt)
                .messages(List.of(
                        new McpSchema.SamplingMessage(
                                McpSchema.Role.USER,
                                new McpSchema.TextContent(userPrompt))))
                .build();

        McpSchema.CreateMessageResult result = exchange.createMessage(request);
        return ((McpSchema.TextContent) result.content()).text();
    }
}
