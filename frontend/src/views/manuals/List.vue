<template>
  <v-container>

    <h1>Frequently asked questions: </h1>
    <ul>
      <li><router-link :to="{ name: 'Curriculum manual' }">How to create a curriculum?</router-link></li>
      <li><router-link :to="{ name: 'Student manual' }">How to upload a student transcript?</router-link></li>
    </ul>
  </v-container>
</template>

<script>
export default {
  name: "List"
}
</script>

<style scoped>

</style>