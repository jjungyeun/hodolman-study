<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
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

const moveToEdit = () => {
  router.push({name: "edit", params: {postId: props.postId}})
}

axios.get(`/be-api/posts/${props.postId}`)
.then((response) => {
  post.value = response.data;
})

</script>
<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>

  <el-button class="mt-5" type="warning" @click="moveToEdit()">수정하기</el-button>
</template>