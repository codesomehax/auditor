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
                  <div class="text--primary">
                    <span class="font-weight-light">cGPA:</span> {{student.gpa}}
                  </div>
                  <div class="text--primary">
                    <span class="font-weight-light">Credits earned:</span> {{student.creditsEarned}}
                  </div>
                </v-card-text>
              </base-material-card>
            </v-col>
          </v-row>
        </v-expansion-panel-content>
      </v-expansion-panel>
      <v-expansion-panel>
        <v-expansion-panel-header><span class="text-h4">Comparison</span></v-expansion-panel-header>
        <v-expansion-panel-content>
          <v-row>
            <v-col>
              <base-material-chart-card
                  elevation="0"
                  :data="data"
                  :options="chartOptions"
                  color="success"
                  type="Line"
              >
                <div ref="legend" class="card-title font-weight-light mt-2 ml-2">
                  Comparison
                </div>
              </base-material-chart-card>
            </v-col>
          </v-row>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-container>
</template>

<script>
  import {get} from "../../helpers/api";

  export default {
    props: ['id'],
    data () {
      return {
          data: {
              labels: [],
              series: []
          },
          students: [],
          panel: [0, 1],
          chartOptions: {
              high: 4,
              low: 0,
              lineSmooth: this.$chartist.Interpolation.none({
                fillHoles: false
              }),
              plugins: [
                  this.$chartist.plugins.tooltip({ anchorToPoint: true }),
                  this.$chartist.plugins.ctAxisTitle({
                      axisY: {
                          axisTitle: 'GPA',
                          axisClass: 'gpa-axis-title',
                          textAnchor: 'middle',
                          offset: {
                              x: 0,
                              y: 20
                          },
                          flipTitle: true
                      }
                  }),
                  this.$chartist.plugins.legend({className: "legend", position: this.$refs.legend})
              ]
            }
          }
    },

    methods: {
      getStudents(){
          let _this = this;
          get(_this, '/transcript/students/'+_this.id, {}, response=>{
              _this.students = response.data;
              _this.getGraph();
          });
      },
      getGraph(){
        let _this = this;
        get(_this, '/transcript/studentsGraph/'+_this.id, {}, response=>{
          _this.data.labels = response.data.terms;
          _this.data.series = [];
          for (let j in response.data.graph){
            let student = response.data.graph[j];
            let student_series = {name: j, data: []};
            response.data.terms.forEach(term => {

              let i = student.find(semester => {return semester["termName"] == term});

              if (i){
                student_series.data.push({x: i.termName, y: i.termGpa});
              }else{
                student_series.data.push({x: term, y: null})
              }
            });
            _this.data.series.push(student_series);
          }
        });
      }
    },
    computed: {
      // chartData(){
      //     return {
      //         labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'],
      //         series: [
      //         [12, 9, 7, 8, 5],
      //         [2, 1, 3.5, 7, 3],
      //         [1, 3, 4, 5, 6]
      //     ]
      //     }
      // },
    },
    created() {
      this.getStudents();
      // this.getGraph();
    }
  }
</script>

<style>
    .chartist-tooltip
    {
        background: #999999;
    }

    .chartist-tooltip:before
    {
        border-top-color: #999999;
    }

    .ct-point
    {
        stroke-width: 8px !important;
    }

    .gpa-axis-title
    {
        fill: #c6e6c7;
    }

</style>
