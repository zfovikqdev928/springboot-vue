<template>
  <div>
    <!--   搜索   -->
    <div style="padding: 10px 0">
      <el-input v-model="username" placeholder="请输入用户名" style="width: 200px"
                suffix-icon="el-icon-search"></el-input>
      <el-input v-model="email" class="ml-5" placeholder="请输入邮箱"
                style="width: 200px" suffix-icon="el-icon-message"></el-input>
      <el-input v-model="address" class="ml-5" placeholder="请输入地址"
                style="width: 200px" suffix-icon="el-icon-position"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <!--   新增 、批量删除、导入、导出    -->
    <div style="padding: 10px 0">
      <el-button class="ml-5" type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i>
      </el-button>
      <!--批量删除提示框-->
      <el-popconfirm class="ml-5"
                     confirm-button-text='确定'
                     cancel-button-text='取消'
                     icon="el-icon-info"
                     icon-color="red"
                     title="您确定批量删除这些数据吗?"
                     @confirm="delBatch">
        <el-button class="ml-5" type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i>
        </el-button>
      </el-popconfirm>

      <el-upload action="http://localhost:9090/user/import"
                 :show-file-list="false"
                 accept="xlsx,xls"
                 :on-success="handleIExcelImportSuccess"
                 :on-error="handleIExcelImportError"
                 style="display: inline-block">
        <el-button class="ml-5" type="primary">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button class="ml-5" type="primary" @click="exp">导出<i class="el-icon-top"></i></el-button>
    </div>


    <el-table :data="tableData"
              :header-cell-style="{background:'#eef1f6',color:'#606266'}"
              border stripe:true
              @selection-change="handleSelectionChange">
      <!--     多选框     -->
      <el-table-column align="center"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column align="center" label="ID" prop="id" sortable width="90"></el-table-column>
      <el-table-column align="center" label="用户名" prop="username" sortable width="140"></el-table-column>
      <el-table-column align="center" label="昵称" prop="nickname" width="140"></el-table-column>
      <el-table-column align="center" label="邮箱" prop="email" width="180"></el-table-column>
      <el-table-column align="center" label="手机号" prop="phone" sortable width="140"></el-table-column>
      <el-table-column align="center" label="地址" prop="address"></el-table-column>
      <el-table-column align="center" label="操作" width="200">

        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <!-- 删除提示框-->
          <el-popconfirm class="ml-5"
                         confirm-button-text='确定'
                         cancel-button-text='取消'
                         icon="el-icon-info"
                         icon-color="red"
                         title="您确定删除吗?"
                         @confirm="handleDel(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-delete"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          :current-page="pageNum"
          :page-size=pageSize
          :page-sizes="[5, 10,15]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
      </el-pagination>
    </div>

    <!--   新增弹出对话框     -->
    <div>
      <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form label-width="80px" size="small" :model="form">
          <el-form-item label="用户名">
            <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" autocomplete="off" placeholder="请输入昵称"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input type="text" v-model="form.password" autocomplete="off" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" autocomplete="off" placeholder="请输入邮箱"></el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" autocomplete="off" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" autocomplete="off" placeholder="请输入地址"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>
    </div>


  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      email: "",
      address: "",
      nickname: "",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false
    }
  },

  created() {
    // 请求分页数据
    this.load()
  },

  methods: {
    // 分页查询
    load: function () {
      // axios 请求分页查询
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address
        }
      })
          .then(res => {
            // 解析res的数据
            this.tableData = res.data.records
            this.total = res.data.total
          })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()

    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    // 重置按钮方法
    reset() {
      this.username = "",
          this.email = "",
          this.address = "",
          this.load()
    },
    // 新增用户时弹窗
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {
      }
    },
    // 编辑用户
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    // 删除用户
    handleDel(id) {
      this.request.delete("/user/del/" + id).then(res => {
        if (res.data) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
      this.load()
    },
    // 批量删除用户
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id); // [{},{}] => [1,2,3...]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.data) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })

    },
    // 弹窗新增用户信息
    save() {
      this.request.post("/user/save", this.form).then(res => {
        if (res.data) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false  //关闭弹窗
          this.load()
        } else {
          this.$message.error("保存失败")
          this.load()
        }
      })
    },
    // 多选框
    handleSelectionChange(data) {
      this.multipleSelection = data
    },

    // 导出
    exp() {
      window.open("http://localhost:9090/user/export")
    },

    // 导入成功
    handleIExcelImportSuccess() {
      this.$message.success("导入成功！")
      this.load()
    },
    //导入失败
    handleIExcelImportError() {
      this.$message.error("导入失败，只支持 .xlsx 或者 .xls 格式的文件！")
      this.load()
    }
  }
}
</script>