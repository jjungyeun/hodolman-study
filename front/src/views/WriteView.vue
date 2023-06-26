<script setup lang="ts">

import {ref} from "vue";
import axios from 'axios';
import {useRouter} from "vue-router";
const router = useRouter();

const title = ref("")
const content = ref("")

const write = function () {
  axios.post("/be-api/posts", {
    title: title.value,
    content: content.value
  }).then(() => {
    router.replace({name: "home"})
  })
}

</script>

<template>
  <div>
    <el-input v-model="title" type="text" placeholder="제목을 입력해주세요" />
  </div>
  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15" placeholder="내용을 입력해주세요"></el-input>
  </div>

  <div class="mt-2 d-flex justify-content-end">
    <el-button type="primary" @click="write()">글 작성 완료</el-button>
  </div>
</template>

<style>

</style>