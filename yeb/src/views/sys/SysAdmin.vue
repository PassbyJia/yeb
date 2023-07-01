<template>
    <div>
        <div style="display: flex;justify-content: center;margin-top: 10px;margin-bottom: 20px">
            <el-input v-model="keywords" placeholder="通过用户名搜索用户..." prefix-icon="el-icon-search" style="width: 400px;margin-right: 10px"></el-input>
            <el-button style="background-color: mediumslateblue;border-color: mediumslateblue" type="primary" icon="el-icon-search" @click="doSearch()">搜索</el-button>
        </div>
        <div class="admin-container">
            <el-card class="admin-card" v-for="(admin,index) in admins" :key="index">
                <div slot="header" class="clearfix">
                    <span>{{admin.name}}</span>
                    <el-button style="float: right; padding: 3px 0;color: red" type="text" icon="el-icon-delete" @click="deleteAdmin(admin)"></el-button>
                </div>
                <div>
                    <div class="img-container">
                        <img :src="admin.userFace" :alt="admin.name" :title="admin.name" class="userface-img">
                    </div>
                    <div class="userinfo-container">
                        <table>
                            <tr>
                                <td class="userinfo-title">用户名：</td>
                                <td>{{admin.name}}</td>
                            </tr>
                            <tr>
                                <td class="userinfo-title">手机号码：</td>
                                <td>{{admin.phone}}</td>
                            </tr>
                            <tr>
                                <td class="userinfo-title">电话号码：</td>
                                <td>{{admin.telphone}}</td>
                            </tr>
                            <tr>
                                <td class="userinfo-title">地址：</td>
                                <td>{{admin.address}}</td>
                            </tr>
                            <tr>
                                <td class="userinfo-title">用户状态：</td>
                                <td>
                                    <el-switch
                                            @change="updateStatus(admin)"
                                            v-model="admin.enabled"
                                            active-color="#13ce66"
                                            inactive-color="#ff4949"
                                            active-text="启用"
                                            inactive-text="不启用">
                                    </el-switch>
                                </td>
                            </tr>
                            <tr>
                                <td class="userinfo-title">用户角色：</td>
                                <td>
                                    <el-tag style="margin-right: 10px" type="success" v-for="(role,index) in admin.roles" :key="indexj">{{role.nameZh}}</el-tag>
                                    <el-popover
                                            @show="showAllRoles(admin)"
                                            @hide="updateRoles(admin)"
                                            placement="right"
                                            title="角色列表"
                                            width="200"
                                            trigger="click">
                                        <el-select v-model="selectedRoles" multiple placeholder="请选择">
                                            <el-option
                                                    v-for="(r,index) in allRoles"
                                                    :key="index"
                                                    :label="r.nameZh"
                                                    :value="r.id">
                                            </el-option>
                                        </el-select>
                                        <el-button slot="reference" type="text" icon="el-icon-more" style="color: mediumslateblue"></el-button>
                                    </el-popover>

                                </td>
                            </tr>
                            <tr>
                                <td class="userinfo-title">备注：</td>
                                <td>{{admin.remark}}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SysAdmin",
        data(){
            return{
                admins:[],
                keywords:'',
                allRoles:[],
                selectedRoles:[]
            }
        },
        mounted(){
            this.initAdmins();
        },
        methods:{
            updateRoles(admin){
                let roles = [];
                Object.assign(roles,admin.roles);
                let flag = false;
                if (roles.length!=this.selectedRoles.length){
                    flag = true;
                } else{
                    for(let i = 0;i<roles.length;i++){
                        let role = roles[i];
                        for (let j = 0; j < this.selectedRoles.length; j++) {
                            let sr = this.selectedRoles[j];
                            if (role.id == sr) {
                                roles.splice(i,1);
                                i--;
                                break;
                            }
                        }
                    }
                    if (roles.length !=0){
                        flag = true;
                    }
                }
                if (flag){
                    let url='/system/admin/role/?adminId='+admin.id;
                    this.selectedRoles.forEach(sr=>{
                        url+='&rids='+sr;
                    });
                    this.putRequest(url).then(resp=>{
                        if (resp){
                            this.initAdmins();
                        }
                    })
                }
            },
            showAllRoles(admin){
                this.initRoles();
                let roles = admin.roles;
                this.selectedRoles = [];
                roles.forEach(r=>{
                    this.selectedRoles.push(r.id);
                })
            },
            initRoles(){
                this.getRequest('/system/admin/roles').then(resp=>{
                    if (resp){
                        this.allRoles = resp;
                    }
                })
            },
            updateStatus(admin){
                this.putRequest('/system/admin/',admin).then(resp=>{
                    if (resp){
                        this.initAdmins();
                    }
                })
            },
            deleteAdmin(admin){
                this.$confirm('此操作将永久删除该['+admin.name+']操作员, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/admin/'+admin.id).then(resp=>{
                        if (resp){
                            this.initAdmins();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            doSearch(){
              this.initAdmins();
            },
            initAdmins(){
                this.getRequest('/system/admin/?keywords='+this.keywords).then(resp=>{
                    if (resp){
                        this.admins = resp;
                    }
                })
            }
        }
    }
</script>

<style>
    .admin-card{
        width: 350px;
        margin-bottom: 20px;
    }
    .admin-container{
        display: flex;
        justify-content: space-around;
        flex-wrap: wrap;/*自动换行*/
    }
    .userface-img{
        width: 72px;
        height: 72px;
        border-radius: 72px;
    }
    .img-container{
        width: 100%;
        display: flex;
        justify-content: center;
        margin-bottom: 10px;
    }
    .userinfo-container{
        font-size: 12px;
        color: mediumslateblue;
    }
    .userinfo-container table{
        table-layout: auto;
        width: 100%;
        height: 200px;
    }
    .userinfo-title{
        width: 60px;
    }
    .el-switch__label.is-active{
        color: mediumslateblue;
    }
</style>