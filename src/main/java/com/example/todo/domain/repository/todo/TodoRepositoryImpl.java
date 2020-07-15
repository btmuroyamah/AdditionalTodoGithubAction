package com.example.todo.domain.repository.todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.todo.domain.model.Todo;

@Repository
public class TodoRepositoryImpl implements TodoRepository {
	private static final Map<String, Todo> TODO_MAP = new ConcurrentHashMap<String, Todo>();
	
	@Override
	public Optional<Todo> findById(String todoId) {
		return Optional.ofNullable(TODO_MAP.get(todoId));
	}

	@Override
	public Collection<Todo> findAll() {
		return TODO_MAP.values();
	}
	
	@Override
	public Collection<Todo> findByLimit(LocalDate start, LocalDate end) {
		Collection<Todo> todos = new ArrayList<Todo>();
		
		//startとendがともに無ければ全件取得
		if(start == null && end == null) {
			for(Todo todo: TODO_MAP.values()) {
				if(todo.getDeadLine() == null) {
					continue;
				}
				todos.add(todo);
			}
			
			//startのみの場合
		}else if(start != null && end == null) {
			for(Todo todo: TODO_MAP.values()) {
				if(todo.getDeadLine() == null) {
					continue;
				}
				if(todo.getDeadLine().isAfter(start) || todo.getDeadLine().isEqual(start)) {
					todos.add(todo);
				}
			}
			
			//endのみの場合
		}else if(start == null && end != null) {
			for(Todo todo: TODO_MAP.values()) {
				if(todo.getDeadLine() == null) {
					continue;
				}
				if(todo.getDeadLine().isBefore(end) || todo.getDeadLine().isEqual(end)) {
					todos.add(todo);
				}
			}
			
			//startとendともにある場合
		}else {
			for(Todo todo: TODO_MAP.values()) {
				if(todo.getDeadLine() == null) {
					continue;
				}
				if((todo.getDeadLine().isAfter(start) || todo.getDeadLine().isEqual(start)) 
						&& (todo.getDeadLine().isBefore(end) || todo.getDeadLine().isEqual(end))) {
					todos.add(todo);
				}
			}
		}
		
		return todos;
	}

	@Override
	public void create(Todo todo) {
		TODO_MAP.put(todo.getTodoId(), todo);
	}

	@Override
	public boolean updateById(Todo todo) {
		TODO_MAP.put(todo.getTodoId(), todo);
		return true;
	}

	@Override
	public void deleteById(Todo todo) {
		TODO_MAP.remove(todo.getTodoId());
	}

	@Override
	public long countByFinished(boolean finished) {
		long count = 0;
        for (Todo todo : TODO_MAP.values()) {
            if (finished == todo.isFinished()) {
                count++;
            }
        }
        return count;
    }
}

