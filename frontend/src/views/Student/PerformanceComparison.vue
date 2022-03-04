<template>
  <v-container>
    <v-expansion-panels v-model="panel" multiple>
      <v-expansion-panel>
        <v-expansion-panel-header>
          <span class="text-h4">
            Students
          </span>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <v-row>
            <v-col
                cols="6"
                v-for="(student, index) in students" :key="index"
            >
              <base-material-card
                  color="info"
                  icon="mdi-account-details"
                  title="Student information"
              >
                <v-card-text>
                  <div class="display-2 font-weight-light">
                    {{student.name}}
                  </div>
                  <div class="display-2 font-weight-light">
                    {{student.id}}
                  </div>
                  <div class="text--primary font-weight-light">
                    {{student.major}}
                  </div>
                  <div class="text--primary font-weight-light">
                    Admission semester: {{student.admissionSemester}}
                  </div>
                  <div class="text--primary font-weight-light">
                    Current semester: {{student.currentSemester}}
                  </div>
                  <div class="text--primary">
                    <span class="font-weight-light">cGPA:</span> {{student.gpa}}
                  </div>
                  <div class="text--primary">
                    <span class="font-weight-light">Credits earned:</span> {{student.creditsEarnedTotal}}
                  </div>
                  <div class="text--primary">
                    <span class="font-weight-light">Courses failed:</span> {{reports[index].failedCourses.length}}
                  </div>
                  <div class="text--primary">
                    <span class="font-weight-light">Unmet degree requirements:</span> {{reports[index].unmappedRequirements.length}}
                  </div>
                  <router-link :to="'/student/' + student.id">
                    Details
                  </router-link>
                </v-card-text>
              </base-material-card>
            </v-col>
          </v-row>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-container>
</template>

<script>
import {get} from "@/helpers/api";

export default {
  name: "ProblemComparison",
  props: ['ids'],

  data() {
    return {

      students: [],
      reports: []
    }
  },

  methods: {

    getTranscripts() {
      get(this, '/transcript/students/'+ this.ids, {}, response=> {

        this.students = response.data;
      });
    },

    getReports() {
      get(this, '/report/batch/'+ this.ids, {}, response=> {

        this.reports = response.data;
      });


    }

  },

  created() {

    this.getTranscripts();
    this.getReports();
  }
}
</script>

<style scoped>

</style>