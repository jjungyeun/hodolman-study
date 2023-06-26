<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {RouterLink, useRouter} from "vue-router";

const posts = ref([]);
const router = useRouter();

axios.get("/be-api/posts")
    .then(response => {
      response.data.content.forEach((p: any) => {
        posts.value.push(p);
      })
    });

const moveToDetail = function () {
  router.push({name: "detail"})
}

</script>

<template>
  <ul>
    <li v-for="(post, idx) in posts" :key="post.id" @click="moveToDetail()">
      <div class="mb-2">
        {{posts.length - idx}} <RouterLink :to="{name: 'detail', params: {postId: post.id}}">{{post.title}}</RouterLink>
      </div>
    </li>
  </ul>
</template>
