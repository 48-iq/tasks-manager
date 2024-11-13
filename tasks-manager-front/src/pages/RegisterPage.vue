<script setup lang="ts">
import MessageHeader from '@/components/base/MessageHeader.vue'
import RegisterForm from '@/components/register/RegisterForm.vue'
</script>

<template>
  <div class="register-page">
    <MessageHeader> Станьте частью Tasks Manager</MessageHeader>
    <div class="register-page-content">
      <RegisterForm />
    </div>
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
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
