<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

const posts = ref([]);

axios.get("/be-api/posts?page=1&size=5")
    .then(response => {
      response.data.content.forEach((p: any) => {
        posts.value.push(p);
      })
    })

</script>

<template>
  <ul>
    <li v-for="(post, idx) in posts" :key="post.id">
      <p class="mb-2">
        {{posts.length - idx}} {{post.title}} {{post.content}}
      </p>
    </li>
  </ul>
</template>
