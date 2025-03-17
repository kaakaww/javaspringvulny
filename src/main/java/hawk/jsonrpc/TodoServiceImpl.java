package hawk.jsonrpc;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import hawk.entity.Todo;
import hawk.repos.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AutoJsonRpcServiceImpl
public class TodoServiceImpl implements TodoService {

    @Autowired
    TodoRepo todoRepo;

    @Override
    public Todo addTodo(String title) {
        Todo todo = new Todo(title, false);
        todo = todoRepo.save(todo);
        return todo;
    }

    @Override
    public List<Todo> listTodos() {
        List<Todo> ret = new ArrayList<>();
        todoRepo.findAll().iterator().forEachRemaining(ret::add);
        return ret;
    }

    @Override
    public Todo getTodoById(Long id) {
        return todoRepo.findById(id).orElse(null);
    }

    @Override
    public Todo updateTodo(Long id, String title, boolean completed) {
        Todo todo = getTodoById(id);
        if (todo != null) {
            todo.setTitle(title);
            todo.setCompleted(completed);
            todo = todoRepo.save(todo);
        }
        return todo;
    }

    @Override
    public boolean deleteTodoById(Long id) {
        try {
            todoRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteAll() {
        todoRepo.deleteAll();
    }

}
