import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//引入ElementUI
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import 'font-awesome/css/font-awesome.css'

Vue.config.productionTip = false
//安装ElementUI
Vue.use(ElementUI,{size:'small'})

import {postRequest} from "./utils/api";
import {putRequest} from "./utils/api";
import {getRequest} from "./utils/api";
import {deleteRequest} from "./utils/api";
import {initMenu} from "./utils/menus";
import {downloadRequest} from "./utils/download";

//插件
Vue.prototype.postRequest = postRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.downloadRequest = downloadRequest;

router.beforeEach((to,from,next)=>{
  //判断用户是否登录
  if (window.sessionStorage.getItem('tokenStr')){
    //已经登录则初始化菜单
    initMenu(router, store);
    //用户信息是否存在
    if (!window.sessionStorage.getItem('user')){
      return getRequest('/admin/info').then(resp=>{
        if (resp){
          //存入用户信息
          window.sessionStorage.setItem('user',JSON.stringify(resp));
          next();
        }
      })
    }
    next();
  }else{
    //没有登录，但跳转的是登录页也可以
    if (to.path=='/'){
      next();
    }else{
      next('/?redirect='+to.path);
    }
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
