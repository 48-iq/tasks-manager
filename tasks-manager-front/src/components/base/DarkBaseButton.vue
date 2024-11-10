<template>
  <button class="base-button"> <slot/> </button>
</template>

<style scoped>
.base-button {
  padding: 10px;
  cursor: pointer;
  border-radius: 5px;
  background-color: #29293f;
  border: 2px solid #29293f;
  transition: background-color 0.2s ease-in-out;
  color: white;
  width: 100%;
}
.base-button:hover {
  background-color: #5d5f88;
}
</style>
