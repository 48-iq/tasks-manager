<script setup lang="ts">
defineProps<{
  name: string
  description: string
}>()
</script>
<template>
  <div class="section">
    {{ name }}
  </div>
</template>

<style scoped>
.section {
  padding: 10px;
  cursor: pointer;
  border-radius: 5px;
  background-color: #989edf;
  transition: background-color 0.2s ease-in-out;
}
.section:hover {
  background-color: #cfd2ea;
}
</style>
