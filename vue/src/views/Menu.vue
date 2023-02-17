<template>
  <div>
    <!--   搜索   -->
    <div style="padding: 10px 0">
      <el-input v-model="name" placeholder="请输入角色名称" style="width: 200px"
                suffix-icon="el-icon-search"></el-input>
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
    </div>


    <el-table :data="tableData" :header-cell-style="{background:'#eef1f6',color:'#606266'}"
              border stripe:true
              row-key="id"
              default-expand-all="true"
              @selection-change="handleSelectionChange">
      <!--     多选框     -->
      <el-table-column align="center"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column align="center" label="ID" prop="id" sortable width="80"></el-table-column>
      <el-table-column align="center" label="名称" prop="name" sortable></el-table-column>
      <el-table-column align="center" label="路径" prop="path"></el-table-column>
      <el-table-column align="center" label="图标" prop="icon"></el-table-column>
      <el-table-column align="center" label="描述" prop="description"></el-table-column>
      <el-table-column align="center" label="操作" width="280">

        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="scope.row.pid & !scope.row.path">新增子菜单<i class="el-icon-plus"></i></el-button>
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


    <!--   新增弹出对话框     -->
    <div>
      <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form label-width="80px" size="small" :model="form">
          <el-form-item label="名称">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="路径">
            <el-input v-model="form.path" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="图标">
            <el-input v-model="form.icon" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: "Menu",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
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
      this.request.get("/menu", {
        params: {
          name: this.name
        }
      })
          .then(res => {
            // 解析res的数据
            this.tableData = res.data
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
      this.name = ""
      this.load()
    },
    // 新增用户时弹窗
    handleAdd(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (id){
        this.form.pid = pid
      }
    },
    // 编辑用户
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    // 删除用户
    handleDel(id) {
      this.request.delete("/menu/del/" + id).then(res => {
        if (res.code === '200') {
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
      this.request.post("/menu/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })

    },
    // 弹窗新增用户信息
    save() {
      this.request.post("/menu/save", this.form).then(res => {
        if (res.code === '200') {
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
      window.open("http://localhost:9090/menu/export")
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