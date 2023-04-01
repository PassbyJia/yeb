<template>
    <div>
        <div>
            <el-input
                    size="small"
                    class="addJlInput"
                    suffix-icon="el-icon-plus"
                    v-model="jl.name"
                    placeholder="职称名称"></el-input>
            <el-select class="addJlSel" size="small" v-model="jl.titleLevel" placeholder="职称等级">
                <el-option
                        v-for="item in titleLevels"
                        :key="item"
                        :label="item"
                        :value="item">
                </el-option>
            </el-select>
            <el-button icon="el-icon-plus" size="small" type="primary" @click="addJobLevel">添加</el-button>
            <el-button
                    icon="el-icon-minus"
                    size="small"
                    type="danger"
                    :disabled="this.ids.length==0"
                    @click="deleteMany">批量删除</el-button>
        </div>
        <div class="jlManaMain">
            <el-table
                    :data="jobLevels"
                    stripe
                    border
                    size="small"
                    @selection-change="handleSelectionChange"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="编号"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="titleLevel"
                        label="职称等级"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="200px">
                </el-table-column>
                <el-table-column
                        prop="enabled"
                        label="是否启用"
                        width="200px">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
                        <el-tag v-else type="danger">未启用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="showEditView(scope.$index, scope.row)">编辑</el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog
                title="编辑职称"
                :visible.sync="dialogVisible"
                width="30%">
            <div class="updateDiv">
                <el-tag>职称名称</el-tag>
                <el-input
                        class="update"
                        v-model="updateJl.name"
                        size="small">
                </el-input>
            </div>
            <div class="updateDiv">
                <el-tag>职称等级</el-tag>
                <el-select class="update" size="small" v-model="updateJl.titleLevel" placeholder="职称等级">
                    <el-option
                            v-for="item in titleLevels"
                            :key="item"
                            :label="item"
                            :value="item">
                    </el-option>
                </el-select>
            </div>
            <div class="updateDiv">
                <el-tag>是否启用</el-tag>
                <el-switch
                        class="update"
                        v-model="updateJl.enabled"
                        active-text="已启用"
                        inactive-text="未启用">
                </el-switch>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false" size="small">取 消</el-button>
                <el-button type="primary" @click="doUpdate" size="small">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "JoblebelMana",
        data(){
            return{
                jobLevels:[],
                titleLevels:[
                    '正高级',
                    '副高级',
                    '中级',
                    '初级'
                ],
                jl:{
                    titleLevel:'',
                    name:''
                },
                ids:[],
                dialogVisible:false,
                updateJl:{
                    titleLevel:'',
                    name:'',
                    enabled:''
                }
            }
        },
        mounted(){
            this.initJls()
        },
        methods:{
            initJls(){
                this.getRequest('/system/basic/joblevel/').then(resp=>{
                    if (resp){
                        this.jobLevels = resp;
                    }
                })
            },
            addJobLevel(){
                if (this.jl.name&&this.jl.titleLevel){
                    this.postRequest('/system/basic/joblevel/',this.jl).then(resp=>{
                        if (resp){
                            this.initJls();
                            this.jl.name='';
                            this.jl.titleLevel=''
                        }
                    })
                } else{
                    this.$message.error('职称名称和等级不能为空！');
                }

            },
            handleDelete(index,data){
                this.$confirm('此操作将永久删除['+data.name+']职称, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/joblevel/'+data.id).then(resp=>{
                        if (resp){
                            this.initJls();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            deleteMany(){
                this.$confirm('此操作将永久删除'+this.ids.length+'条职称, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = '?';
                    this.ids.forEach(item=>{
                        ids += 'ids='+item.id+'&';
                    });
                    this.deleteRequest('/system/basic/joblevel/'+ids).then(resp=>{
                        if (resp){
                            this.initJls();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleSelectionChange(val) {
                this.ids = val;
            },
            showEditView(index,data){
                this.dialogVisible=true;
                Object.assign(this.updateJl,data);
                this.updateJl.createDate='';
            },
            doUpdate(){
                if (this.updateJl.name&&this.updateJl.titleLevel){
                    this.putRequest('/system/basic/joblevel/',this.updateJl).then(resp=>{
                        if (resp){
                            this.initJls();
                            this.dialogVisible=false;
                        }
                    })
                } else{
                    this.$message.error('职称名称和等级不能为空！');
                }
            }

        }
    }
</script>

<style scoped>
    .addJlInput{
        width: 300px;
        margin-right: 8px;
    }
    .addJlSel{
        margin-right: 8px;
    }
    .jlManaMain{
        margin-top: 8px;
    }
    .update{
        width: 200px;
        margin-left: 8px;
    }
    .updateDiv{
        margin-top: 8px;
    }

</style>