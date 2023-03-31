<template>
    <div>
        <div>
            <el-input
                    size="small"
                    class="addPosInput"
                    placeholder="添加职位"
                    suffix-icon="el-icon-plus"
                    v-model="pos.name"
                    @keydown.enter.native="addPosition">
            </el-input>
            <el-button icon="el-icon-plus" size="small" type="primary" @click="addPosition">添加</el-button>
            <el-button
                    icon="el-icon-minus"
                    size="small"
                    type="danger"
                    :disabled="this.ids.length==0"
                    @click="deleteMany">批量删除</el-button>
        </div>
        <div class="posManaMain">
            <el-table
                    :data="positions"
                    stripe
                    border
                    size="small"
                    @selection-change="handleSelectionChange"
                    style="width: 70%">
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
                        label="职位"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="200px">
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
                title="编辑职位"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <el-tag>职位名称</el-tag>
                <el-input
                        v-model="updatePos.name"
                        size="small"
                        class="updatePosInput"
                        @keydown.enter.native="addPosition">
                </el-input>
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
        name: "PosMana",
        data(){
            return{
                pos:{
                    name:''
                },
                positions: [],
                dialogVisible:false,
                updatePos:{
                    name:''
                },
                ids: []
            }
        },
        mounted(){
            this.initPositions()
        },
        methods:{
            initPositions(){
                this.getRequest('/system/basic/pos/').then(resp=>{
                    if (resp){
                        this.positions = resp;
                    }
                })
            },
            addPosition(){
                if (this.pos.name){
                    this.postRequest('/system/basic/pos/',this.pos).then(resp=>{
                        if (resp){
                            this.initPositions();
                            this.pos.name="";
                        }
                    })
                } else{
                    this.$message.error('职位名称不能为空！');
                }
            },
            showEditView(index,data){
                this.dialogVisible = true;
                Object.assign(this.updatePos,data);
                this.updatePos.createDate='';
            },
            doUpdate(){
                this.putRequest('/system/basic/pos/',this.updatePos).then(resp=>{
                    if (resp){
                        this.initPositions();
                        this.dialogVisible = false;
                    }
                })
            },
            handleDelete(index,data){
                this.$confirm('此操作将永久删除['+data.name+']职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/pos/'+data.id).then(resp=>{
                        if (resp){
                            this.initPositions();
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
            deleteMany(){
                this.$confirm('此操作将永久删除['+this.ids.length+']条职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = '?';
                    this.ids.forEach(item=>{
                        ids+='ids='+item.id+'&';
                    });
                    this.deleteRequest('/system/basic/pos/'+ids).then(resp=>{
                        if (resp){
                            this.initPositions();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        }
    }
</script>

<style scoped>
    .addPosInput{
        width: 300px;
        margin-right: 8px;
    }
    .posManaMain{
        margin-top: 10px;
    }
    .updatePosInput{
        width: 200px;
        margin-left: 8px;
    }
</style>