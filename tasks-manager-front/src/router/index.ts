import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/sign-in',
      name: 'home',
      component: () => import('../pages/LoginPage.vue')
    },
    {
      path: '/app',
      name: 'app',
      component: () => import('../views/BaseView.vue')
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: () => import('../pages/RegisterPage.vue')
    }
  ],
})

export default router
