package com.example.todo.domain.model;

public enum Priority {
	// ゆうせ：高
	High(1),
	
	// 優先度：中
	Middle(2),
	
	// 優先度：低
	Low(3);

	private int id;

	private Priority(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
