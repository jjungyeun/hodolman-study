<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
    <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/write">글 작성</RouterLink>
    </nav>

  <RouterView />
</template>

<style>
</style>
