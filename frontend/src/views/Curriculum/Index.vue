<template>
    <v-container
            fluid
            tag="section">
        <v-row justify="end">
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
    </v-container>
</template>

<script>
import {download, get, post} from '../../helpers/api'
    import RequirementsTable from '@/views/components/curriculum/RequirementsTable'
    export default {
      data () {
        return {
          curriculum: ''
        }
      },

      components: {
        RequirementsTable
      },

      methods: {

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