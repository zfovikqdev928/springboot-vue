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
      <el-popconfirm cancel-button-text='取消'
                     class="ml-5"
                     confirm-button-text='确定'
                     icon="el-icon-info"
                     icon-color="red"
                     title="您确定批量删除这些数据吗?"
                     @confirm="delBatch">
        <el-button slot="reference" class="ml-5" type="danger">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
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
      <el-table-column align="center" label="ID" prop="id" sortable width="80"></el-table-column>
      <el-table-column align="center" label="角色名称" prop="name" sortable></el-table-column>
      <el-table-column align="center" label="描述" prop="description"></el-table-column>
      <el-table-column align="center" label="操作" width="280">

        <template slot-scope="scope">
          <el-button slot="reference" type="info" @click="selectMenu(scope.row.id)">分配菜单<i class="el-icon-menu"></i>
          </el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <!-- 删除提示框-->
          <el-popconfirm cancel-button-text='取消'
                         class="ml-5"
                         confirm-button-text='确定'
                         icon="el-icon-info"
                         icon-color="red"
                         title="您确定删除吗?"
                         @confirm="handleDel(scope.row.id)"
          >
            <el-button slot="reference" type="danger">删除<i class="el-icon-delete"></i></el-button>
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

    <!--   弹出对话框     -->
    <div>
      <!--   新增弹框    -->
      <el-dialog :visible.sync="dialogFormVisible" title="角色信息" width="30%">
        <el-form :model="form" label-width="80px" size="small">
          <el-form-item label="角色名称">
            <el-input v-model="form.name" placeholder="请输入角色名称"></el-input>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" autocomplete="off" placeholder="角色描述"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>

      <!--   分配菜单弹框  -->
      <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
        <el-tree
            :data="menuData"
            show-checkbox
            node-key="id"
            :default-expanded-keys="[1]"
            :default-checked-keys="[4]"
            @check-change="handleCheckChange">
        </el-tree>
        <div slot="footer" class="dialog-footer">
          <el-button @click="menuDialogVis = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>
    </div>


  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      menuDialogVis: false,

      // 分配角色
      menuData: [{
        id: 1,
        label: '主页',
        children: []
      }
        , {
          id: 2,
          label: '系统管理',
          children: [{
            id: 3,
            label: '用户管理',
            children: []
          },
            {
              id: 4,
              label: '角色管理',
              children: []
            },
            {
              id: 5,
              label: '菜单管理',
              children: []
            },
            {
              id: 6,
              label: '文件管理',
              children: []
            }]
        },
      ]
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
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
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
      this.name = ""
      this.load()
    },
    // 新增用户时弹窗
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    // 编辑用户
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    // 删除用户
    handleDel(id) {
      this.request.delete("/role/del/" + id).then(res => {
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
      this.request.post("/role/del/batch", ids).then(res => {
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
      this.request.post("/role/save", this.form).then(res => {
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

    // 分配角色
    selectMenu(roleId) {
      this.menuDialogVis = true

    },

    handleCheckChange(data, checked, indeterminate) {
      console.log(data, checked, indeterminate);
    },
  }
}
</script>