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
        </v-row>
        <base-material-card
                icon="mdi-calendar"
                :title="$t('curriculum.curriculum')"
                class="px-5 py-3"
        >
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
                    <td class="font-weight-bold">{{totalCredits}}</td>
                    <td></td>
                </tr>
                </tbody>
            </v-simple-table>
        </base-material-card>
    </v-container>
</template>

<script>
    import { get } from '../../helpers/api'
    export default {
      data () {
        return {
          curriculum: ''
        }
      },

      methods: {
        getCurriculum(){
          let _this = this;

          get(_this, '/curriculum/'+ _this.$route.params.id, '', response => {
            _this.curriculum = response.data;
          });
        }
      },
      computed: {
        totalCredits() {
          let sum = 0;
          this.curriculum.requirements.forEach(x => sum+=parseInt(x.credit));
          return sum;
        },
      },
      created() {
        this.getCurriculum()
      }
    }
</script>