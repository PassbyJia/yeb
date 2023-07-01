<template>
    <div>
        <el-table :data="emps">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="name" label="姓名" width="60"></el-table-column>
            <el-table-column prop="workID" label="工号" width="90"></el-table-column>
            <el-table-column prop="email" label="邮箱地址" width="140"></el-table-column>
            <el-table-column prop="phone" label="电话号码" width="100"></el-table-column>
            <el-table-column label="所属部门" width="100">
                <template slot-scope="scope">
                    {{scope.row.department.name}}
                </template>
            </el-table-column>
            <el-table-column label="工资账套" width="140" align="center">
                <template slot-scope="scope">
                    <el-tooltip v-if="scope.row.salary" class="item" effect="dark" placement="right">
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>基本工资</td>
                                    <td>{{scope.row.salary.basicSalary}}</td>
                                </tr>
                                <tr>
                                    <td>交通补助</td>
                                    <td>{{scope.row.salary.trafficSalary}}</td>
                                </tr>
                                <tr>
                                    <td>午餐补助</td>
                                    <td>{{scope.row.salary.lunchSalary}}</td>
                                </tr>
                                <tr>
                                    <td>奖金</td>
                                    <td>{{scope.row.salary.bonus}}</td>
                                </tr>
                                <tr>
                                    <td>养老金比率</td>
                                    <td>{{scope.row.salary.pensionPer}}</td>
                                </tr>
                                <tr>
                                    <td>养老金基数</td>
                                    <td>{{scope.row.salary.pensionBase}}</td>
                                </tr>
                                <tr>
                                    <td>医疗保险比率</td>
                                    <td>{{scope.row.salary.medicalPer}}</td>
                                </tr>
                                <tr>
                                    <td>医疗保险基数</td>
                                    <td>{{scope.row.salary.medicalBase}}</td>
                                </tr>
                                <tr>
                                    <td>公积金比率</td>
                                    <td>{{scope.row.salary.accumulationFundPer}}</td>
                                </tr>
                                <tr>
                                    <td>公积金基数</td>
                                    <td>{{scope.row.salary.accumulationFundBase}}</td>
                                </tr>
                            </table>
                        </div>
                        <el-tag >{{scope.row.salary.name}}</el-tag>
                    </el-tooltip>
                    <el-tag type="warning" v-else>未设置工资账套</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="" label="操作"  align="center">
                <template slot-scope="scope">
                    <el-popover
                            @hide="hidePop(scope.row)"
                            @show="showPop(scope.row)"
                            placement="left"
                            title="修改工资账套"
                            width="200"
                            trigger="click">
                        <el-select v-model="currentSal" placeholder="请选择">
                            <el-option
                                    v-for="item in salaries"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                        <el-button slot="reference" type="danger">修改工资账套</el-button>
                    </el-popover>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                style="display: flex;justify-content: flex-end"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 100, 500, 1000]"
                :page-size="size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalPages">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name: "SalSobCfg",
        data(){
            return{
                currentSal:null,
                salaries:[],
                totalPages:0,
                currentPage:1,
                size:10,
                emps:[]
            }
        },
        mounted(){
            this.initEmps();
            this.initSalaries();
        },
        methods:{
            hidePop(data){
                if (data.salary!=null){
                    if (this.currentSal && this.currentSal !=data.salary.id){
                        this.putRequest('/salary/sobcfg/?eid='+data.id+'&sid='+this.currentSal).then(resp=>{
                            if (resp){}
                            this.initEmps();
                        })
                    }
                } else{
                    if (this.currentSal){
                        this.putRequest('/salary/sobcfg/?eid='+data.id+'&sid='+this.currentSal).then(resp=>{
                            if (resp){}
                            this.initEmps();
                        })
                    }
                }

            },
            showPop(data){
                if (data.salary){
                    this.currentSal = data.salary.id;
                }else{
                    this.currentSal = null;
                }
            },
            initSalaries(){
                this.getRequest('/salary/sobcfg/salaries').then(resp=>{
                    if (resp){
                        this.salaries = resp;
                    }
                })
            },
            handleCurrentChange(currentPage){
                this.currentPage = currentPage;
                this.initEmps();
            },
            handleSizeChange(size){
                this.size = size;
                this.initEmps();
            },
            initEmps(){
                this.getRequest('/salary/sobcfg/?currentPage='+this.currentPage+'&size='+this.size).then(resp=>{
                    if (resp){
                        this.emps = resp.data;
                        this.totalPages = resp.total;
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>