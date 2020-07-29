package com.example.todo.domain.repository.todo;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.todo.domain.model.Todo;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

	protected static final Map<String, Todo> TODO_MAP = new ConcurrentHashMap<String, Todo>();

	@Override
	public Optional<Todo> findById(String todoId) {
		return Optional.ofNullable(TODO_MAP.get(todoId));
	}

	@Override
	public List<Todo> findAll() {
		// Collection型のTODO_MAP.valuesを拡張for文でListにつめる
		List<Todo> todos = new ArrayList<>();
		for (Todo todo : TODO_MAP.values()) {
			todos.add(todo);
		}
		return todos;
	}

	@Override
	public List<Todo> findByLimit(LocalDate start, LocalDate end) {
		List<Todo> todos = new ArrayList<Todo>();

		for (Todo todo : TODO_MAP.values()) {
			
			// startとendがともに無ければTodoを全件取得

			if (start == null && end == null) {
				todos.add(todo);

				// startとendともにある場合
			} else if (!(todo.getDeadLine() == null) && isAfterStart(todo, start) && isBeforeEnd(todo, end)) {
				todos.add(todo);

				// endのみの場合
			} else if (!(todo.getDeadLine() == null) && start == null && isBeforeEnd(todo, end)) {
				todos.add(todo);

				// startのみの場合
			} else if (!(todo.getDeadLine() == null) && end == null && isAfterStart(todo, start)) {
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
		if (start == null) {
			return false;
		}
		return target.getDeadLine().isAfter(start) || target.getDeadLine().isEqual(start);
	}

	private boolean isBeforeEnd(Todo target, LocalDate end) {
		if (end == null) {
			return false;
		}
		return target.getDeadLine().isBefore(end) || target.getDeadLine().isEqual(end);
	}
}
