<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

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

</script>
<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>
</template>