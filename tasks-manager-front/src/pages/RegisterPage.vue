<script setup lang="ts">
import RegisterHeader from '@/components/register/RegisterHeader.vue';
</script>



<template>
  <div>
    <RegisterHeader/>
  </div>
</template>

<style scoped>
.register-page {
  min-height: 100vh;
  background-color: #29293f;
  display: flex;
  flex-direction: column;
}

.register-page-content {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
