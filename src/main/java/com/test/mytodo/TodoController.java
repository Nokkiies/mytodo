package com.test.mytodo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

@RestController
public class TodoController {

	private List<Todo> todos = new ArrayList<>();
	private final AtomicLong counter = new AtomicLong();

	public TodoController() {

	}

	// GET .../todo
	@GetMapping("/todo")
	public List<Todo> getTodos1() {
		return todos;
	}

	// GET .../todo/id
	@GetMapping("/todo/{id}")
	public Todo getTodos2(@PathVariable() long id) {
		return todos.stream().filter(result -> result.getId() == id).findFirst()
				.orElseThrow(() -> new TodoNotFoundException(id)
//		new TodoUnAuthenException()
				);
	}

	// GET .../todo?name=hello
	@GetMapping("/todo/search")
	public String getTodoByName(@RequestParam(defaultValue = "nok") String name) {
		return "search: " + name;
	}

	// POST .../todo
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/todo")
	public void addTodo(@RequestBody Todo todo) {
		todos.add(new Todo(counter.getAndIncrement(), todo.getName()));
	}

	// PUT .../todo/1234
	@PutMapping("/todo/{id}")
	public void editTodo(@RequestBody Todo todo, @PathVariable long id) {
		todos.stream().filter(result -> result.getId() == id).findFirst().ifPresentOrElse(result -> {
			result.setName(todo.getName());
		}, () -> {
			throw new TodoNotFoundException(id);
//			throw new TodoUnAuthenException();
		});
	}

	// Delete .../todo/1234
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/todo/{id}")
	public void deleteTodo(@PathVariable long id) {
		todos.stream().filter(result -> result.getId() == id).findFirst().ifPresentOrElse(result -> {
			todos.remove(result);
		}, () -> {
			throw new TodoNotFoundException(id);
//			throw new TodoUnAuthenException();
		});
	}
}
