package tools.muthuishere.todo.todo.prompts;

import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoPrompts {

    @McpPrompt(
            name = "create-todo-prompt",
            description = "Prompt to create a new Todo item"
    )
    public McpSchema.GetPromptResult createTodoPrompt(
            @McpArg(
                    name = "title",
                    description = "Title of the Todo item",
                    required = true
            )
            String title
    ) {

        String message = "Add this " + title + " as a new todo item.";

        return new McpSchema.GetPromptResult(
                "Create a new Todo Item",
                List.of(new McpSchema.PromptMessage(McpSchema.Role.USER, new McpSchema.TextContent(message)))
        );

    }

    @McpPrompt(
            name = "list-todos-prompt",
            description = "Prompt to list all Todo items"
    )
    public McpSchema.GetPromptResult listTodosPrompt() {

        String message = "List all the todo items.";

        return new McpSchema.GetPromptResult(
                "List all Todo Items",
                List.of(new McpSchema.PromptMessage(McpSchema.Role.USER, new McpSchema.TextContent(message)))
        );

    }
}
