<template>
    <v-container
            id="regular-tables"
            fluid
            tag="section"
    >
        <v-row justify="space-between">
            <v-col cols="2">
                <v-select v-model="pageSize"
                          :items ="pageSizeOptions"
                          prepend-icon="mdi-format-align-justify"
                          menu-props="auto"
                          hide-details
                          label="pageSize"
                          single-line
                >
                </v-select>
            </v-col>
            <v-col cols="4">
                <v-row no-gutters>
                    <v-col>
                        <v-btn
                                @click="showDeleteModal()"
                                :class="{'d-none': selectedCurriculums.length === 0}"
                                color="success">
                            {{$t('delete.text')}}
                            <v-icon
                                    class="mx-1">
                                mdi-delete
                            </v-icon>
                        </v-btn>
                    </v-col>
                    <v-col>
                        <router-link :to="{ name: 'Curriculum-create'}">
                            <v-btn
                                    color="success">
                                {{$t('curriculum.add')}}
                            </v-btn>
                        </router-link>
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
        <base-material-card
                icon="mdi-calendar-multiple"
                :title="$t('curriculum.list')"
                class="px-5 py-3"
        >
            <v-simple-table>
                <thead>
                <tr>
                    <th>
                        <input class="mr-3" type="checkbox" @click="selectAll" v-model="allSelected"/>
                    </th>
                    <th class="primary--text display-1">
                        {{$t('major')}}
                    </th>
                    <th class="primary--text display-1">
                        {{$t('year')}}
                    </th>
                    <th></th>

                </tr>
                </thead>

                <tbody>
                <tr @click="$router.push({name: 'Curriculum', params: {id: plan.id}})" class="cursor-pointer" v-for="(plan, index) in curriculums" :key="index" >
                    <td><input @click.stop="" type="checkbox" :value=plan.id v-model="selectedCurriculums"/>
                    <td>{{plan.major}}</td>
                    <td>{{plan.year}}</td>
                    <td class="text-right">
                        <v-tooltip open-delay="83" bottom>
                            <template v-slot:activator="{ on, attrs }">
                                <v-icon
                                        @click.stop="$router.push({ name: 'Curriculum-edit', params: {id: plan.id} })"
                                        v-bind="attrs"
                                        v-on="on"
                                        class="mx-1">
                                    mdi-pencil
                                </v-icon>
                            </template>
                            <span>{{$t('edit')}}</span>
                        </v-tooltip>
                        <v-tooltip open-delay="83" bottom>
                            <template v-slot:activator="{ on, attrs }">
                                <v-icon
                                        @click.stop="showDeleteModal([plan.id]);"
                                        v-bind="attrs"
                                        v-on="on"
                                        color="error"
                                        class="mx-1">
                                    mdi-delete
                                </v-icon>
                            </template>
                            <span>{{$t('delete.text')}}</span>
                        </v-tooltip>
                    </td>
                </tr>
                </tbody>
            </v-simple-table>
        </base-material-card>
        <DeleteModal
                disabled ref="delete"
                :number="toDelete ? 1 : selectedCurriculums.length"
                @submit="deleteCurriculum(toDelete ? toDelete : selectedCurriculums)">
        </DeleteModal>
    </v-container>
</template>

<script>
    import { get, del } from '@/helpers/api'

    import DeleteModal from '../common/modal/DeleteModal.vue'

    export default {
      components: {
        DeleteModal
      },

      data () {
        return {
          toDelete: "",
          curriculums: [],
          selectedCurriculums: [],
          allSelected: false,
          pageSize: 10,
          pageSizeOptions: [10, 25, 50, 100],
        }
      },

      methods: {
        getCurriculums() {
          let _this = this;

          get(_this, '/curriculum', '', response=>{
            _this.curriculums = response.data;
          })
        },
        showDeleteModal(val){
          if (val)
            this.toDelete = val;
          this.$refs.delete.showModal();
        },
        deleteCurriculum(val){
          let _this = this;
          let temp = _this.selectedCurriculums;
          if (val !== _this.selectedCurriculums){
            let i = _this.selectedCurriculums.findIndex(x=> x===val[0]);
            if (i !== -1)
              temp.splice(i, 1);
            localStorage.setItem('selectedCurriculums', JSON.stringify(temp));
          }
          val.forEach((x, idx, arr)=> del(_this, '/curriculum/'+x, '',
              ()=> {
                if (idx === arr.length-1){
                  _this.$store.dispatch('setSnackbar', {text: "Successfully deleted"});
                  if (val !== _this.selectedCurriculums){
                    temp = JSON.parse(localStorage.getItem('selectedCurriculums'));
                  }
                  _this.getCurriculums();
                  _this.selectedCurriculums = temp;
                  _this.toDelete = "";
                }
              }, error=>{
            _this.$store.dispatch('setSnackbar', {text: error, color: "error"});
          }));
        },
        selectAll() {
          this.selectedCurriculums = [];
          if (!this.allSelected){
            this.allSelected = true;
            this.curriculums.forEach(x => this.selectedCurriculums.push(x.id))
          } else
            this.allSelected = false
        }
      },

      created() {
        this.getCurriculums();
      },

      watch: {
        selectedCurriculums(val) {
          this.allSelected = val.length === this.curriculums.length
        }
      }
    }
</script>
