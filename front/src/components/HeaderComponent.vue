<script>
export default {
  name: 'HeaderComponent'
}
</script>

<template>
  <el-header>
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">Write</el-menu-item>
    </el-menu>
  </el-header>
</template>


<style scoped>
.header {
  padding: 0;
  height: 60px;
}
</style>