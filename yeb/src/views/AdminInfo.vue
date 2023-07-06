<template>
    <div style="display: flex;justify-content: center;">
        <el-card :body-style="{ padding: '0px' }" style="width: 500px;">
            <div style="display: flex;justify-content: center;margin-top: 8px">
            <img class="userface-img" :src="admin.userFace" >
            </div>
            <div style="padding: 14px;">
                <span style="display: flex;justify-content: center;font-weight: bolder">{{admin.name}}</span>
                <div style="display: flex;justify-content: center;margin-top: 8px">
                    <table>
                        <tr>
                            <td>电话号码:</td>
                            <td>{{admin.telephone}}</td>
                        </tr>
                        <tr>
                            <td>手机号码:</td>
                            <td>{{admin.phone}}</td>
                        </tr>
                        <tr>
                            <td>居住地址:</td>
                            <td>{{admin.address}}</td>
                        </tr>
                        <tr>
                            <td>用户标签:</td>
                            <td>
                                <el-tag v-for="(item,index) in admin.roles" :key="index" style="margin-right: 3px">
                                    {{item.nameZh}}
                                </el-tag>
                            </td>
                        </tr>
                    </table>
                </div>
                <div style="display: flex;justify-content: center;margin-top: 8px">
                    <el-button type="primary" @click="dialogVisible = true" style="margin-right: 8px">修改信息</el-button>
                    <el-dialog
                            title="修改信息"
                            :visible.sync="dialogVisible"
                            width="50%">
                        <div style="display: flex;justify-content: center">
                            <table>
                                <tr>
                                    <td>用户昵称:</td>
                                    <td><el-input v-model="updateAdmin.name"></el-input></td>
                                </tr>
                                <tr>
                                    <td>电话号码:</td>
                                    <td><el-input v-model="updateAdmin.telephone"></el-input></td>
                                </tr>
                                <tr>
                                    <td>手机号码:</td>
                                    <td><el-input v-model="updateAdmin.phone"></el-input></td>
                                </tr>
                                <tr>
                                    <td>居住地址:</td>
                                    <td><el-input style="width: 200px;" v-model="updateAdmin.address"></el-input></td>
                                </tr>
                            </table>
                        </div>
                        <span slot="footer" class="dialog-footer">
                            <el-button @click="dialogVisible = false">取 消</el-button>
                            <el-button type="primary" @click="updateAdminInfo">确 定</el-button>
                        </span>
                    </el-dialog>
                    <el-button @click="dialogVisible1 = true" type="danger">修改密码</el-button>
                    <el-dialog
                            title="修改密码"
                            :visible.sync="dialogVisible1"
                            width="50%">
                        <div style="display: flex;justify-content: center">
                            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                                <el-form-item label="旧密码" prop="oldPass">
                                    <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="密码" prop="pass">
                                    <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="确认密码" prop="checkPass">
                                    <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-dialog>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "AdminInfo",
        data(){
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.pass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return{
                ruleForm: {
                    oldPass:'',
                    pass: '',
                    checkPass: ''
                },
                rules: {
                    pass: [
                        { validator: validatePass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { validator: validatePass2, trigger: 'blur' }
                    ],
                    oldPass:[
                        {
                            validator:validatePass,trigger:'blur'
                        }
                    ]
                },
                dialogVisible1:false,
                admin:{},
                updateAdmin:{},
                dialogVisible:false
            }
        },
        mounted(){
            this.initAdmin();
        },
        methods:{
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.ruleForm.adminId = this.admin.id;
                        this.putRequest('/admin/pass',this.ruleForm).then(resp=>{
                            if (resp){
                                //退出登录
                                this.postRequest('/logout');
                                window.sessionStorage.removeItem('user');
                                window.sessionStorage.removeItem('tokenStr');
                                this.$store.commit('initRputes',[]);
                                this.$router.replace('/');
                            }
                        })
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            updateAdminInfo(){
                this.dialogVisible = false;
                 this.putRequest('/admin/info',this.updateAdmin).then(resp=>{
                     if (resp){
                         this.initAdmin();
                     }
                 })
            },
            initAdmin(){
                this.getRequest('/admin/info').then(resp=>{
                    if (resp){
                        this.admin = resp;
                        this.updateAdmin = Object.assign({},this.admin);
                        window.sessionStorage.setItem('user',JSON.stringify(resp));
                        this.$store.commit('INIT_ADMIN',resp);
                    }
                })
            }
        }
    }
</script>

<style>
    .userface-img {
        width: 72px;
        height: 72px;
        border-radius: 72px;
    }
</style>