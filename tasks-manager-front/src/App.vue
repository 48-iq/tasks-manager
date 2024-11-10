<script setup lang="ts">
import { getCookie } from 'typescript-cookie';
import { onMounted } from 'vue';
import { useAuthStore } from './stores/authStore';

const authStore = useAuthStore()

onMounted(() => {
  const refresh = getCookie('refresh')
  if (refresh)
    authStore.refreshTokens(refresh)
})
</script>

<template>
  <RouterView />
</template>

<style scoped>
</style>
