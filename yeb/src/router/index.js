import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login'
import Home from "../views/Home";
import Test1 from "../views/Test1";
import Test2 from "../views/Test2";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login,
    hidden:true
  },
  {
    path:'/home',
    name:'导航一',
    component:Home,
    children:[
      {
        path:'/test1',
        name:'Test1',
        component:Test1
      },
      {
        path:'/test2',
        name:'Test2',
        component:Test2
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

export default router
