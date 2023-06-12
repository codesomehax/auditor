import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  //  mode: 'hash',
  mode: 'history',
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
          name: 'Compare Students Performance',
          path: '/students-performance-comparison/',
          props: route => ({ ids: route.query.ids, curriculum: route.query.curriculum }),
          component: () => import('@/views/Student/PerformanceComparison'),
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
        },
        {
          name: 'Manuals list',
          path: 'manuals-list',
          component: () => import('@/views/manuals/List')
        },
        {
          name: 'Curriculum manual',
          path: 'manual-curriculum',
          component: () => import('@/views/manuals/Curriculum')
        },
        {
          name: 'Student manual',
          path: 'manual-student',
          component: () => import('@/views/manuals/Student')
        },
        {
          name: 'Login',
          path: 'login',
          component: () => import('@/views/Login/login')
        },
        {
          name: 'Permission denied',
          path: 'permission-denied',
          component: () => import('@/views/PermissionDenied/PermissionDenied')
        }
      ],
    },
  ],
})

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/manuals-list', '/manual-curriculum', '/manual-student', '/permission-denied']
  const rolesRequiredPages = {
    "CURRICULUM": ['/', '/curriculum', '/curriculum/edit', '/curriculum/create'],
    "STUDENT": ['/students-list', '/student', '/students-comparison', '/students-performance-comparison'],
    "TEMPLATE_MAIL": ['/mails-list', '/modify-mail'],
  }
  //public pages
  const authNotRequired = publicPages.includes(to.path)
  if (authNotRequired) {
    return next()
  }

  const user = JSON.parse(localStorage.getItem('user'))

  //logged in pages
  if (!authNotRequired && !user) {
    return next('/login')
  }
  //pages with roles
  for (let [roleName, pages] of Object.entries(rolesRequiredPages)) {
    if (pages.includes(to.path) && user.includes(roleName)) {
      return next()
    }
  }
  //user does not have the required role
  return next('permission-denied')
  // next(false)

})

export default router