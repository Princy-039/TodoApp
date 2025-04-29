<template>
  <div id="app">
    <TodoApp />
  </div>
</template>

<script>
import TodoApp from './components/TodoApp.vue';  // Path to your TodoApp component

export default {
  name: 'App',
  components: {
    TodoApp
  }
}
</script>
