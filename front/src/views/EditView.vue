<script setup lang="ts">

import {ref} from "vue";
import axios from 'axios';
import {useRouter} from "vue-router";
const router = useRouter();

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true
  }
});

const post = ref({
  id: 0,
  title: "",
  content: ""
});

axios.get(`/be-api/posts/${props.postId}`)
    .then((response) => {
      post.value = response.data;
    })

const edit = function () {
  axios.patch(`/be-api/posts/${props.postId}`, {
    title: post.value.title,
    content: post.value.content
  }).then(() => {
    router.replace({name: "detail", params: {postId: this.postId}})
  })
}

</script>

<template>
  <div>
    <el-input v-model="post.title" type="text" placeholder="제목을 입력해주세요">{{post.title}}</el-input>
  </div>
  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15" placeholder="내용을 입력해주세요">{{post.content}}</el-input>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="edit()">글 수정 완료</el-button>
  </div>
</template>

<style>

</style>