<template>
  <div>
    <div style="padding: 10px 0">
      <el-upload :on-success="handleFileUploadSuccess" :show-file-list="false"
                 action="http://localhost:9090/file/upload" style="display: inline-block">
        <el-button class="ml-5" type="primary">上传<i class="el-icon-top"></i></el-button>
      </el-upload>
      <!--批量删除提示框-->
      <el-popconfirm cancel-button-text='取消'
                     class="ml-5"
                     confirm-button-text='确定'
                     icon="el-icon-info"
                     icon-color="red"
                     title="您确定批量删除这些数据吗?"
                     @confirm="delBatch">
        <el-button slot="reference" class="ml-5" type="danger">批量删除<i class="el-icon-remove-outline"></i>
        </el-button>
      </el-popconfirm>


      <el-table :data="tableData"
                :header-cell-style="{background:'#eef1f6',color:'#606266',margin: 10}"
                border stripe:true
                @selection-change="handleSelectionChange">
        <!--     多选框     -->
        <el-table-column align="center"
                         type="selection"
                         width="55">
        </el-table-column>
        <el-table-column align="center" label="ID" prop="id" sortable width="90"></el-table-column>
        <el-table-column align="center" label="文件名称" prop="name" sortable></el-table-column>
        <el-table-column align="center" label="文件类型" prop="type"></el-table-column>
        <el-table-column align="center" label="文件大小（KB）" prop="size"></el-table-column>

        <el-table-column align="center" label="下载链接">
          <template slot-scope="scope">
            <el-button type="primary" @click="downloadFile(scope.row.url)">下载</el-button>
          </template>
        </el-table-column>

        <el-table-column align="center" label="是否启用">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.enable"
                       active-color="#13ce66"
                       inactive-color="#ff4949"
                       @change="changeEnable(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">

          <template slot-scope="scope">
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
    </div>
    <!--分页    -->
    <div style="padding: 10px 0">
      <el-pagination
          :current-page="pageNum"
          :page-size=pageSize
          :page-sizes="[10, 15,20]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "File",

  created() {
    this.load()
  },
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0
    }
  },


  methods: {
    // axios 请求分页查询
    load: function () {
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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
    // 删除用户
    handleDel(id) {
      this.request.delete("/file/del/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
          this.load()
        }
      })
      this.load()
    },

    // 批量删除用户
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id); // [{},{}] => [1,2,3...]
      this.request.post("/file/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })

    },
    // 多选框
    handleSelectionChange(data) {
      this.multipleSelection = data
    },


    // 上传文件
    handleFileUploadSuccess() {
      this.$message.success("上传成功")
      this.load()
    },

    // 下载
    downloadFile(url) {
      window.open(url)
    },

    // 启用 | 关闭

    changeEnable(row) {
      this.request.post("/file/save", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
          this.load()
        }
      })

    },
  }
}
</script>

<style scoped>

</style>