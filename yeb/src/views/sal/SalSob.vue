<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <el-button type="primary" icon="el-icon-plus" @click="showAddSalWindow">添加工资账套</el-button>
            <el-dialog
                    :title="dialogTitle"
                    :visible.sync="dialogVisible"
                    width="80%">
                <el-steps :active="activeStep" finish-status="success" simple >
                    <el-step :title="item" v-for="(item,index) in salItemName" :key="index" ></el-step>
                </el-steps>
                <div style="text-align:center;vertical-align:middle;">
                    <el-input v-model="salary[title]" style="margin-top: 30px;width: 350px;text-align: center" v-show="index == activeStep" :placeholder="'请输入'+salItemName[index]" v-for="(value,title,index) in salary" :key="index"></el-input>
                </div>

                <span slot="footer" class="dialog-footer">
                    <el-button @click="preStep">上一步</el-button>
                    <el-button type="primary" @click="nextStep">{{activeStep<10?'下一步':'确定'}}</el-button>
                </span>
            </el-dialog>
            <el-button type="success" @click="initSalaries" icon="el-icon-refresh"></el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table :data="salaries" stripe border>
                <el-table-column type="selection" width="45"></el-table-column>
                <el-table-column label="账套名称" prop="name" width="120"></el-table-column>
                <el-table-column label="基本工资" prop="basicSalary" width="75"></el-table-column>
                <el-table-column label="交通补助" prop="trafficSalary" width="75"></el-table-column>
                <el-table-column label="午餐补助" prop="lunchSalary" width="75"></el-table-column>
                <el-table-column label="奖金" prop="bonus" width="60"></el-table-column>
                <el-table-column label="启用时间" prop="createDate" width="120"></el-table-column>
                <el-table-column label="养老金">
                    <el-table-column label="比率" prop="pensionPer" width="60"></el-table-column>
                    <el-table-column label="基数" prop="pensionBase" width="60"></el-table-column>
                </el-table-column>
                <el-table-column label="医疗保险">
                    <el-table-column label="比率" prop="medicalPer" width="60"></el-table-column>
                    <el-table-column label="基数" prop="medicalBase" width="60"></el-table-column>
                </el-table-column>
                <el-table-column label="公积金">
                    <el-table-column label="比率" prop="accumulationFundPer" width="60"></el-table-column>
                    <el-table-column label="基数" prop="accumulationFundBase" width="60"></el-table-column>
                </el-table-column>
                <el-table-column label="操作" prop="name">
                    <template slot-scope="scope">
                        <el-button @click="editSal(scope.row)" type="primary">编辑</el-button>
                        <el-button @click="delSal(scope.row)" type="danger">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SalSob",
        data(){
            return{
                dialogTitle:'添加工资账套',
                activeStep:0,
                salItemName:[
                    '账套名称',
                    '基本工资',
                    '交通补助',
                    '午餐补助',
                    '奖金',
                    '养老金比率',
                    '养老金基数',
                    '医疗保险比率',
                    '医疗保险基数',
                    '公积金比率',
                    '公积金基数'
                ],
                salary:{
                    name:'',
                    basicSalary:null,
                    trafficSalary:null,
                    lunchSalary:null,
                    bonus:null,
                    pensionPer:null,
                    pensionBase:null,
                    medicalPer:null,
                    medicalBase:null,
                    accumulationFundPer:null,
                    accumulationFundBase:null
                },
                salaries:[],
                dialogVisible:false
            }
        },
        mounted(){
            this.initSalaries();
        },
        methods:{
            editSal(data){
                this.dialogVisible = true;
                this.activeStep = 0;
                this.dialogTitle = '编辑工资账套';
                this.salary.id = data.id;
                this.salary.name = data.name;
                this.salary.basicSalary = data.basicSalary;
                this.salary.trafficSalary = data.trafficSalary;
                this.salary.lunchSalary = data.lunchSalary;
                this.salary.bonus = data.bonus;
                this.salary.pensionPer = data.pensionPer;
                this.salary.pensionBase = data.pensionBase;
                this.salary.medicalPer = data.medicalPer;
                this.salary.medicalBase = data.medicalBase;
                this.salary.accumulationFundPer = data.accumulationFundPer;
                this.salary.accumulationFundBase = data.accumulationFundBase;
            },
            delSal(data){
                this.$confirm('此操作将永久删除该工资账套['+data.name+'], 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/salary/sob/'+data.id).then(resp=>{
                        if (resp){
                            this.initSalaries();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            preStep(){
                if (this.activeStep>0){
                    this.activeStep--;
                }else{
                    this.$message({
                        message: '这已经是第一步了哦~',
                        type: 'warning'
                    });
                }
            },
            nextStep(){
                if (this.activeStep<10){
                    this.activeStep++;
                }else{
                    if (this.dialogTitle=='添加工资账套') {
                        this.postRequest('/salary/sob/',this.salary).then(resp=>{
                            if (resp){
                                this.dialogVisible = false;
                                this.initSalaries();
                            }
                        })
                    }else{
                        this.putRequest('/salary/sob/',this.salary).then(resp=>{
                            if (resp){
                                this.dialogVisible = false;
                                this.initSalaries();
                            }
                        })
                    }

                }
            },
            showAddSalWindow(){
                this.dialogVisible = true;
                this.activeStep = 0;
                this.dialogTitle = '添加工资账套';
                this.salary = {
                    name:'',
                    basicSalary:null,
                    trafficSalary:null,
                    lunchSalary:null,
                    bonus:null,
                    pensionPer:null,
                    pensionBase:null,
                    medicalPer:null,
                    medicalBase:null,
                    accumulationFundPer:null,
                    accumulationFundBase:null
                };
            },
            initSalaries(){
                this.getRequest('/salary/sob/').then(resp => {
                    if (resp){
                        this.salaries = resp;
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>