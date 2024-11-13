<script setup lang="ts">
export type SelectOption = {
  name: string,
  value: string
}

defineProps<{
  options: SelectOption[],
  disabled: string|null
}>()

const model = defineModel()

</script>
<template>
<select v-model="model" class="base-select">
  <option v-if="disabled!==null" disabled :value="disabled" class="base-select-option"> {{ disabled }} </option>
  <option v-for="option in options" :key="option.value" :value="option.value" class="base-select-option"> 
    {{ option.name }}
  </option>
</select>
</template>

<style scoped>
  .base-select {
    font-size: 18px;
    background-color: #cfd2ea;
    padding: 5px;
    border-radius: 5px;
  } 
</style>