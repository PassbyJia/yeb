<template>
    <div>
        <el-container>
            <el-header class="homeHeader">
                <div class="title">云E办</div>
                <el-dropdown class="userInfo">
                <span class="el-dropdown-link">
                {{user.name}}<img src="user.userFace"></img>
                </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>个人中心</el-dropdown-item>
                        <el-dropdown-item>设置</el-dropdown-item>
                        <el-dropdown-item>注销登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-header>

            <el-container>
                <el-aside width="200px">
                    <el-menu router unique-opened="">
                        <el-submenu :index="index+''"
                                    v-for="(item,index) in routes" :key="index"
                                    v-if="!item.hidden">
                            <template slot="title">
                                <i :class="item.iconCls" style="color: mediumslateblue;margin-right: 5px"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item :index="children.path"
                                          v-for="(children,indexj) in item.children"
                                          :key="indexj">
                                {{children.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <router-view/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data(){
          return{
              user:JSON.parse(window.sessionStorage.getItem('user'))
          }
        },
        computed:{
            routes(){
                return this.$store.state.routes;
            }
        },
        methods: {

        }
    }
</script>

<style scoped>
    .homeHeader{
        background: mediumslateblue;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 15px;
        box-sizing: border-box;
    }
    .homeHeader .title{
        font-size: 30px;
        font-family: 华文彩云;
        color: white;
    }
    .homeHeader .userInfo{
        cursor: pointer;
    }
    .el-dropdown-link img{
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }
</style>