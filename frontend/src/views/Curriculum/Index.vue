<template>
    <v-container
            fluid
            tag="section">
        <v-row justify="end">
            <v-col cols="1">
                <v-btn
                        @click="addFile = true"
                >
                    Add plan
                </v-btn>
            </v-col>
            <v-col cols="1">
                <v-btn
                        @click="$router.push({ name: 'Curriculum-edit', params: {id: curriculum.id} })"
                        color="success"
                >
                    {{$t('edit')}}
                </v-btn>
            </v-col>
            <v-col cols="1">
                <v-btn
                    @click="exportCurriculum"
                    color="success"
                >
                  Export
                </v-btn>
            </v-col>
        </v-row>
        <base-material-card
                icon="mdi-calendar"
                :title="$t('curriculum.curriculum')"
                class="px-5 py-3"
        >
          <RequirementsTable
            v-bind:curriculum="curriculum"
          />
        </base-material-card>
        <v-dialog
            v-model="addFile"
            max-width="600">
            <v-card>
                <v-card-title class="display-2">
                    Upload Plan
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
                        accept=".docx"
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
import {download, get, post} from '../../helpers/api'
    import RequirementsTable from '@/views/components/curriculum/RequirementsTable'
    export default {
      data () {
        return {
          curriculum: '',
          curriculumId: '',
          addFile: false,
          files: []
        }
      },

      components: {
        RequirementsTable
      },

      methods: {

        submitFile() {
          let _this = this;
          let formData = new FormData();
          formData.append('file', this.files);
          _this.curriculumId = this.$route.params.id;

          post(_this, '/curriculum/'+_this.curriculumId + '/plan', formData, response=> {
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

          get(_this, '/curriculum/'+ _this.$route.params.id, '', response => {
            _this.curriculum = response.data;
          });
        },

        exportCurriculum() {

          let _this = this;

          download(_this, '/curriculum/'+ _this.$route.params.id + '/export', '', response => {
          }, error => {
            _this.$store.dispatch('setSnackbar', {
              text: error,
              color: "error"
            })
          });
        }

      },
      created() {
        this.getCurriculum()
      }
    }
</script>