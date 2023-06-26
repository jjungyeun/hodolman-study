<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {RouterLink, useRouter} from "vue-router";

const router = useRouter();

const posts = ref([]);
const totalCnt = ref(0);

axios.get("/be-api/posts")
    .then(response => {
      response.data.content.forEach((p: any) => {
        posts.value.push(p);
      });
      totalCnt.value = response.data.totalElements;
    });

const moveToDetail = function () {
  router.push({name: "detail"})
}

</script>

<template>
  <div>전체 글 <span class="total-cnt">{{totalCnt}}</span></div>

  <ul>
    <li v-for="(post, idx) in posts" :key="post.id" @click="moveToDetail()">
      <div class="mb-2 title">
        {{posts.length - idx}} <RouterLink class="post-title" :to="{name: 'detail', params: {postId: post.id}}">{{post.title}}</RouterLink>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 1rem;

  li {
    margin-bottom: 1.3rem;
    font-size: 1.1rem;
    color: #727272;

    .post-title {
      margin-left: 1.5rem;
      text-decoration: none;
      color: #2a2a2a;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.total-cnt {
  color: #e00011;
  font-size: 1.1rem;
}
</style>
