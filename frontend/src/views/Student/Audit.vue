<template>
  <v-container
    id="dashboard"
    fluid
    tag="section"
  >
    <v-row class="text-right">
      <v-spacer></v-spacer>
      <v-col cols="6" md="2">
          <v-btn
            color="success"
            @click="downloadAudit( )"
          >
            Download
            <v-icon>
              mdi-download
            </v-icon>
          </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        cols="12"
        sm="6"
      >
        <base-material-card
          color="info"
          icon="mdi-account-details"
          title="Student information"
        >
          <v-card-text >
            <div class="display-2 font-weight-light">
              {{student.name}}
            </div>

            <div class="subtitle-1 font-weight-light">
              {{student.id}}
            </div>
            
            <div class="text--primary font-weight-light">
              {{student.major}}
            </div>

            <div class="text--primary font-weight-light">
              {{student.admissionSemester}}
            </div>
          </v-card-text>
        </base-material-card>
      </v-col>
      <v-col
        cols="12"
        sm="6"
      >
        <base-material-card
          color="info"
          class="px-5 py-3"
          icon="mdi-book-education"
          title="Academic standing"
        >
          <v-card-text>
            <v-simple-table dense>
              <tbody>
                <tr>
                  <td>Cumulative GPA</td>
                  <td>{{student.gpa}}</td>
                </tr>
                <tr>
                  <td>Credits Enrolled</td>
                  <td>{{student.creditsEnrolled}}</td>
                </tr>
                <tr>
                  <td>Credits Earned</td>
                  <td>{{student.creditsEarned}}</td>
                </tr>
              </tbody>
            </v-simple-table>
          </v-card-text>
        </base-material-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        cols="12"
      >
        <base-material-chart-card
          :data="semesterGPAchart.data"
          :options="semesterGPAchart.options"
          color="success"
          hover-reveal
          type="Line"
        >

          <h4 class="card-title font-weight-light mt-2 ml-2">
            Average GPA over terms
          </h4>

          <p class="d-inline-flex font-weight-light ml-2 mt-1">
            
          </p>
        </base-material-chart-card>
      </v-col>
    </v-row>
    <v-row>
      <v-select v-model="selectedCurriculum"
        :items ="curriculums"
        item-value= "id"
        prepend-icon="mdi-format-align-justify"
        menu-props="auto"
        hide-details
        label="Select a curriculum to build a report"
        single-line
      >
        <template slot="selection" slot-scope="data">
          {{ data.item.major }} {{ data.item.year }}
        </template>
        <template slot="item" slot-scope="data">
          {{ data.item.major }} {{ data.item.year }}
        </template>
      </v-select>
      <v-btn
          color="success"
          @click="buildReport()">
        Create
      </v-btn>
    </v-row>
    <base-material-card
      v-if="tableInfo.completeRequirements && tableInfo.completeRequirements.length > 0"
      icon="mdi-text-box-check"
      title="Audit"
      class="px-5 py-3"
    >
      <v-simple-table>
        <thead>
          <tr>
            <th><input class="mr-3" type="checkbox" @click="selectAll" v-model="allSelected"
                :disabled = "unmappingDisabled"/></th>
            <th class="primary--text display-1">Required Course</th>
            <th class="primary--text display-1">Credits</th>
            <th class="primary--text display-1">Taken</th>
            <th class="primary--text display-1">Credits</th>
            <th class="primary--text display-1">Grades</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="course in tableInfo.completeRequirements"
              :key="course.id"
              @click.stop="allRowCheckbox(map2unmap, course.requirement.id)">
              <td><input type="checkbox" :value=course.requirement.id v-model="map2unmap"
                  :disabled = "unmappingDisabled"/></td>
              <td>
                {{course.requirement.name}}
              </td>
              <td>
                {{course.requirement.credit}}
              </td>
              <td>
                <div v-if="course.course.code === course.requirement.patterns">
                  <v-icon
                    color="success"
                    class="mx-1">
                    mdi-checkbox-marked-circle-outline
                  </v-icon>
                </div>
                <div v-else-if="course.course.code">
                  {{course.course.code}}
                </div>
              </td>
              <td>
                {{course.course.credits}}
              </td>
              <td>
                {{course.course.gradePoint}}
              </td>
          </tr>
        </tbody>
      </v-simple-table>
    </base-material-card>
    <v-row>
      <v-col
        cols="12"
        md="6"
      >
        <base-material-card
          v-if="tableInfo.unmappedRequirements && tableInfo.unmappedRequirements.length > 0"
          icon="mdi-text-box-remove"
          title="Unmapped requirements"
          class="px-5 py-3"
        >
          <v-simple-table>
            <thead>
              <tr>
                <th></th>
                <th class="primary--text display-1">Required Course</th>
                <th class="primary--text display-1">Credits</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="req in tableInfo.unmappedRequirements"
                  :key="req"
                  @click.stop="allRowCheckbox(unmappedReq, req)">
                  <td><input type="checkbox" :value=req v-model="unmappedReq"
                      :disabled = "mappingReqDisabled"/></td>
                  <td>
                    {{req.name}}
                  </td>
                  <td>
                    {{req.credit}}
                  </td>
              </tr>
            </tbody>
          </v-simple-table>
        </base-material-card>
      </v-col>
      <v-col
        cols="12"
        md="6"
      >
        <base-material-card
          v-if="tableInfo.unmappedCourses && tableInfo.unmappedCourses.length > 0"
          icon="mdi-text-box-plus"
          title="Unmapped courses"
          class="px-5 py-3"
        >
          <v-simple-table>
            <thead>
              <tr>
                <th></th>
                <th class="primary--text display-1">Taken Course</th>
                <th class="primary--text display-1">Credits</th>
                <th class="primary--text display-1">Grades</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="course in tableInfo.unmappedCourses"
                  :key="course"
                  @click.stop="allRowCheckbox(unmappedCourse, course)">
                  <td><input type="checkbox" :value=course v-model="unmappedCourse"
                      :disabled = "mappingCourseDisabled"/></td>
                  <td>
                    {{course.code}}
                  </td>
                  <td>
                    {{course.credits}}
                  </td>
                  <td>
                    {{course.gradePoint}}
                  </td>
              </tr>
            </tbody>
          </v-simple-table>
        </base-material-card>
      </v-col>
    </v-row>
    <v-row class="text-right">
      <v-spacer></v-spacer>
      <v-col cols="3" md="2">
          <v-btn
            v-if="map2unmap.length > 0 || (unmappedCourse.length !== 0 && unmappedReq.length !== 0)"
            color="error"
            @click="dialog = true"
          >
            Map/Unmap
            <v-icon>
              mdi-link
            </v-icon>
          </v-btn>
      </v-col>
      <v-col cols="3" md="2">
          <v-btn
            v-if="tableInfo !== ''"
            color="error"
            @click="del=true, dialog = true"
          >
            Clear
            <v-icon>
              mdi-delete
            </v-icon>
          </v-btn>
      </v-col>
    </v-row>
    <v-dialog
      v-model="dialog"
      max-width="600"
    >
      <v-card>
        <v-card-title class="warning--text display-2">
          Warning!

          <v-spacer />

          <v-icon
            aria-label="Close"
            @click="dialog = false, del = false"
          >
            mdi-close
          </v-icon>
        </v-card-title>

        <v-card-text class="text-center">
          {{del ? 'Are you sure you want to delete?' : (map2unmap.length > 0 ? 'Are you sure you want to unmap the selected row(s)?' : (unmappedReq.length !== 0 && unmappedCourse.length !== 0 ? 'Are you sure you want to map the selected courses to the selected requirements?' : 'Nothing selected or incorrect selection!'))}}
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="warning"
            text
            @click="dialog = false, del = false, map2unmap = [], unmappedCourse = [], unmappedReq = []"
          >
            Cancel
          </v-btn>

          <v-btn
            color="warning"
            text
            @click="del === true ? removeAudit() : mapUnmap(), dialog=false, del=false"
          >
            Yes
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
  import { get, post, del } from '../../helpers/api'

  export default {
    name: 'StudentAudit',
    props: [ 'id' ],

    data () {
      return {
        taken: 0,
        earned: 0,
        student: {},
        semesterGPAchart: {},
        tableInfo: '',
        curriculums: [],
        selectedCurriculum: "",
        map2unmap: [],
        unmappingDisabled: false,
        allSelected: false,
        unmappedCourse: [],
        mappingCourseDisabled: false,
        unmappedReq: [],
        mappingReqDisabled: false,
        dialog: false,
        del: false
      }
    },

    watch: {
      map2unmap(val) {
        this.allSelected = val.length === this.tableInfo.completeRequirements.length;
        this.mappingCourseDisabled = this.map2unmap.length > 0;
        this.mappingReqDisabled = this.map2unmap.length > 0;
      },
      unmappedReq() {
        this.unmappingDisabled = this.unmappedReq.length > 0;
        this.mappingCourseDisabled = this.unmappedCourse.length > this.unmappedReq.length + 1;
        this.mappingReqDisabled = this.unmappedCourse.length + 1 < this.unmappedReq.length;
      },
      unmappedCourse() {
        this.unmappingDisabled = this.unmappedCourse.length > 0;
        this.mappingCourseDisabled = this.unmappedCourse.length > this.unmappedReq.length + 1;
        this.mappingReqDisabled = this.unmappedCourse.length + 1 < this.unmappedReq.length;
      }
    },
    
    methods: {
      getCurriculums() {
        get(this, '/curriculum', '', response=>{
          this.curriculums = response.data;
        })
      },
      getInfo() {
        get(this, '/transcript/student/' + this.$route.params.id, '', response=>{
          this.student = response.data;
        })
      },
      getSemesterChart() {
        this.semesterGPAchart = {
          data: {
            labels: [],
            series: [[]],
          },
          options: {
            lineSmooth: this.$chartist.Interpolation.simple({
              fillHoles: true
            }),
            low: 0,
            high: 4.5,
            chartPadding: {
              top: 0,
              right: 0,
              bottom: 0,
              left: 0,
            },
          },
        }

        get(this, '/transcript/student/' + this.$route.params.id, '', response=>{
          var chartInfo = response.data.studentTerms;
          chartInfo.forEach(x => this.semesterGPAchart.data.labels.push(x.name))
          chartInfo.forEach(x => this.semesterGPAchart.data.series[0].push(x.termGpa))
        })
      },
      getReport() {
        get(this, '/report/'+ this.$route.params.id, '', response => {
          if(response.data.data === true) {
            this.tableInfo = response.data.content;
          }
        });
      },
      buildReport( ) {
        let data = {
          curriculumId: this.selectedCurriculum,
          studentId: this.student.id
        }

        post(this, '/report', data, response => {
          this.tableInfo = response.data;
          this.$store.dispatch('setSnackbar', {text: "Success"})
        }, error => {
          this.$store.dispatch('setSnackbar', {text: error, color: "error"});
        });
      },
      selectAll( ) {
        this.map2unmap = []
        if( !this.allSelected ) {
          this.allSelected = true
          this.tableInfo.completeRequirements.forEach(x => this.map2unmap.push(x.requirement.id))
        } else {
          this.allSelected = false
        }
      },
      mapUnmap( ) {
        if(this.unmappedReq.length > 0 && this.unmappedCourse.length > 0) {
          for(var i = 0; i < this.unmappedCourse.length; i ++) {
            if(this.unmappedReq[i].credit <= this.unmappedCourse[i].credits) {
              let query = 'requirementId='+this.unmappedReq[i].id+'&courseId='+this.unmappedCourse[i].id;
              console.log(query)
              post(this, '/report/' + this.student.id + '/mapRequirement?'+query, '',
                  () => {
                    this.getReport();
                    this.$store.dispatch('setSnackbar', {text: "Success"});
                  }, error => {
                    this.$store.dispatch('setSnackbar', {text: error, color: "error"});
                  });
            }
          }
        } else {
          this.map2unmap.forEach(x => post(this, '/report/' + this.student.id + '/detachRequirement?requirementId='+x,
            '', () => {
              this.getReport();
              this.$store.dispatch('setSnackbar', {text: "Success"});
            }, error => {
              this.$store.dispatch('setSnackbar', {text: error, color: "error"});
            }));
        }
        this.unmappedReq = [];
        this.unmappedCourse = [];
        this.map2unmap = []
      },
      allRowCheckbox(arr, obj) {
        if((arr === this.map2unmap && !this.unmappingDisabled) ||
           (arr === this.unmappedCourse && !this.mappingCourseDisabled) ||
           (arr === this.unmappedReq && !this.mappingReqDisabled)) {
          if(arr.includes(obj)) {
            arr = arr.splice(arr.indexOf(obj),1);
          } else {
            arr.push(obj);
          }
        }
      },
      downloadAudit( ) {
        window.print()
      },
      removeAudit() {
        del(this, '/report/'+this.$route.params.id, '',  () => { 
          this.tableInfo = '';
          this.$store.dispatch('setSnackbar', {text: "Success"});
        }, error => {
          this.$store.dispatch('setSnackbar', {text: error, color: "error"});
        });
      }
    },

    created() {
      this.getInfo()
      this.getCurriculums()
      this.getSemesterChart()
      this.getReport()
    },
  }
</script>
