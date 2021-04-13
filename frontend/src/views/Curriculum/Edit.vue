<template>
    <v-container
            fluid
            tag="section"
    >
        <v-row justify="center">
            <v-col cols="12">
                <base-material-card>
                    <template v-slot:heading>
                        <div class="display-2 font-weight-light">
                            {{action === 'edit' ? $t("edit") : $t("create") }} curriculum
                        </div>

                        <div class="subtitle-1 font-weight-light">
                            {{action === 'create' ? 'Fill the form' : form.curriculum.major + ' ' + form.curriculum.year}}
                        </div>
                    </template>
                    <v-tabs  v-model="tab">
                        <v-tab :class="{'d-none': action === 'edit'}">Create curriculum</v-tab>
                        <v-tab :disabled="stage<1">Edit content</v-tab>
                        <v-tab :disabled="stage<2">Finish</v-tab>
                    </v-tabs>
                    <v-tabs-items v-model="tab">
                        <v-tab-item :class="{'d-none': action === 'edit'}">
                                <v-form>
                                    <v-container class="py-0" style="margin-left: 0; margin-right: 0; max-width: none">
                                        <v-row>
                                            <v-col
                                                    cols="12"
                                                    md="6"
                                            >
                                                <v-text-field
                                                        :disabled="stage !== 0"
                                                        v-model="form.curriculum.major"
                                                        label="Major"
                                                />
                                            </v-col>
                                            <v-col
                                                    cols="12"
                                                    md="6"
                                            >
                                                <v-text-field
                                                        :disabled="stage !== 0"
                                                        label="Year"
                                                        v-mask="'####'"
                                                        v-model="form.curriculum.year"
                                                        v-on:keyup.enter="createCurriculum()"
                                                />
                                            </v-col>
                                        </v-row>
                                        <v-row>
                                            <v-col>
                                                <v-btn
                                                        color="success"
                                                        class="mr-0 float-right"
                                                        :disabled="form.curriculum.major.length === 0 ||
                                             form.curriculum.year.length === 0"
                                                        :class="{ 'd-none': stage!==0 }"
                                                        @click="createCurriculum()"
                                                >
                                                    Create
                                                </v-btn>
                                            </v-col>
                                        </v-row>
                                    </v-container>
                                </v-form>
                        </v-tab-item>
                        <v-tab-item>
                            <v-card>
                                <v-tabs vertical>
                                    <v-tab>Upload</v-tab>
                                    <v-tab>Manual</v-tab>

                                    <v-tab-item>
                                        <v-container>
                                            <v-row justify="center">
                                                <v-col>
                                                    <v-btn
                                                            class="mr-0"
                                                            color="success"
                                                            @click="addFile = true"
                                                    >
                                                        Upload file
                                                    </v-btn>
                                                </v-col>
                                            </v-row>
                                        </v-container>
                                    </v-tab-item>
                                    <v-tab-item>
                                        <v-container class="py-0">
                                            <v-row
                                                    justify="end"
                                                    :class="{'d-none': stage !==1 }">
                                                <v-col cols="3"><v-btn color="white">Total credits: {{totalCredits}}</v-btn></v-col>
                                                <v-col cols="2">
                                                <v-btn
                                                        color="success"
                                                        class="mr-0 float-right"
                                                        @click="addCourse"
                                                >
                                                    Add slot
                                                </v-btn>
                                            </v-col>
                                                <v-col cols="2">
                                                <v-btn
                                                        color="success"
                                                        class="mr-0 float-right"
                                                >
                                                    Save
                                                </v-btn>
                                            </v-col>
                                            </v-row>
                                        </v-container>
                                        <v-simple-table fixed-header height="600px">
                                            <thead>
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
                                                <th></th>

                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr v-for="(course, index) in form.curriculum.requirements" :key="index" >
                                                <td><v-text-field
                                                        v-model="course.name"
                                                        label="Course name"
                                                /></td>
                                                <td><v-text-field
                                                        v-model="course.type.name"
                                                        label="Course type"
                                                /></td>
                                                <td><v-text-field
                                                        v-model="course.credit"
                                                        label="Credits"
                                                        type="number"
                                                /></td>
                                                <td class="text-right">
                                                    <v-tooltip open-delay="83" bottom>
                                                        <template v-slot:activator="{ on, attrs }">
                                                            <v-icon
                                                                    @click="removeCourse(index)"
                                                                    v-bind="attrs"
                                                                    v-on="on"
                                                                    class="mx-1">
                                                                mdi-close
                                                            </v-icon>
                                                        </template>
                                                        <span>Remove</span>
                                                    </v-tooltip>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </v-simple-table>
                                    </v-tab-item>

                                </v-tabs>
                            </v-card>
                        </v-tab-item>
                        <v-tab-item>
                            <v-card flat>
                                <v-row>
                                    <v-col>
                                        <v-card-text>
                                            Curriculum created successfully
                                        </v-card-text>
                                    </v-col>
                                    <v-col>
                                        <router-link :to="{ name: 'Curriculums' }">
                                            <v-btn class="success">
                                                Finish
                                            </v-btn>
                                        </router-link>
                                    </v-col>
                                </v-row>
                            </v-card>
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
                                </tr>
                                </thead>

                                <tbody>
                                <tr v-for="(course, index) in curriculum.requirements" :key="index" >
                                    <td>{{course.name}}</td>
                                    <td>{{course.type.name}}</td>
                                    <td>{{course.credit}}</td>
                                </tr>
                                <tr>
                                    <td class="font-weight-bold">Total Credits</td>
                                    <td class="font-weight-bold">{{totalCreditsParsed}}</td>
                                </tr>
                                </tbody>
                            </v-simple-table>
                        </v-tab-item>
                    </v-tabs-items>
                </base-material-card>
            </v-col>
        </v-row>
        <v-dialog
                v-model="addFile"
                max-width="600">
            <v-card>
                <v-card-title class="display-2">
                    Upload Curriculum
                    <v-spacer />

                    <v-icon
                            aria-label="Close"
                            @click="addFile = false"
                    >
                        mdi-close
                    </v-icon>
                </v-card-title>

                <v-card-text>
                    <v-file-input
                            v-model="files"
                            show-size
                            counter
                            placeholder="Select file"
                            prepend-icon="mdi-paperclip"
                            outlined
                            accept=".xlsx"
                    >
                        <template v-slot:selection="{ index, text }">
                            <v-chip
                                    v-if="index < 2"
                                    dark
                                    label
                                    small
                            >
                                {{ text }}
                            </v-chip>
                        </template>
                    </v-file-input>
                </v-card-text>

                <v-divider></v-divider>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                            color="primary"
                            text
                            @click="submitFile()"
                    >
                        Ok
                    </v-btn>
                    <v-btn
                            color="primary"
                            text
                            @click="addFile = false"
                    >
                        Close
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>

<script>
    import { get, post } from '../../helpers/api'

    export default {

      data () {
        return {
          action: 'create',
          tab: null,
          tab2: null,
          curriculum: '',
          curriculumId: '',
          form: {
            curriculum: {
              year: '',
              major: '',
              requirements: []
            }
          },
          stage: 0,
          addFile: false,
          files: []
        }
      },

      methods: {
        createCurriculum(){
          let _this = this;

          let data = {
            major: _this.form.curriculum.major,
            year: _this.form.curriculum.year
          };

          post(_this, '/curriculum', data, response => {
            _this.curriculumId = response.data.id;
            _this.tab = 1;
            _this.stage = 1;
            _this.$store.dispatch('setSnackbar', {
              text: "Curriculum created",
            })
          }, error => {
            _this.$store.dispatch('setSnackbar', {
              text: error,
              color: "error"
            })
          });

        },

        addCourse(){
          let c = {
            name: '',
            type: {
              name:''
            },
            credit: ''
          };
          this.form.curriculum.requirements.push(c);
        },

        removeCourse(index){
          this.form.curriculum.requirements.splice(index,1);
        },

        scrollToEnd(){
          window.scrollTo(0, document.body.scrollHeight || document.documentElement.scrollHeight);
        },

        submitFile(){
          let _this = this;
          let formData = new FormData();
          formData.append('file', this.files);

          post(_this, '/curriculum/'+_this.curriculumId, formData, response=> {
            _this.curriculum = response.data;
            _this.stage = 2;
            _this.tab = 2;
            _this.addFile = false;
            _this.$store.dispatch('setSnackbar', {text: "Curriculum successfully filled"})
          }, error=>{
            _this.$store.dispatch('setSnackbar', {
              text: error,
              color: "error"
            })
          }, {
                'Content-Type': 'multipart/form-data'
              });

        },

        getCurriculum(){
          let _this = this;
          _this.curriculumId = this.$route.params.id;

          get(_this, '/curriculum/'+ _this.curriculumId, '', response => {
            _this.form.curriculum = response.data;
            _this.stage = 1;
            _this.tab = 1;
            _this.action = 'edit';
          });
        }
      },

      computed: {
        totalCredits() {
          let sum = 0;
          this.form.curriculum.requirements.forEach(x => sum+=parseInt(x.credit));
          return sum;
        },
        totalCreditsParsed() {
          let sum = 0;
          if (this.curriculum)
            this.curriculum.requirements.forEach(x => sum+=parseInt(x.credit));
          return sum;
        },
      },

      created() {
        if (this.$route.path.includes('edit')){
          this.getCurriculum();
        }
      }
    }
</script>