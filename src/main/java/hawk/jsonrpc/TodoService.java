package hawk.jsonrpc;

import com.googlecode.jsonrpc4j.JsonRpcService;
import hawk.entity.Todo;

import java.util.List;

@JsonRpcService("/api/token/jsonrpc")
public interface TodoService {
    Todo addTodo(String title);
    List<Todo> listTodos();
    Todo getTodoById(Long id);
    Todo updateTodo(Long id, String title, boolean completed);
    boolean deleteTodoById(Long id);
    void deleteAll();
}
