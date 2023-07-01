<template>
    <div>
        <div style="display: flex;justify-content: space-between;margin-bottom: 10px">
            <div>
                <el-input style="width: 250px;margin-right: 10px;"
                          placeholder="请输入员工名进行搜索..."
                          v-model="searchName"
                          clearable
                          :disabled="showAdvanceSearchInfo"
                          @clear="initEmps"
                          @change="initEmps"
                          @input="initEmps"
                          prefix-icon="el-icon-search"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="initEmps" :disabled="showAdvanceSearchInfo">搜索</el-button>
                <el-button type="primary" @click="openAdvanceSearch">
                    <i :class="showAdvanceSearchInfo? 'fa fa-angle-double-up':'fa fa-angle-double-down'"></i>
                    高级搜索</el-button>
            </div>
            <div>
                <el-upload
                        :before-upload="beforeUpload"
                        :on-success="onSuccess"
                        :on-error="onError"
                        :headers="headers"
                        :show-file-list=false
                        style="display: inline-flex;margin-right: 10px"
                        action="/employee/basic/import">
                    <el-button type="success" :icon="importBtnIcon">
                        {{importBtnText}}
                    </el-button>
                </el-upload>

                <el-button type="success" @click="exportEmps()" icon="el-icon-download">
                    导出数据
                </el-button>
                <el-button type="primary" icon="el-icon-plus" @click="showAddWindow">添加员工</el-button>
                <el-dialog
                        :title="title"
                        :visible.sync="dialogVisible"
                        width="80%">
                    <el-form :rules="rules" ref="addOrEditEmpForm" :model="newEmp" label-width="80px">
                        <el-row>
                            <el-col :span="6">
                                <el-form-item  label="姓名:" prop="name">
                                    <el-input clearable style="width: 175px;" size="mini" prefix-icon="el-icon-edit" v-model="newEmp.name" placeholder="请填写姓名"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label-width="100px" label="身份证号码:" prop="idCard">
                                    <el-input clearable size="mini" prefix-icon="el-icon-edit" v-model="newEmp.idCard" placeholder="请填写身份证号码"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="性别:" prop="gender">
                                    <el-radio-group size="mini" v-model="newEmp.gender" style="margin-top: 10px">
                                        <el-radio label="男">男</el-radio>
                                        <el-radio label="女">女</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item prop="beginContract" style="width: 250px;" label-width="120px" label="合同起始日期:">
                                    <el-date-picker
                                            v-model="newEmp.beginContract"
                                            type="date"
                                            size="mini"
                                            value-format="yyyy-MM-dd"
                                            placeholder="选择日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="endContract" style="width: 250px;" label-width="120px" label="合同截止日期:">
                                    <el-date-picker
                                            v-model="newEmp.endContract"
                                            type="date"
                                            size="mini"
                                            value-format="yyyy-MM-dd"
                                            placeholder="选择日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="beginDate" style="width: 250px;" label-width="100px" label="入职日期:">
                                    <el-date-picker
                                            v-model="newEmp.beginDate"
                                            type="date"
                                            size="mini"
                                            value-format="yyyy-MM-dd"
                                            placeholder="选择日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="conversionTime" style="width: 250px;" label-width="100px" label="转正日期:">
                                    <el-date-picker
                                            v-model="newEmp.conversionTime"
                                            type="date"
                                            size="mini"
                                            value-format="yyyy-MM-dd"
                                            placeholder="选择日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item prop="nationId" style="width: 175px;" label="民族:">
                                    <el-select size="mini" v-model="newEmp.nationId" placeholder="请选择">
                                        <el-option
                                                v-for="item in nations"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item prop="address" label-width="100px" label="联系地址:">
                                    <el-input clearable size="mini" prefix-icon="el-icon-edit" v-model="newEmp.address" placeholder="请填写联系地址"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="nativePlace" label="籍贯:">
                                    <el-input clearable style="width: 150px;" size="mini" prefix-icon="el-icon-edit" v-model="newEmp.nativePlace" placeholder="请填写籍贯"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item prop="birthday" style="width: 250px;" label-width="100px" label="出生日期:">
                                    <el-date-picker
                                            size="mini"
                                            v-model="newEmp.birthday"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            placeholder="选择日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="politicId" label-width="100px" label="政治面貌:">
                                    <el-select size="mini" v-model="newEmp.politicId" placeholder="请选择">
                                        <el-option
                                                v-for="item in politicsStatus"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="email" style="width: 250px;" label-width="100px" label="电子邮箱:">
                                    <el-input clearable size="mini" prefix-icon="el-icon-edit" v-model="newEmp.email" placeholder="请填写电子邮箱"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="phone" style="width: 250px;" label-width="100px" label="电话号码:">
                                    <el-input clearable size="mini" prefix-icon="el-icon-edit" v-model="newEmp.phone" placeholder="请填写电话号码"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item prop="posId" label="职位:">
                                    <el-select size="mini" v-model="newEmp.posId" placeholder="请选择">
                                        <el-option
                                                v-for="item in positions"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="jobLevelId" label="职称:">
                                    <el-select size="mini" v-model="newEmp.jobLevelId" placeholder="请选择">
                                        <el-option
                                                v-for="item in joblevels"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item prop="wedlock" label-width="100px" label="婚姻状况:">
                                    <el-radio-group size="mini" v-model="newEmp.wedlock" style="margin-top: 10px">
                                        <el-radio label="未婚">未婚</el-radio>
                                        <el-radio label="已婚">已婚</el-radio>
                                        <el-radio label="离异">离异</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item prop="wordId" label="工号:">
                                    <el-input clearable style="width: 150px;" disabled size="mini" v-model="newEmp.workID" ></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="departmentId" label-width="100px" label="所属部门:">
                                    <div :class="depName!='请选择'? 'depChoosed':'depUnChoosed'" style="padding-left:5px;display: inline-flex;border: 1px solid #dedede;height: 28px;width: 200px;border-radius: 5px;cursor:pointer;font-size: 13px;padding-top: 5px" @click="showDepWindow">{{depName}}</div>
                                    <el-popover
                                            title="选择部门"
                                            width="400"
                                            trigger="manual"
                                            v-model="visible">
                                        <el-tree default-expand-all	 :data="deps" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
                                    </el-popover>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="tiptopDegree" style="width: 200px;" label="学历:">
                                    <el-select size="mini" v-model="newEmp.tiptopDegree" placeholder="请选择">
                                        <el-option
                                                v-for="item in tiptopDegree"
                                                :key="item"
                                                :label="item"
                                                :value="item">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item prop="school" label-width="100px" label="毕业院校:">
                                    <el-input clearable style="width: 200px;" size="mini" prefix-icon="el-icon-edit" v-model="newEmp.school" placeholder="请填写毕业院校"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item prop="specialty" label-width="100px" label="专业名称:">
                                    <el-input clearable style="width: 150px;" size="mini" prefix-icon="el-icon-edit" v-model="newEmp.specialty" placeholder="请填写专业名称"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="engageForm" label-width="100px" label="聘用形式:">
                                    <el-radio-group size="mini" v-model="newEmp.engageForm" style="margin-top: 10px">
                                        <el-radio label="劳动合同">劳动合同</el-radio>
                                        <el-radio label="劳务合同">劳务合同</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                        <el-button @click="dialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="valAddOrEditInfo">确 定</el-button>
                    </span>
                </el-dialog>
            </div>
        </div>
        <transition name="slide-fade">
        <div v-show="showAdvanceSearchInfo" style="border:1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px">
            <el-row>
                <el-col :span="6">
                    政治面貌:
                    <el-select size="mini" v-model="searchValue.politicId" placeholder="请选择">
                        <el-option
                                v-for="item in politicsStatus"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="6">
                    职位:
                    <el-select size="mini" v-model="searchValue.posId" placeholder="请选择">
                        <el-option
                                v-for="item in positions"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="6">
                    职称:
                    <el-select size="mini" v-model="searchValue.jobLevelId" placeholder="请选择">
                        <el-option
                                v-for="item in joblevels"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="6">
                    部门:
                    <div :class="searchDepName!='请选择'? 'depChoosed':'depUnChoosed'" style="padding-left:10px;display: inline-flex;border: 1px solid #dedede;height: 28px;width: 200px;border-radius: 5px;cursor:pointer;font-size: 13px;padding-top: 5px" @click="showSearchDepWindow">{{searchDepName}}</div>
                    <el-popover
                            title="选择部门"
                            width="400"
                            trigger="manual"
                            v-model="searchDepWindowVisible">
                        <el-tree default-expand-all	 :data="deps" :props="defaultProps" @node-click="handleSearchNodeClick"></el-tree>
                    </el-popover>
                </el-col>
            </el-row>
            <el-row style="margin-top: 10px">
                <el-col :span="6">
                    民族:
                    <el-select size="mini" v-model="searchValue.nationId" placeholder="请选择">
                        <el-option
                                v-for="item in nations"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="12">
                    入职日期:
                    <el-date-picker
                            size="mini"
                            v-model="searchValue.beginDateScope"
                            type="daterange"
                            value-format="yyyy-MM-dd"
                            unlink-panels
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </el-col>
                <el-col :span="6">
                    聘用形式:
                    <el-radio-group size="mini" v-model="searchValue.engageForm">
                        <el-radio label="劳动合同">劳动合同</el-radio>
                        <el-radio label="劳务合同">劳务合同</el-radio>
                    </el-radio-group>
                </el-col>
            </el-row>
            <el-row style="margin-top: 8px">
                <el-col :span="6" :offset="18">
                    <el-button @click="showAdvanceSearchInfo = false">返回</el-button>
                    <el-button @click="initEmps('Advance')" type="primary">搜索</el-button>
                </el-col>
            </el-row>
        </div>
        </transition>
        <div>
            <el-table
                    :data="emps"
                    border
                    style="width: 100%"
                    max-height="650">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        fixed
                        prop="name"
                        label="姓名"
                        width="75">
                </el-table-column>
                <el-table-column
                        prop="gender"
                        label="性别"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="birthday"
                        label="生日"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="idCard"
                        label="身份证"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="wedlock"
                        label="婚姻情况"
                        width="75">
                </el-table-column>
                <el-table-column
                        prop="nation.name"
                        label="民族"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="nativePlace"
                        label="籍贯"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="politicsStatus.name"
                        label="政治地位"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="email"
                        label="邮箱"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="手机号"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="地址"
                        width="300">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        label="部门"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="joblevel.name"
                        label="职称"
                        width="125">
                </el-table-column>
                <el-table-column
                        prop="position.name"
                        label="职位"
                        width="125">
                </el-table-column>
                <el-table-column
                        prop="engageForm"
                        label="聘请形式"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="tiptopDegree"
                        label="最高学历"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="specialty"
                        label="专业"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="school"
                        label="毕业院校"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="beginDate"
                        label="入职日期"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="workState"
                        label="就职状态"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="workID"
                        label="工号"
                        width="150">
                </el-table-column>
                <el-table-column
                        label="合同期限"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag style="margin-right: 3px">{{scope.row.contractTerm}}</el-tag>年
                    </template>
                </el-table-column>
                <el-table-column
                        prop="conversionTime"
                        label="转正日期"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="notWorkDate"
                        label="离职日期"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="beginContract"
                        label="合同起始日期"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="endContract"
                        label="合同终止日期"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="workAge"
                        label="工龄"
                        width="90">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpWindow(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button style="padding: 3px" size="mini" type="primary">查看高级资料</el-button>
                        <el-button @click="delEmp(scope.row)" style="padding: 3px" size="mini" type="danger">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end;margin-top: 10px">
                <el-pagination
                        background
                        @size-change="sizeChange"
                        @current-change="currentChange"
                        @prev-click="prevClick"
                        @next-click="nextClick"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "EmpBasic",
        data(){
            return{
                searchValue:{
                    politicId: null,
                    departmentId: null,
                    jobLevelId: null,
                    posId: null,
                    nationId: null,
                    engageForm: '',
                    beginDateScope:[]
                },
                showAdvanceSearchInfo:false,
                headers:{
                    Authorization:window.sessionStorage.getItem('tokenStr')
                },
                importBtnIcon:'el-icon-upload2',
                importBtnText:'导入数据',
                title:'',
                rules:{
                    name: [
                        {required: true, message: '请输入员工姓名', trigger: 'blur'}
                    ],
                    gender: [
                        {required: true, message: '请输入员工性别', trigger: 'blur'}
                    ],
                    birthday: [
                        {required: true, message: '请输入员工生日', trigger: 'blur'}
                    ],
                    idCard: [
                        {required: true, message: '请输入员工身份证', trigger: 'blur'},
                        {pattern:/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '请输入正确的员工身份证', trigger: 'blur'}
                    ],
                    wedlock: [
                        {required: true, message: '请输入员工婚姻状况', trigger: 'blur'}
                    ],
                    nationId: [
                        {required: true, message: '请输入员工所属民族', trigger: 'blur'}
                    ],
                    nativePlace: [
                        {required: true, message: '请输入员工籍贯', trigger: 'blur'}
                    ],
                    politicId: [
                        {required: true, message: '请输入员工政治面貌', trigger: 'blur'}
                    ],
                    email: [
                        {required: true, message: '请输入员工邮箱', trigger: 'blur'},
                        {type:'email', message: '请输入正确的员工邮箱', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '请输入员工手机号', trigger: 'blur'},
                        {pattern:/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,message:'请输入正确的手机号',trigger:'blur'}
                    ],
                    address: [
                        {required: true, message: '请输入员工所在地址', trigger: 'blur'}
                    ],
                    departmentId: [
                        {required: true, message: '请输入员工所在部门', trigger: 'blur'}
                    ],
                    jobLevelId: [
                        {required: true, message: '请输入员工职称', trigger: 'blur'}
                    ],
                    posId: [
                        {required: true, message: '请输入员工职位', trigger: 'blur'}
                    ],
                    engageForm: [
                        {required: true, message: '请输入员工聘用形式', trigger: 'blur'}
                    ],
                    tiptopDegree: [
                        {required: true, message: '请输入员工学位', trigger: 'blur'}
                    ],
                    specialty: [
                        {required: true, message: '请输入员工专业', trigger: 'blur'}
                    ],
                    school: [
                        {required: true, message: '请输入员工毕业院校', trigger: 'blur'}
                    ],
                    beginDate: [
                        {required: true, message: '请输入员工入职日期', trigger: 'blur'}
                    ],
                    conversionTime: [
                        {required: true, message: '请输入员工转正日期', trigger: 'blur'}
                    ],
                    beginContract: [
                        {required: true, message: '请输入员工合同起始日期', trigger: 'blur'}
                    ],
                    endContract: [
                        {required: true, message: '请输入员工合同终止日期', trigger: 'blur'}
                    ]
                },
                tiptopDegree:['博士','硕士','本科','大专','高中','初中','小学','其他'],
                depName:'请选择',
                searchDepName:'请选择',
                deps:[],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                searchDepWindowVisible:false,
                visible:false,
                joblevels:[],
                positions:[],
                nations:[],
                politicsStatus:[],
                emps:[],
                total:0,
                size:10,
                currentPage:1,
                searchName:'',
                dialogVisible:false,
                newEmp:{
                    name: '',
                    gender: '',
                    birthday: '',
                    idCard: '',
                    wedlock: '',
                    nationId: null,
                    nativePlace: '',
                    politicId: null,
                    email: '',
                    phone: '',
                    address: '',
                    departmentId: null,
                    jobLevelId: null,
                    posId: null,
                    engageForm: '',
                    tiptopDegree: '',
                    specialty: '',
                    school: '',
                    beginDate: '',
                    workState: '在职',
                    workID: '',
                    conversionTime: '',
                    notWorkDate: null,
                    beginContract: '',
                    endContract: '',
                    workAge: null,
                    salaryId: null,
                }
            }
        },
        mounted(){
          this.initEmps();
          this.initData();
          this.initPositions();
        },
        methods:{
            openAdvanceSearch(){
                this.showAdvanceSearchInfo = !this.showAdvanceSearchInfo;
                this.searchValue = {};
                this.searchDepName = '请选择';
                this.initEmps();
            },
            onSuccess(){
                this.importBtnIcon='el-icon-upload2';
                this.importBtnText='导入数据';
                this.initEmps();
                this.$message({
                    message: '导入数据成功！',
                    type: 'success'
                });
            },
            onError(){
                this.importBtnIcon='el-icon-upload2';
                this.importBtnText='导入数据'
                this.$message.error('导入数据失败！');
            },
            beforeUpload(){
              this.importBtnText = '正在导入';
              this.importBtnIcon = 'el-icon-loading';
            },
            exportEmps(){
                this.downloadRequest('/employee/basic/export');
            },
            showEditEmpWindow(data){
                this.dialogVisible = true;
                this.title = '编辑员工'
                this.newEmp = data;
                this.depName = data.department.name;
                this.initPositions();
            },
            delEmp(data){
                this.$confirm('此操作将永久删除该['+data.name+']员工, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/employee/basic/'+data.id).then(resp=>{
                        if (resp){
                            this.initEmps();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            valAddOrEditInfo(){
                if (this.title == '添加员工'){
                    this.$refs.addOrEditEmpForm.validate((valid)=>{
                        if (valid){
                            this.postRequest('/employee/basic/',this.newEmp).then(resp=>{
                                if (resp){
                                    this.dialogVisible = false;
                                    this.initEmps();
                                }
                            })
                        }
                    })
                } else{
                    this.$refs.addOrEditEmpForm.validate((valid)=>{
                        if (valid){
                            this.putRequest('/employee/basic/',this.newEmp).then(resp=>{
                                if (resp){
                                    this.dialogVisible = false;
                                    this.initEmps();
                                }
                            })
                        }
                    })
                }

            },
            handleSearchNodeClick(data){
                this.searchDepWindowVisible = !this.searchDepWindowVisible;
                this.searchValue.departmentId = data.id;
                this.searchDepName = data.name;
            },
            handleNodeClick(data){
                this.visible = !this.visible;
                this.newEmp.departmentId = data.id;
                this.depName = data.name;
            },
            showSearchDepWindow(){
                this.searchDepWindowVisible = !this.searchDepWindowVisible;
            },
            showDepWindow(){
                this.visible = !this.visible;
            },
            getWorkId(){
                this.getRequest('/employee/basic/maxWorkID').then(resp=>{
                    if (resp){
                        this.newEmp.workID = resp.obj;
                    }
                })
            },
            initPositions(){
                this.getRequest('/employee/basic/positions').then(resp=>{
                    if (resp){
                        this.positions = resp;
                    }
                })
            },
            initData(){
                if (!window.sessionStorage.getItem('nations')){
                    this.getRequest('/employee/basic/nations').then(resp=>{
                        if (resp){
                            this.nations = resp;
                            window.sessionStorage.setItem('nations',JSON.stringify(resp));
                        }
                    })
                }else{
                    this.nations = JSON.parse(window.sessionStorage.getItem('nations'));
                }
                if (!window.sessionStorage.getItem('politicsStatus')){
                    this.getRequest('/employee/basic/politicsstatus').then(resp=>{
                        if (resp){
                            this.politicsStatus = resp;
                            window.sessionStorage.setItem('politicsStatus',JSON.stringify(resp));
                        }
                    })
                }else{
                    this.politicsStatus = JSON.parse(window.sessionStorage.getItem('politicsStatus'));
                }
                if (!window.sessionStorage.getItem('joblevels')){
                    this.getRequest('/employee/basic/joblevels').then(resp=>{
                        if (resp){
                            this.joblevels = resp;
                            window.sessionStorage.setItem('joblevels',JSON.stringify(resp));
                        }
                    })
                }else{
                    this.joblevels = JSON.parse(window.sessionStorage.getItem('joblevels'));
                }
                if (!window.sessionStorage.getItem('deps')){
                    this.getRequest('/employee/basic/deps').then(resp=>{
                        if (resp){
                            this.deps = resp;
                            window.sessionStorage.setItem('deps',JSON.stringify(resp));
                        }
                    })
                }else{
                    this.deps = JSON.parse(window.sessionStorage.getItem('deps'));
                }
            },
            showAddWindow(){
                this.dialogVisible = true;
                this.title = '添加员工'
                this.initPositions();
                this.getWorkId();
                this.newEmp = {};
                this.depName = '请选择';
            },
            nextClick(currentPage){
                this.currentPage = currentPage;
                this.initEmps();
            },
            prevClick(currentPage){
                this.currentPage = currentPage;
                this.initEmps();
            },
            currentChange(currentPage){
                this.currentPage = currentPage;
                this.initEmps();
            },
            sizeChange(size){
                this.size = size;
                this.initEmps();
            },
            initEmps(type){
                let url = '/employee/basic/?currentPage='+this.currentPage+"&size="+this.size;
                if (!type||type=='Advance'){
                    if (this.searchValue.departmentId){
                        url+='&departmentId='+this.searchValue.departmentId;
                    }
                    if (this.searchValue.beginDateScope){
                        url+='&beginDateScope='+this.searchValue.beginDateScope;
                    }
                    if (this.searchValue.engageForm){
                        url+='&engageForm='+this.searchValue.engageForm;
                    }
                    if (this.searchValue.jobLevelId){
                        url+='&jobLevelId='+this.searchValue.jobLevelId;
                    }
                    if (this.searchValue.nationId){
                        url+='&nationId='+this.searchValue.nationId;
                    }
                    if (this.searchValue.politicId){
                        url+='&politicId='+this.searchValue.politicId;
                    }
                    if (this.searchValue.posId){
                        url+='&posId='+this.searchValue.posId;
                    }
                } else{
                    url+="&name="+this.searchName;
                }
                this.getRequest(url).then(resp=>{
                    if (resp){
                        this.emps = resp.data;
                        this.total = resp.total;
                    }
                })
            }
        }
    }
</script>

<style>
    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .5s ease;
    }
    .slide-fade-leave-active {
        transition: all .5s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }
    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */ {
        transform: translateX(10px);
        opacity: 0;
    }

    /*部门选择前后的字体颜色*/
    .depChoosed{
        color: black;
    }
    .depUnChoosed{
        color: #dddfe4;
    }

</style>