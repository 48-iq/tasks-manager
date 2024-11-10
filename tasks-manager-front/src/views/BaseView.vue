<script setup lang="ts">
import SideBar from '@/components/navigation/NavigationSidebar.vue'
</script>

<template>
  <div class="base">
    <SideBar />
    <router-view />
  </div>
</template>

<style scoped>
.base {
  background-color: #29293f;
}
</style>
