package com.example.todo.domain.repository.todo;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.LocalDate;
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
		
		for(Todo todo: TODO_MAP.values()) {
			if(todo.getDeadLine() == null) {
				continue;
			}
			
			//startとendがともに無ければ期限付きのTodoを全件取得
			if(start == null && end == null) {
				todos.add(todo);
				
				//startとendともにある場合
			}else if(isAfterStart(todo, start) && isBeforeEnd(todo, end)) {
				todos.add(todo);
				
				//endのみの場合
			}else if(start == null && isBeforeEnd(todo, end)) {
				todos.add(todo);
				
				//startのみの場合
			}else if(end == null && isAfterStart(todo, start)) {
				todos.add(todo);
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
	
	private boolean isAfterStart(Todo target, LocalDate start) {
		if(start == null) {
			return false;
		}
		return target.getDeadLine().isAfter(start) || target.getDeadLine().isEqual(start);
	}
	
	private boolean isBeforeEnd(Todo target, LocalDate end) {
		if(end == null) {
			return false;
		}
		return target.getDeadLine().isBefore(end) || target. getDeadLine().isEqual(end);
	}
}

