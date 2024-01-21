import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '用户信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/PersonInfo') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'userList', name: 'UserList', meta: { name: '用户列表' }, component: () => import('../views/manager/User') },
      { path: 'textbookList', name: 'TextbookList', meta: { name: '教材列表' }, component: () => import('../views/manager/Textbook') },
      { path: 'applicationList', name: 'ApplicationList', meta: { name: '申请列表' }, component: () => import('../views/manager/Application') },
      { path: 'applicationPending', name: 'ApplicationPending', meta: { name: '申请待办' }, component: () => import('../views/manager/ApplicationPending') },
      { path: 'applicationProcessed', name: 'ApplicationProcessed', meta: { name: '申请已办' }, component: () => import('../views/manager/ApplicationProcessed') },
      { path: 'department', name: 'Department', meta: { name: '学院信息' }, component: () => import('../views/manager/Department') },
      { path: 'orderInfo', name: 'OrderInfo', meta: { name: '征订信息' }, component: () => import('../views/manager/OrderInfo') },
      { path: 'noticeList', name: 'NoticeList', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
router.beforeEach((to ,from, next) => {
  let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
  if (to.path === '/') {
    if (user.role) {
      if (user.role === 'ADMIN') {
        next('/front/home')
      } else {
        next('/home')
      }
    } else {
      next('/login')
    }
  } else {
    next()
  }
})

export default router
