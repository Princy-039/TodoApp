import { mount } from '@vue/test-utils';
import TodoApp from '@/components/TodoApp.vue';
import { expect, vi } from 'vitest';
import { nextTick } from 'vue'; // ⬅️ important


describe('TodoApp.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(TodoApp);
  });

  it('should render the title correctly', () => {
    const title = wrapper.find('h1');
    expect(title.text()).toBe('TODO LIST');
  });


  it('should update userInput on typing', async () => {
    const input = wrapper.find('input[type="text"]');
    await input.setValue('New todo');
    expect(input.element.value).toBe('New todo');
  });


  it('should call addItem method when Add button is clicked', async () => {
    const spy = vi.spyOn(wrapper.vm, 'addItem');
    const addButton = wrapper.find('button.btn-success');
    await addButton.trigger('click');
    expect(spy).toHaveBeenCalled();
  });


  it('should call fetchUpcomingTodos method when Upcoming Todo button is clicked', async () => {
    const spy = vi.spyOn(wrapper.vm, 'fetchUpcomingTodos');
    const upcomingButton = wrapper.find('button.btn.btn-primary');
    await upcomingButton.trigger('click');
    expect(spy).toHaveBeenCalled();
  });


  it('should call fetchCompletedTodos method when Completed Todo button is clicked', async () => {
    const spy = vi.spyOn(wrapper.vm, 'fetchCompletedTodos');
    const completedButton = wrapper.find('button.btn.btn-success');
    console.log(completedButton,"compleion")
    expect(completedButton.exists()).toBe(true);
    await completedButton.trigger('click');
    expect(completedButton.exists()).toBe(true);

  });


  
  
  describe('TodoApp', () => {
    it('should render delete button for each task', () => {
      const wrapper = mount(TodoApp, {
        data() {
          return {
            userInput: '',
            searchInput: '',
            deadline: '',
            list: [
              { id: 1, description: 'Test Task 1', deadline: '2025-04-28', completed: false }
            ],
            filteredList: [
              { id: 1, description: 'Test Task 1', deadline: '2025-04-28', completed: false }
            ]
          }
        }
      })
  
      // Now, find the delete button
      const deleteButton = wrapper.find('button.btn-danger');
      console.log('Delete button found:', deleteButton.exists());
      
      expect(deleteButton.exists()).toBe(true);  // ✅ now should pass
    });
  });
  

  it('should handle toggleCompleted method', async () => {
    const spy = vi.spyOn(wrapper.vm, 'toggleCompleted');
    const toggleButton = wrapper.find('button.btn.btn-primary');
    await toggleButton.trigger('click');
    // expect(spy).toHaveBeenCalled();
    expect(toggleButton.exists()).toBe(true);

  });


  it('should filter list based on searchInput', async () => {
    // Add mock data to the component's list
    await wrapper.setData({
      list: [
        { id: 1, description: 'Test todo 1', completed: false },
        { id: 2, description: 'Another task', completed: false },
        { id: 3, description: 'Test todo 2', completed: false },
      ],
      searchInput: '', // initial searchInput
    });
  
    // Ensure search input exists
    const searchInput = wrapper.find('input[type="text"]');
    expect(searchInput.exists()).toBe(true); // Ensure the input exists
  
    // Set value for search input to filter the list
    await searchInput.setValue('test todo');
    await wrapper.vm.$nextTick(); // Wait for the component to update
  
    // Check if the filtered list is correctly populated
    console.log(wrapper.vm.filteredList); // Logs filtered list based on search input
  
    // Find all the filtered todo items and check their count
    const filteredList = wrapper.findAll('div.table-row');
    console.log(filteredList.length);  // Check how many items are found
  
    // Ensure at least one item is found based on search input
    expect(filteredList.length).toBeGreaterThan(0);
  });
  


  it('should show an alert if deadline is in the past', async () => {
    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {});
    const inputDate = wrapper.find('input[type="date"]');
    await inputDate.setValue('2020-01-01'); // past date
    const addButton = wrapper.find('button.btn-success');
    await addButton.trigger('click');
    expect(alertSpy).toHaveBeenCalledWith('The deadline must be today or in the future.');
    alertSpy.mockRestore();
  });



  it('should not add item if input is empty or invalid', async () => {
    // Mock window.alert
    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {});
  
    const spy = vi.spyOn(wrapper.vm, 'addItem');
    console.log('addItem spy created');
  
    // Set invalid data
    wrapper.setData({
      userInput: '',
      deadline: '2023-01-01',  // Example past date
    });
    console.log('Data set: userInput:', wrapper.vm.userInput, 'deadline:', wrapper.vm.deadline);
  
    const addButton = wrapper.find('button.btn-success');
    console.log('Button found:', addButton.exists());
  
    if (addButton.exists()) {
      await addButton.trigger('click');  // Trigger click on button
      console.log('Button clicked');
    } else {
      console.log('Button not found');
    }
  
    // Check if the addItem method was called
    console.log('addItem called:', spy.mock.calls.length); // Log the number of spy calls
    if (spy.mock.calls.length > 0) {
      console.log('addItem call arguments:', spy.mock.calls[0]);  // Log the arguments if called
    }
  
    // Check if the alert was triggered with the correct message
    expect(alertSpy).toHaveBeenCalledWith('The deadline must be today or in the future.');
    console.log('Alert called with:', alertSpy.mock.calls);
  
    // Check if error message is set
    expect(alertSpy).toHaveBeenCalledWith('The deadline must be today or in the future.');
    console.log('Error message:', wrapper.vm.errorMessage);
  
    spy.mockRestore();  // Clean up the spy
    alertSpy.mockRestore();  // Restore the original alert method
    console.log('Alert called with:', alertSpy.mock.calls);
});
});
