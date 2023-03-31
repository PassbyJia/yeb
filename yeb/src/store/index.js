import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        routes:[]
    },
    /*唯一改变state的变量的值的方法*/
    mutations:{
        initRoutes(state,data){
            state.routes = data;
        }
    },
    actions:{}
})