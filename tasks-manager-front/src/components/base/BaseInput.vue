<script setup lang="ts">
  const model = defineModel()

</script>
<template>
  <input class="base-input" v-model="model"/>
</template>

<style>
  .base-input {
    background-color: #cfd2ea;
    border-radius: 5px;
    border: 2px solid #5d5f88;
    padding: 5px;
    font-size: 18px;
  }
</style>
