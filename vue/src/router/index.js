import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)


const routes = [
    // 主页路由
    {
        path: '/',
        component: () => import('../views/Manage.vue'),
        redirect: "/home",
        // 子路由
        children:
            [
                {path: 'home', name: '首页', component: () => import('../views/Home.vue')},
                {path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
                {path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
                {path: 'role', name: '角色管理', component: () => import('../views/Role.vue')},
                {path: 'menu', name: '菜单管理', component: () => import('../views/Menu.vue')},
                {path: 'file', name: '文件管理', component: () => import('../views/File.vue')}
            ]
    },

    // 关于路由
    {
        path: '/about',
        name: 'about',
        component: () => import('../views/AboutView.vue')
    },

    // 登录路由
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },

    // 注册路由
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
