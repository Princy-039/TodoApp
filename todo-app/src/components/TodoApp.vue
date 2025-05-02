<template>
    <div id="app">
      <div class="container">
        <h1 class="title">TODO LIST</h1>
        <hr>
        <div class="input-group">
          <input
            type="text"
            class="form-control"
            placeholder="Add item..."
            v-model="userInput"
            @keyup.enter="addItem"
          />
          <input
            type="text"
            class="form-control"
            placeholder="Search tasks..."
            v-model="searchInput"  
          />
  
          <input
            type="date"
            class="form-control"
            v-model="deadline"
            placeholder="Set deadline"
          />
          <button class="btn btn-success" @click="addItem">ADD</button>
        </div>
        <div>
          <button class="btn btn-primary" @click="fetchUpcomingTodos">Upcoming Todo</button>
          <button class="btn btn-success" @click="fetchCompletedTodos">Completed Todo</button>
        </div>
  
        <div class="todo-table">
          <div class="table-header">
            <div class="table-cell">Task</div>
            <div class="table-cell">Deadline</div>
            <div class="table-cell">Actions</div>
          </div>
  
          <div class="table-body">
            <div
              class="table-row"
              v-for="(item, index) in filteredList"
              :key="index"
              :class="{ 'even-row': index % 2 === 0, completed: item.completed }"
            >
              <div class="table-cell" :class="{ 'completed-cell': item.completed }">
                {{ item.description }}
              </div>
              <div class="table-cell">{{formatDate(item.deadline) }}</div>
              <div class="table-cell">
                <button class="btn btn-primary" @click="toggleCompleted(index)">
                  {{ item.completed ? 'Undo' : 'Complete' }}
                </button>
                <button
                  class="btn btn-info"
                  v-if="!item.completed"
                  @click="editItem(index)"
                >
                  Edit
                </button>
                <button class="btn btn-danger" @click="deleteItem(item.id)">
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'App',
    data() {
      return {
        userInput: '',
        deadline: '',
        searchInput: '',
        list: [],
        isCompletedFilter: false,  // Add this if you need it
      };
    },
    computed: {
      filteredList() {
        return this.list.filter((item) => {
          if (!item || !item.description) return false;
          return item.description
            .toLowerCase()
            .includes(this.searchInput.toLowerCase());
        });
      },
    },
    mounted() {
    this.fetchTodos();  // Call fetchTodos when the page loads
  },
    methods: {
      async fetchTodos() {
        const res = await fetch('http://localhost:8083/getalltodo');
        this.list = await res.json();
      },
  
      formatDate(dateString) {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) {
            // Return an empty string or a fallback value if the date is invalid
            return '';
        }
        return date.toISOString().split('T')[0];
      },


      async addItem() {
        const currentDate = new Date().toISOString().split('T')[0];
        if (this.userInput.trim() !== '' && this.deadline && this.deadline >= currentDate) {
          const newItem = {
            description: this.userInput.trim(),
            completed: false,
            deadline: this.deadline,
          };
  
          const res = await fetch('http://localhost:8083/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newItem),
          });
  
          const created = await res.json();
          this.list.push(created);
          this.userInput = '';
          this.deadline = '';
        } else {
          alert('The deadline must be today or in the future.');
        }
      },
  

      async deleteItem(id) {
        const todoIndex = this.list.findIndex(todo => todo.id === id);  
        if (todoIndex !== -1) {
          try {
            const response = await fetch(`http://localhost:8083/delete/${id}`, {
              method: 'DELETE',
            });
  
            if (response.ok) {
              this.list.splice(todoIndex, 1);
            } else {
              console.error("Failed to delete item from server");
            }
            } catch (error) {
            console.error("Error while deleting item:", error);
                }
            } else {
            console.error("Todo item not found");
            }
        },
  
      async fetchUpcomingTodos() {
        const currentDate = new Date().toISOString().split('T')[0];
        const res = await fetch(`http://localhost:8083/upcomingtodo`);
        this.list = await res.json();
      },
  
      async fetchCompletedTodos() {
        console.log('fetchCompletedTodos is being called'); // Debug log

        this.isCompletedFilter = true;
        const res = await fetch('http://localhost:8083/completedtodo');
        this.list = await res.json();
      },
  
      async editItem(index) {
        const item = this.list[index];
        if (!item.id) {
          console.error('Item id is missing!', item);
          return;
        }
  
        const editedDescription = prompt('Edit the todo:', item.description);
        const editedDeadline = prompt('Edit deadline (YYYY-MM-DD):', item.deadline);
  
        if (editedDescription !== null && editedDescription.trim() !== '' && editedDeadline !== null) {
          const currentDate = new Date().toISOString().split('T')[0];
          if (editedDeadline >= currentDate) {
            const updatedItem = {
              ...item,
              description: editedDescription.trim(),
              deadline: editedDeadline,
            };
  
            try {
              const res = await fetch(`http://localhost:8083/update/${updatedItem.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedItem),
              });
  
              if (res.ok) {
                const result = await res.json();
                this.list[index] = result;
              } else {
                alert('Failed to update the todo item');
              }
            } catch (error) {
              console.error('Error during update:', error);
              alert('An error occurred while updating the todo.');
            }
          } else {
            alert('The deadline must be today or in the future.');
          }
        }
      },
  
      async toggleCompleted(index) {
        const item = this.list[index];
        const updatedItem = {
          ...item,
          completed: !item.completed,
        };
  
        const res = await fetch(`http://localhost:8083/mark/${item.id}`, {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ completed: updatedItem.completed }),
        });
  
        if (res.ok) {
          this.list[index].completed = updatedItem.completed;
        } else {
          alert('Error updating task status');
        }
      },
    },
  };
  </script>
  
  
  <style src="../style.css"></style>

  