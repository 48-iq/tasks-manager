<template>
  <form class="login-form">
    <span>Вход</span>
    <BaseInput />
    <BaseInput type="password" />
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
