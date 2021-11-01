<template>
  <tr>
    <td>{{requirement.name}}</td>
    <td>{{requirement.type}}</td>
    <td>{{requirement.credit}}</td>
    <td>{{requirement.patterns}}</td>
    <td>{{requirement.antipatterns}}</td>
    <td>{{requirement.semester}}</td>
  </tr>
</template>

<script>
  export default {
    props: {
      requirement: {
        type: Object,
        required: true
      }
    }
  }
</script>