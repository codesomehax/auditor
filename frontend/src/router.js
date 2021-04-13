import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      component: () => import('@/views/Index'),
      children: [
        {
          name: 'Curriculums',
          path: '/',
          component: () => import('@/views/Curriculum/List'),
        },
        {
          name: 'Curriculum',
          path: '/curriculum/:id',
          component: () => import('@/views/Curriculum/Index'),
        },
        {
          name: 'Curriculum-edit',
          path: '/curriculum/edit/:id',
          props: route => ({ action: route.query.action }),
          component: () => import('@/views/Curriculum/Edit'),
        },
        {
          name: 'Curriculum-create',
          path: '/curriculum/create',
          props: route => ({ action: route.query.action }),
          component: () => import('@/views/Curriculum/Edit'),
        },
        {
          name: 'Students',
          path: '/students-list',
          component: () => import('@/views/Student/List'),
        },
        {
          name: 'Student Audit',
          path: '/student/:id',
          component: () => import('@/views/Student/Audit'),
        },
        {
          name: 'Compare Students',
          path: '/students-comparison/',
          props: route => ({ id: route.query.id }),
          component: () => import('@/views/Student/Comparison'),
        },
        {
          name: 'Template mails',
          path: '/mails-list',
          component: () => import('@/views/Mail/List'),
        },
        {
          name: 'Mail',
          path: '/modify-mail',
          props: route => ({ action: route.query.action, id: route.query.id }),
          component: () => import('@/views/Mail/Modify')
        }
      ],
    },
  ],
})
