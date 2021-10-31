<template>
  <div>
    <v-simple-table>
      <thead>
        <tr>
          <th class="font-weight-bold display-2">{{curriculum.major}}</th>
          <th class="font-weight-bold display-2">{{curriculum.year}}</th>
        </tr>
        <tr>
          <th class="primary--text display-1">
            Course name
          </th>
          <th class="primary--text display-1">
            Course type
          </th>
          <th class="primary--text display-1">
            Credits
          </th>
          <th class="primary--text display-1">
            Patterns
          </th>
          <th class="primary--text display-1">
            Antipatterns
          </th>
          <th class="primary--text display-1">
            Semester
          </th>
        </tr>
      </thead>

      <tbody>
        <Requirement
          v-for="(requirement, index) in curriculum.requirements" :key="index"
          v-bind:requirement="requirement"
        />
          <td class="font-weight-bold">Total Credits</td>
          <td class="font-weight-bold">{{totalCredits}}</td>
          <td></td>
      </tbody>
    </v-simple-table>
  </div>
</template>

<script>
  import Requirement from "@/views/components/curriculum/Requirement";
  export default {
    components: {Requirement},
    computed: {
      totalCredits() {
        let sum = 0;
        this.curriculum.requirements.forEach(x => sum+=parseInt(x.credit));
        return sum;
      },
    },
    props: [
      'curriculum'
    ]
  }
</script>