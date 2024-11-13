<script setup lang="ts">
import { reactive, watch } from 'vue';
import BaseInput from '../base/BaseInput.vue'
import BaseSelect from '../base/BaseSelect.vue';

  const roles = [
    {
      name: "администратор",
      value: "ROLE_ADMIN"
    },
    {
      name: "пользователь",
      value: "ROLE_USER"
    }
  ]

  export type RegisterFormData = {
    login: string
    password: string
    confirmPassword: string
    role: string
    adminPassword: string
  }

  const formData: RegisterFormData = reactive(
    {
      login: "",
      password: "",
      confirmPassword: "",
      role: "",
      adminPassword: ""
    }
  )
  

  watch(formData, async() => {
    console.log(formData)
  })

</script>

<template>
  <form class="register-form">
    <span>Регистрация</span>
    <BaseInput class="register-form-input" placeholder="Логин" v-model="formData.login"/>
    <BaseInput type="password" class="register-form-input" placeholder="Пароль" v-model="formData.password"/>
    <BaseInput type="password" class="register-form-input" placeholder="Повторите пароль" v-model="formData.confirmPassword"/>
    <BaseSelect :options="roles" v-model="formData.role" disabled="выберите роль" class="role-select"/>
    <BaseInput class="register-form-input" placeholder="пароль администратора"/>
    
  </form>
</template>

<style scoped>
.register-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  background-color: #989edf;
  border-radius: 5px;
  padding: 30px;
  width: 80%;
  font-size: 30px;
}

.register-form-input {
  width: 400px;
}

.role-select {
  width: 400px;
}
</style>
