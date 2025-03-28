import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home.vue'
import PuzzlePrint from '@/views/PuzzlePrint.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/puzzle-print/:puzzleType/:puzzleId',
      name: 'puzzlePrint',
      component: PuzzlePrint,
      props: true,
    },
  ],
})

export default router
