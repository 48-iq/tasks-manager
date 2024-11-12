<script setup lang="ts">
import LoginForm from '@/components/login/LoginForm.vue'
import MessageHeader from '@/components/base/MessageHeader.vue'
</script>

<template>
  <div class="login-page">
    <MessageHeader> Добро пожаловать в Tasks Manager </MessageHeader>
    <div class="login-page-content">
      <LoginForm />
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  background-color: #29293f;
  display: flex;
  flex-direction: column;
}

.login-page-content {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
