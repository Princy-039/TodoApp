import { mount } from '@vue/test-utils';
import TodoApp from '@/components/TodoApp.vue';
import { vi } from 'vitest';

describe('TodoApp.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(TodoApp);
  });

  it('should fetch all todos', async () => {
    const mockTodos = [
      { id: 1, description: 'Test Task 1', deadline: '2025-04-28', completed: false },
      { id: 2, description: 'Test Task 2', deadline: '2025-04-30', completed: false },
    ];

    // Mock the API call to fetch todos
    vi.spyOn(global, 'fetch').mockResolvedValueOnce({
      json: () => Promise.resolve(mockTodos),
    });

    await wrapper.vm.fetchTodos();

    expect(wrapper.vm.list).toEqual(mockTodos);  // Check if the todos are set correctly
    expect(global.fetch).toHaveBeenCalledWith('http://localhost:8083/getalltodo');  // Check if the correct API was called
  });
});



describe('App.vue', () => {
  let wrapper;

  // Before each test, mount the component
  beforeEach(() => {
    wrapper = mount(TodoApp);
  });

  it('should add a new todo', async () => {
    const newTodo = {
      description: 'New Task',
      completed: false,
      deadline: '2025-05-01',
    };

    // Mock the API response for adding a todo
    vi.spyOn(global, 'fetch').mockResolvedValueOnce({
      json: () => Promise.resolve({ ...newTodo, id: 3 }),  // Mocked response with an ID
    });

    // Directly set the data of the component
    wrapper.vm.userInput = newTodo.description;
    wrapper.vm.deadline = newTodo.deadline;

    // Call the method to add the item
    await wrapper.vm.addItem();

    // Check if the new item was added to the list
    expect(wrapper.vm.list).toContainEqual({ ...newTodo, id: 3 });

    // Ensure the correct API call was made
    expect(global.fetch).toHaveBeenCalledWith('http://localhost:8083/add', expect.objectContaining({
      method: 'POST',
      body: JSON.stringify(newTodo),
    }));
  });
});



  it('should delete a todo item', async () => {
    const todoId = 1;
    const wrapper = mount(TodoApp); 
  
    // Mock the delete API call
    vi.spyOn(global, 'fetch').mockResolvedValueOnce({
      ok: true,
    });
  
    // Initially set a list with the todo
    await wrapper.setData({
      list: [{ id: 1, description: 'Task to be deleted', completed: false, deadline: '2025-04-28' }]
    });
  
    await wrapper.vm.deleteItem(todoId);
  
    // Check if the item was removed from the list
    expect(wrapper.vm.list).not.toContainEqual(expect.objectContaining({ id: todoId }));
    expect(global.fetch).toHaveBeenCalledWith(`http://localhost:8083/delete/${todoId}`, expect.objectContaining({
      method: 'DELETE',
    }));
  });

  
  it('should fetch upcoming todos', async () => {
    const mockUpcomingTodos = [
      { id: 2, description: 'Upcoming Task 1', deadline: '2025-05-10', completed: false },
    ];
    const wrapper = mount(TodoApp); 

  
    vi.spyOn(global, 'fetch').mockResolvedValueOnce({
      json: () => Promise.resolve(mockUpcomingTodos),
    });
  
    await wrapper.vm.fetchUpcomingTodos();
  
    expect(wrapper.vm.list).toEqual(mockUpcomingTodos);
    expect(global.fetch).toHaveBeenCalledWith('http://localhost:8083/upcomingtodo');
  });

  
  it('should fetch completed todos', async () => {
    const mockCompletedTodos = [
      { id: 3, description: 'Completed Task 1', deadline: '2025-04-28', completed: true },
    ];
    const wrapper = mount(TodoApp); 

  
    vi.spyOn(global, 'fetch').mockResolvedValueOnce({
      json: () => Promise.resolve(mockCompletedTodos),
    });
  
    await wrapper.vm.fetchCompletedTodos();
  
    expect(wrapper.vm.list).toEqual(mockCompletedTodos);
    expect(global.fetch).toHaveBeenCalledWith('http://localhost:8083/completedtodo');
  });

  
  it('should edit a todo item', async () => {
    let wrapper = mount(TodoApp);
  
    const initialTodo = {
      id: 1,
      description: 'Test Task',
      completed: false,
      deadline: '2025-04-28'
    };
  
    await wrapper.setData({ list: [initialTodo] });
  
    // Mock fetch response for update
    vi.spyOn(global, 'fetch').mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ ...initialTodo, description: 'Updated Task Description', deadline: '2025-05-05' })
    });
  
    // Mock prompt responses
    vi.spyOn(window, 'prompt')
      .mockImplementationOnce(() => 'Updated Task Description')  // for description
      .mockImplementationOnce(() => '2025-05-05');               // for deadline
  
    await wrapper.vm.editItem(0);  // Editing the first todo
  
    const updatedTodo = {
      id: 1,
      description: 'Updated Task Description',
      completed: false,
      deadline: '2025-05-05'
    };
  
    expect(wrapper.vm.list[0]).toEqual(updatedTodo);
    expect(global.fetch).toHaveBeenCalledWith(`http://localhost:8083/update/${updatedTodo.id}`, expect.objectContaining({
      method: 'PUT',
    }));
  });
  

  
  it('should toggle the completion status of a todo', async () => {
    const todoId = 1;
    const wrapper = mount(TodoApp); 
    const updatedTodo = {
      id: todoId,
      description: 'Task to be toggled',
      deadline: '2025-04-28',
      completed: true,
    };
  
    vi.spyOn(global, 'fetch').mockResolvedValueOnce({
      ok: true,
    });
  
    const mockTodo = { id: todoId, description: 'Task to be toggled', deadline: '2025-04-28', completed: false };
  
    await wrapper.setData({ list: [mockTodo] });
    await wrapper.vm.toggleCompleted(0);
  
    expect(wrapper.vm.list[0].completed).toBe(true);
    expect(global.fetch).toHaveBeenCalledWith(`http://localhost:8083/mark/${todoId}`, expect.objectContaining({
      method: 'PATCH',
      body: JSON.stringify({ completed: true }),
    }));
  });
  
  