<script setup lang="ts">
import NavigationSidebarSection from './NavigationSidebarSection.vue';

const sections = [
  {
    name: 'Команды',
    description:
      'Выберите команду для просмотра задач, если вы не состоите в команде, то присоединитесь к существующей или создайте свою',
  },
  {
    name: 'Профиль',
    description: 'Просмотрите и измените свой профиль',
  },
  {
    name: 'Форум',
    description: 'Обсудите задачи с другими участниками, получите ответы о функиях площадки',
  },
]
</script>

<template>
  <div class="sidebar">
    <NavigationSidebarSection v-for="section in sections"
    :name="section.name"
    :description="section.description"
    :key="section.name"/>
  </div>
</template>

<style scoped>
.sidebar {
  background-color: #5d5f88;
  width: 210px;
  height: 100vh;
  border-radius: 5px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 5px;
}
</style>
