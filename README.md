# MCP (Model Context Protocol) server with advanced features including MCP Prompts, Sampling, and support for multiple protocols.

A production-ready Spring Boot application demonstrating the Model Context Protocol (MCP) with support for multiple transport protocols, MCP Prompts, and AI Sampling capabilities. This application showcases how to build a flexible MCP server that can operate via STDIO, SSE, Streamable HTTP, and Stateless HTTP protocols.

## 🚀 Features

- **Multiple Transport Protocols**: STDIO, SSE, Streamable HTTP, and Stateless HTTP
- **MCP Tools**: Complete CRUD operations exposed as discoverable MCP tools
- **MCP Prompts**: Pre-configured prompts for common todo operations
- **AI Sampling**: Integration with client AI capabilities for enhanced responses
- **Spring Boot 3.5.7** with Java 21
- **Spring AI 1.1.0-M4** with MCP support
- **H2 In-Memory Database** for persistence
- **REST API** endpoints for testing and health checks
- **Gradle** build with custom tasks for different profiles

## 📋 Prerequisites

- Java 21 or higher
- Gradle 8.14.3+ (included via wrapper)
- Optional: [Task](https://taskfile.dev/) for convenient task execution

## 🛠️ Tech Stack

- **Spring Boot 3.5.7**: Core framework
- **Spring AI 1.1.0-M4**: MCP server implementation
- **Spring Data JPA**: Data persistence layer
- **H2 Database**: In-memory database
- **Lombok**: Boilerplate reduction
- **Jakarta Validation**: Input validation

## 📦 Project Setup

The project uses the following key dependencies in `build.gradle`:

```gradle
dependencies {
    implementation platform("org.springframework.ai:spring-ai-bom:1.1.0-M4")
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'com.h2database:h2'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    implementation 'org.springframework.ai:spring-ai-starter-mcp-server-webmvc'
    implementation 'org.springframework.ai:spring-ai-starter-mcp-server'
    implementation 'org.springframework.ai:spring-ai-mcp-annotations'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

Repositories:
```gradle
repositories {
    mavenCentral()
    maven { url 'https://repo.spring.io/milestone' }
    maven { url 'https://repo.spring.io/snapshot' }
    maven {
        name = 'Central Portal Snapshots'
        url = 'https://central.sonatype.com/repository/maven-snapshots/'
    }
}
```

## ⚙️ Configuration Profiles

### Main Configuration (`application.properties`)
```properties
spring.application.name=todoapp
spring.profiles.active=stdio

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:todo-db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop

# Enable H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### STDIO Profile (`application-stdio.properties`)
```properties
# STDIO Profile - Standard Input/Output communication
# Uses spring-ai-starter-mcp-server dependency

# Disable web server for STDIO (no HTTP server needed)
spring.main.web-application-type=none

# Spring AI MCP Server STDIO configuration
spring.ai.mcp.server.stdio=true

# Disable Spring Boot banner
spring.main.banner-mode=off

###################################################################################
# Logging Configuration
###################################################################################

# Disable console logging completely for STDIO communication
logging.threshold.console=OFF

# Only use file logging
logging.file.name=${user.home}/mcp-server-stdio.log
logging.logback.rollingpolicy.max-file-size=10MB
logging.logback.rollingpolicy.max-history=10
# Set logging levels
logging.level.root=INFO
logging.level.org.springframework=WARN
logging.level.com.bothub.movie_mcp_server=DEBUG
# File log pattern
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
```

### SSE Profile (`application-sse.properties`)
```properties
# SSE WebMVC Profile - Server-Sent Events
# Uses spring-ai-starter-mcp-server-webmvc dependency
spring.ai.mcp.server.stdio=false
# Spring AI MCP Server SSE configuration
spring.ai.mcp.server.protocol=SSE

# Server configuration for WebMVC
server.port=8080

# Note: MCP server metadata (name, version, description) are defined in main application.properties
```

### Streamable Profile (`application-streamable.properties`)
```properties
# Streamable WebMVC Profile - Streamable HTTP
# Uses spring-ai-starter-mcp-server-webmvc dependency

# Spring AI MCP Server Streamable configuration
spring.ai.mcp.server.protocol=STREAMABLE

# Server configuration for WebMVC
server.port=8080

logging.level.root=INFO
logging.level.org.apache.tomcat.util.compat=ERROR
# Note: MCP server metadata (name, version, description) are defined in main application.properties
```
### Streamable Profile (`application-stateless.properties`)
```properties
# Streamable WebMVC Profile - Streamable HTTP
# Uses spring-ai-starter-mcp-server-webmvc dependency

# Spring AI MCP Server Streamable configuration
spring.ai.mcp.server.protocol=STATELESS

# Server configuration for WebMVC
server.port=8080

logging.level.root=INFO
logging.level.org.apache.tomcat.util.compat=ERROR
# Note: MCP server metadata (name, version, description) are defined in main application.properties
```

## 🔌 MCP Transport Protocols

The application supports four different MCP transport protocols:

| Protocol | Type | Use Case | Port |
|----------|------|----------|------|
| **STDIO** | Standard I/O | CLI tools, direct process communication | N/A |
| **SSE** | Server-Sent Events | Web clients, real-time updates | 8080 |
| **Streamable HTTP** | HTTP with streaming | Stateful HTTP connections | 8080 |
| **Stateless HTTP** | HTTP stateless | RESTful interactions | 8080 |

Switch between protocols by changing the active profile:
```bash
# STDIO (no web server)
./gradlew bootRun --args='--spring.profiles.active=stdio'

# SSE
./gradlew bootRun --args='--spring.profiles.active=sse'

# Streamable HTTP
./gradlew bootRun --args='--spring.profiles.active=streamable'

# Stateless HTTP
./gradlew bootRun --args='--spring.profiles.active=stateless'
```

## 📊 Architecture & Components

### Domain Model

### Todo Entity (`Todo.java`)
```java
package tools.muthuishere.todo.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private boolean completed;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
```

**Key Points:**
- `@Entity`: Marks this as a JPA entity for database persistence
- `@Data`: Lombok annotation that generates getters, setters, toString, equals, and hashCode
- `@Builder`: Lombok annotation that provides a builder pattern for object creation
- `@NotBlank`: Validation annotation ensuring title is not empty
- Auto-generated ID with `@GeneratedValue`

### Todo Response Model (`TodoToolResponse.java`)
```java
package tools.muthuishere.todo.todo.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoToolResponse {
    private Todo todo;
    private String fact;
}
```

**Purpose:** This wrapper class is used by MCP tools to return both the Todo object and AI-generated facts using the Sampling capability.

### Repository Layer

### Todo Repository (`TodoRepository.java`)
```java
package tools.muthuishere.todo.todo;

import tools.muthuishere.todo.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
```

**Key Points:**
- Extends `JpaRepository<Todo, Long>` providing CRUD operations automatically
- Spring Data JPA generates implementation at runtime
- No custom queries needed for basic operations
- `Long` represents the ID type of the Todo entity

### Service Layer

### Todo Service (`TodoService.java`)
```java
package tools.muthuishere.todo.todo;

import tools.muthuishere.todo.todo.model.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    public Optional<Todo> updateTodo(Long id, Todo todoDetails) {
        return todoRepository.findById(id).map(todo -> {
            todo.setTitle(todoDetails.getTitle());
            todo.setDescription(todoDetails.getDescription());
            todo.setCompleted(todoDetails.isCompleted());
            todo.setUpdatedAt(LocalDateTime.now());
            return todoRepository.save(todo);
        });
    }

    public boolean deleteTodo(Long id) {
        return todoRepository.findById(id).map(todo -> {
            todoRepository.delete(todo);
            return true;
        }).orElse(false);
    }
}
```

**Key Points:**
- `@Service`: Marks this as a Spring service component
- `@RequiredArgsConstructor`: Lombok generates constructor for final fields (dependency injection)
- **getAllTodos()**: Retrieves all todos from database
- **getTodoById()**: Returns Optional<Todo> - handles case when todo doesn't exist
- **createTodo()**: Sets timestamps and saves new todo
- **updateTodo()**: Uses Optional.map() for safe updates, returns empty if todo not found
- **deleteTodo()**: Returns boolean indicating success/failure

## 🔧 MCP Tools

MCP Tools expose application functionality to AI clients through the `@McpTool` annotation. The application provides five discoverable tools:

### Todo Tools Implementation (`TodoTools.java`)

Located in `src/main/java/tools/muthuishere/todo/todo/tools/TodoTools.java`, this component exposes five MCP tools with AI Sampling integration:

```java
@Component
@RequiredArgsConstructor
public class TodoTools {
    private final TodoService todoService;

    @McpTool(name = "fetch-all-todos", description = "Gets all Todo items")
    public List<Todo> fetchAllTodos() {
        return todoService.getAllTodos();
    }

    @McpTool(name = "fetch-todo-by-id", description = "Gets a Todo item by ID")
    public Optional<Todo> fetchTodoById(
            @McpToolParam(description = "id for the Item")
            Long id
    ) {
        return todoService.getTodoById(id);
    }

    @McpTool(name = "make-todo", description = "Creates a new Todo item")
    public TodoToolResponse makeTodo(
            @McpToolParam(description = "Title for the Todo")
            String title,
            @McpToolParam(description = "Description for the Todo")
            String description,
            @McpToolParam(description = "Is the Todo completed?")
            boolean completed,
            McpSyncServerExchange serverExchange
    ) {
        Todo todo = Todo.builder()
                .title(title)
                .description(description)
                .completed(completed)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Todo savedTodo = todoService.createTodo(todo);

        // AI Sampling - get interesting fact about the todo
        String fact = Sampling.createSamplingRequest(
                serverExchange,
                "You are an expert todo list assistant. Provide an interesting fact related to todo lists or productivity.",
                "Share an interesting fact about this todo item: " + title
        );

        return TodoToolResponse.builder()
                .todo(savedTodo)
                .fact(fact)
                .build();
    }

    @McpTool(name = "change-todo", description = "Updates an existing Todo item")
    public Optional<Todo> changeTodo(
            @McpToolParam(description = "id for the Item") Long id,
            @McpToolParam(description = "Title for the Todo") String title,
            @McpToolParam(description = "Description for the Todo") String description,
            @McpToolParam(description = "Is the Todo completed?") boolean completed
    ) {
        return todoService.getTodoById(id).map(todo -> {
            todo.setTitle(title);
            todo.setDescription(description);
            todo.setCompleted(completed);
            todo.setUpdatedAt(LocalDateTime.now());
            return todoService.createTodo(todo);
        });
    }

    @McpTool(name = "remove-todo", description = "Deletes a Todo item by ID")
    public boolean removeTodo(
            @McpToolParam(description = "id for the Item") Long id
    ) {
        return todoService.getTodoById(id).map(todo -> {
            todoService.deleteTodo(id);
            return true;
        }).orElse(false);
    }
}
```

**Key Features:**
- `@McpTool`: Exposes methods as discoverable MCP tools
- `@McpToolParam`: Provides parameter descriptions for AI clients
- **AI Sampling**: The `make-todo` tool uses `McpSyncServerExchange` to request AI-generated facts
- **Optional handling**: Graceful error handling using Java Optional
- **Builder pattern**: Clean object construction with Lombok

### Available MCP Tools

| Tool Name | Description | Parameters |
|-----------|-------------|------------|
| `fetch-all-todos` | Retrieves all todo items | None |
| `fetch-todo-by-id` | Gets a specific todo by ID | `id: Long` |
| `make-todo` | Creates a new todo with AI fact | `title: String`, `description: String`, `completed: boolean` |
| `change-todo` | Updates an existing todo | `id: Long`, `title: String`, `description: String`, `completed: boolean` |
| `remove-todo` | Deletes a todo by ID | `id: Long` |

## 💬 MCP Prompts

The application includes pre-configured prompts for common operations in `src/main/java/tools/muthuishere/todo/todo/prompts/TodoPrompts.java`:

```java
@Service
public class TodoPrompts {

    @McpPrompt(
            name = "create-todo-prompt",
            description = "Prompt to create a new Todo item"
    )
    public McpSchema.GetPromptResult createTodoPrompt(
            @McpArg(name = "title", description = "Title of the Todo item", required = true)
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
```

### Available Prompts

| Prompt Name | Description | Arguments |
|-------------|-------------|-----------|
| `create-todo-prompt` | Generates a prompt to create a new todo | `title: String` (required) |
| `list-todos-prompt` | Generates a prompt to list all todos | None |

## 🤖 AI Sampling Integration

The application includes a `Sampling` utility class (`src/main/java/tools/muthuishere/todo/utils/Sampling.java`) that enables AI-powered enhancements:

```java
public static String createSamplingRequest(
    McpSyncServerExchange exchange, 
    String systemPrompt, 
    String userPrompt
) {
    // Creates a sampling request to the MCP client
    // Returns AI-generated content based on prompts
}
```

**Features:**
- Checks client sampling capabilities before making requests
- Sends logging notifications to clients
- Returns AI-generated text content
- Used in `make-todo` tool to generate interesting facts

## 🏃 Running the Application

### Using Gradle

```bash
# Build the project
./gradlew clean build

# Run with STDIO profile (no web server)
./gradlew bootRun --args='--spring.profiles.active=stdio'

# Run with SSE profile
./gradlew bootRun --args='--spring.profiles.active=sse'

# Run with Streamable HTTP profile
./gradlew bootRun --args='--spring.profiles.active=streamable'

# Run with Stateless HTTP profile
./gradlew bootRun --args='--spring.profiles.active=stateless'
```

### Using Gradle Custom Tasks

```bash
# Development tasks defined in build.gradle
./gradlew devSse          # Run with SSE profile
./gradlew devStreamable   # Run with Streamable profile
./gradlew devStateless    # Run with Stateless profile
```

### Using Task (Taskfile)

If you have [Task](https://taskfile.dev/) installed:

```bash
# Build for STDIO
task build:stdio

# Run development servers
task dev:sse
task dev:streamable
task dev:stateless
```

## 🔧 Building for Production

### Build JAR file

```bash
./gradlew clean bootJar
```

The JAR file will be created at: `build/libs/todo-0.0.1-SNAPSHOT.jar`

### Run the JAR

```bash
# STDIO mode
java -Dspring.profiles.active=stdio -jar build/libs/todo-0.0.1-SNAPSHOT.jar

# SSE mode
java -Dspring.profiles.active=sse -jar build/libs/todo-0.0.1-SNAPSHOT.jar

# Streamable mode
java -Dspring.profiles.active=streamable -jar build/libs/todo-0.0.1-SNAPSHOT.jar

# Stateless mode
java -Dspring.profiles.active=stateless -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

## 🔌 MCP Client Configuration

### Claude Desktop / MCP Clients

Add to your MCP client configuration:

#### STDIO Server Configuration
```json
#### STDIO Server Configuration
```json
{
  "mcpServers": {
    "todo-mcp-server-stdio": {
      "type": "stdio",
      "command": "java",
      "args": [
        "-Dspring.profiles.active=stdio",
        "-jar",
        "/path/to/build/libs/todo-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

#### SSE Server Configuration
```json
{
  "mcpServers": {
    "todo-mcp-server-sse": {
      "url": "http://localhost:8080/sse",
      "type": "sse"
    }
  }
}
```

#### Streamable HTTP Server Configuration
```json
{
  "mcpServers": {
    "todo-mcp-server-streamable": {
      "url": "http://localhost:8080/mcp",
      "type": "http"
    }
  }
}
```

#### Stateless HTTP Server Configuration
```json
{
  "mcpServers": {
    "todo-mcp-server-stateless": {
      "url": "http://localhost:8080/mcp",
      "type": "http"
    }
  }
}
```

## 🧪 Testing the Server

### REST API Endpoints

The application includes REST endpoints for testing (when running web profiles):

```bash
# Health check
curl http://localhost:8080/api/health

# Test endpoint
curl http://localhost:8080/api/test

# Root endpoint
curl http://localhost:8080/api/
```

### H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

**Connection details:**
- JDBC URL: `jdbc:h2:mem:todo-db`
- Username: `sa`
- Password: `password`

### Testing with MCP Clients

Once configured in your MCP client (e.g., Claude Desktop):

1. **List todos**: "Show me all todos" (uses `fetch-all-todos` tool)
2. **Create todo**: "Create a todo to buy groceries" (uses `make-todo` tool with AI sampling)
3. **Update todo**: "Mark todo #1 as completed" (uses `change-todo` tool)
4. **Delete todo**: "Remove todo #2" (uses `remove-todo` tool)
5. **Use prompts**: Prompts are automatically available in the client

## 📁 Project Structure

```
mcp-internals/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── tools/muthuishere/todo/
│   │   │       ├── TodoApplication.java              # Main application
│   │   │       ├── todo/
│   │   │       │   ├── ApiTestController.java        # REST endpoints
│   │   │       │   ├── TodoRepository.java           # JPA repository
│   │   │       │   ├── TodoService.java              # Business logic
│   │   │       │   ├── model/
│   │   │       │   │   ├── Todo.java                 # Entity
│   │   │       │   │   └── TodoToolResponse.java     # Response DTO
│   │   │       │   ├── prompts/
│   │   │       │   │   └── TodoPrompts.java          # MCP prompts
│   │   │       │   └── tools/
│   │   │       │       └── TodoTools.java            # MCP tools
│   │   │       └── utils/
│   │   │           └── Sampling.java                 # AI sampling utility
│   │   └── resources/
│   │       ├── application.properties                # Main config
│   │       ├── application-stdio.properties          # STDIO config
│   │       ├── application-sse.properties            # SSE config
│   │       ├── application-streamable.properties     # Streamable config
│   │       └── application-stateless.properties      # Stateless config
│   └── test/
│       └── java/
│           └── tools/muthuishere/todo/
│               └── TodoApplicationTests.java
├── build.gradle                                      # Gradle build file
├── Taskfile.yaml                                     # Task runner config
└── README.md                                         # This file
```

## 🎯 Key Concepts

### Model Context Protocol (MCP)

MCP is a protocol that enables AI assistants to interact with external tools and data sources. This application demonstrates:

- **Tools**: Exposed functions that AI can call (`@McpTool`)
- **Prompts**: Pre-configured conversation starters (`@McpPrompt`)
- **Sampling**: AI-generated content integration (`McpSyncServerExchange`)
- **Transport Protocols**: Multiple ways to communicate (STDIO, SSE, HTTP)

### Spring AI Integration

The application uses Spring AI's MCP implementation:

- `spring-ai-starter-mcp-server`: STDIO server support
- `spring-ai-starter-mcp-server-webmvc`: WebMVC (SSE, HTTP) support
- `spring-ai-mcp-annotations`: Annotation-based configuration

### Multi-Protocol Support

The same application code works with different transport protocols by changing the active Spring profile. This demonstrates the flexibility of the MCP specification.

## 🚀 Advanced Features

### 1. AI-Enhanced Responses

The `make-todo` tool uses AI Sampling to generate interesting facts about todo items, demonstrating how to integrate AI capabilities into your MCP tools.

### 2. Logging Configuration

STDIO mode includes sophisticated logging configuration to prevent interference with the protocol communication:
- Console logging disabled for STDIO
- File-based logging to `~/mcp-server-stdio.log`
- Configurable log rotation

### 3. Multiple Transport Protocols

Demonstrates how a single codebase can support multiple MCP transport protocols through Spring profiles.

### 4. Health & Monitoring

Includes REST endpoints for health checks and testing, useful for monitoring HTTP-based servers.

## 📚 Resources

- [Model Context Protocol Specification](https://modelcontextprotocol.io/)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MCP Spring Boot Examples](https://github.com/spring-projects-experimental/spring-ai-mcp)