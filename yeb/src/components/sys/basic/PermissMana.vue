<template>
    <div>
        <div class="toolbar">
            <el-input size="small" class="permissInput" placeholder="角色英文名" v-model="role.name">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input size="small" class="permissInput" placeholder="角色中文名" v-model="role.nameZh">
            </el-input>
            <el-button icon="el-icon-plus" size="small" type="primary" @click="addRole">添加</el-button>
        </div>
        <div>
            <el-collapse v-model="activeName" accordion @change="change">
                <el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r,index) in roles" :key="index">
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>可访问资源</span>
                            <el-button style="float: right; padding: 3px 0; color: red" type="text" icon="el-icon-delete"></el-button>
                        </div>
                        <div>
                            <el-tree show-checkbox
                                     :data="menus"
                                     :props="defaultProps"
                                     ref="tree"
                                     :default-checked-keys="checkedIds"
                                     node-key="id"></el-tree>
                            <div style="display: flex;justify-content: flex-end;margin-top: 8px">
                                <el-button size="mini" @click="cancelUpdate()">取消修改</el-button>
                                <el-button size="mini" type="primary" @click="updateCheckIds(r.id,index)">确认修改</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-collapse-item>
            </el-collapse>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PermissMana",
        data(){
            return{
                role:{
                    name:'',
                    nameZh:''
                },
                roles:[],
                menus:[],
                defaultProps:{
                    children:'children',
                    label:'name'
                },
                checkedIds:[],
                activeName:-1
            }
        },
        mounted(){
            this.initRoles();
        },
        methods:{
            initRoles(){
                this.getRequest('/system/basic/permiss/').then(resp=>{
                    if (resp){
                        this.roles = resp;
                    }
                })
            },
            initMenus(){
                this.getRequest('/system/basic/permiss/menus').then(resp=>{
                    if (resp){
                        this.menus = resp;
                    }
                })
            },
            change(rid){
                this.initMenus();
                this.getCheckedIds(rid);
            },
            getCheckedIds(rid){
                this.getRequest('/system/basic/permiss/mid/'+rid).then(resp=>{
                    if (resp){
                        this.checkedIds = resp;
                    }
                })
            },
            updateCheckIds(rid,index){
                let checkKeys = this.$refs.tree[index].getCheckedKeys(true);
                let url = "/system/basic/permiss/?rid="+rid;
                checkKeys.forEach(key=>{
                    url += "&&mids="+key;
                });
                this.putRequest(url).then(resp=>{
                    if (resp){
                        this.initRoles();
                        this.activeName=-1;
                    }
                })
            },
            cancelUpdate(){
                this.activeName=-1;
            }
        }
    }
</script>

<style scoped>
    .toolbar{
        display: flex;
        margin-bottom: 8px;
    }
    .toolbar .permissInput{
        width: 300px;
        margin-right: 9px;
    }
</style>