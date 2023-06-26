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
  <h2 class="title">{{post.title}}</h2>
  <div class="content">{{post.content}}</div>

  <div class="mt-5 d-flex justify-content-end">
    <el-button type="text" @click="moveToEdit()">수정하기</el-button>
  </div>
</template>

<style scoped lang="scss">
h2 {
  margin-bottom: 0;
}

.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
}

.content {
  font-size: 0.95rem;
  margin-top: 12px;
  color: #727272;
  word-break: break-all;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>