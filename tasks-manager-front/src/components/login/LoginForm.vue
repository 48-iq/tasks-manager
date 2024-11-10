<script setup lang="ts">
import BaseButton from '../base/DarkBaseButton.vue'
import BaseInput from '../base/BaseInput.vue'
import { useAuthStore } from '../../stores/authStore'
import { ref } from 'vue'

const authStore = useAuthStore()

const username = ref('')
const password = ref('')

const signIn = () => {
  authStore.signIn(username.value, password.value)
}

if (authStore.user)
  console.log('user', authStore.user)

authStore.$subscribe((mutation, state) => {
  if (state.user)
    console.log('user', state.user)
})


const toRegister = () => {}
</script>

<template>
  <form class="login-form">
    <span>Вход</span>
    <BaseInput v-model="username" />
    <BaseInput type="password" v-model="password" />
    <BaseButton width="100%" @click.prevent="signIn">Войти</BaseButton>
    <BaseButton width="100%" @click.prevent="toRegister">Зарегистрироваться</BaseButton>
  </form>
</template>

<style scoped>
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  background-color: #989edf;
  border-radius: 5px;
  padding: 8px;
}
</style>
