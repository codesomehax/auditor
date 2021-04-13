<template>
  <v-container
    id="regular-tables"
    fluid
    tag="section"
  >
  <v-row justify="space-between">
    <v-col cols="6" md="2">
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
    <v-spacer/>
    <v-col cols="6" md="2">
        <v-btn
          v-if="selectedStudents.length > 0"
          color="success"
          @click="del=true">
          Delete
          <v-icon
            class="mx-1">
            mdi-delete
          </v-icon>
        </v-btn>
    </v-col>
    <v-col cols="6" md="2">
        <v-btn
          color="success"
          @click="addFiles=true">
          Add transcript
        </v-btn>
    </v-col>
  </v-row>

    <base-material-card
      icon="mdi-account-group"
      title="Student list"
      class="px-5 py-3"
    >
      <v-simple-table>
        <thead>
          <tr>
            <th><input class="mr-3" type="checkbox" @click="selectAll" v-model="allSelected"/></th>
            <th class="primary--text display-1" @click="sort('studentId')">ID</th>
            <th class="primary--text display-1" @click="sort('name')">Name</th>
            <th class="primary--text display-1" @click="sort('gpa')">cGPA</th>
            <th class="primary--text display-1" @click="sort('major')">Major</th>
            <th class="primary--text display-1" @click="sort('admissionSemester')">Year enrolled</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <tr class="cursor-pointer" @click="goToAudit(student.id)" v-for="(student) in sortedStudents"
              :key="student.id">
            <td><input @click.stop="" type="checkbox" :value=student.id v-model="selectedStudents"/></td>
            <td>{{student.id}}</td>
            <td>{{student.name}}</td>
            <td>{{student.gpa}}</td>
            <td>{{student.major}}</td>
            <td>{{student.admissionSemester}}</td>
            <td class="text-right">
              <v-tooltip open-delay="83" bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-icon
                    @click.stop="selectedStudents=[student.id], del=true"
                    v-bind="attrs"
                    v-on="on"
                    color="error"
                    class="mx-1">
                    mdi-delete
                  </v-icon>
                </template>
                <span>Delete</span>
              </v-tooltip>
            </td>
          </tr>
        </tbody>
      </v-simple-table>
      <v-row>
        <v-col>
        <v-btn color="success" v-if='prev' @click="prevPage">Previous</v-btn> 
        </v-col>
        <v-col
          cols="1"
        >
        <v-btn color="success" v-if='next' @click="nextPage">Next</v-btn>
        </v-col>
      </v-row>
    </base-material-card>
    <v-row>
      <v-col
        cols="1">
        <v-btn
          v-if="selectedStudents.length>0"
          color="success"
          @click="send=true, showMails()">
          Send mails
        </v-btn>
      </v-col>
      <v-spacer/>
      <v-col
        cols="1">
        <router-link v-if="selectedStudents.length>1"
          :to="{ name: 'Compare Students', query: { id: selectedStudents } }" >
          <v-btn  color="success">
              Compare
          </v-btn>
        </router-link>
      </v-col>
    </v-row>
    <v-dialog
      v-model="addFiles"
      max-width="600">
      <v-card>
        <v-card-title class="primary--text display-2">
          Add transcripts
        <v-spacer />
      
        <v-icon
          aria-label="Close"
          @click="addFiles = false"
        >
          mdi-close
        </v-icon>
        </v-card-title>

        <v-card-text>
          <v-file-input
            v-model="files"
            multiple
            show-size
            counter
            placeholder="Select transcripts"
            prepend-icon="mdi-paperclip"
            outlined
            accept=".pdf"
          >
          </v-file-input>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="submitFiles()"
          >
            Ok
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="addFiles = false, files = []"
          >
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog
      v-model="send"
      max-width="600">
      <v-card>
        <v-card-title class="primary--text display-2">
          Mails list

        <v-spacer />
          
        <v-icon
          aria-label="Close"
          @click="send=false, mails=''"
        >
          mdi-close
        </v-icon>
        </v-card-title>
        <v-card-text v-model="mails" id="tocopy">
          {{mails}}
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="copyMails()"
          >
            Copy to clipboard
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="send = false, mails = ''"
          >
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog
      v-model="del"
      max-width="600"
    >
      <v-card>
        <v-card-title class="warning--text display-2">
          Warning!

          <v-spacer />

          <v-icon
            aria-label="Close"
            @click="del = false"
          >
            mdi-close
          </v-icon>
        </v-card-title>

        <v-card-text class="text-center">
          Are you sure you want to delete?
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="warning"
            text
            @click="del = false, selectedStudents = []"
          >
            Cancel
          </v-btn>

          <v-btn
            color="warning"
            text
            @click="removeStudents(selectedStudents)"
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
  data: () => ({
    currentSort: 'Name',
    currentSortDir: 1,
    pageSize: 10,
    pageSizeOptions: [2, 5, 10, 15, 20, 25, 30],
    currentPage: 1,
    prev: false,
    next: true,
    send: false,
    files: [],
    addFiles: false,
    mails: '',
    selectedStudents: [],
    allSelected: false,
    students: [],
    del: ''
  }),
  
  computed: {
    sortedStudents() {
      return this.students.slice().sort((a,b) => {
        if(a[this.currentSort] < b[this.currentSort]) return -1 * this.currentSortDir;
        if(a[this.currentSort] > b[this.currentSort]) return this.currentSortDir;
        return 0;
      }).filter((row, index) => {
        let start = (this.currentPage-1)*this.pageSize;
        let end = this.currentPage*this.pageSize;
        if(index >= start && index < end) return true;
      });
    },
  },

  watch: {
    pageSize() {
      this.next = ((this.currentPage*this.pageSize) < this.students.length) ? true : false;
      this.prev = (this.currentPage == 1) ? false : true;
    },
    currentPage() {
      this.next = ((this.currentPage*this.pageSize) < this.students.length) ? true : false;
      this.prev = (this.currentPage == 1) ? false : true;
    },
    selectedStudents(val) {
      this.allSelected = val.length === this.students.length
    }
  },

  methods: {
    // get information
    getStudents() {
      get(this, '/transcript/all', '', response=>{
        this.students = response.data;
      })
    },
    // sorting
    sort ( col ) {
      if(this.currentSort === col){
        this.currentSortDir = -1 * this.currentSortDir;
      }else{
        this.currentSort = col;
      }
    },
    // navigation
    nextPage() {
      if((this.currentPage*this.pageSize) < this.students.length)
        this.currentPage++;
    },
    prevPage() {
      if(this.currentPage > 1)
        this.currentPage--;
    },
    // selection
    selectAll( ) {
      this.selectedStudents = []
      if( !this.allSelected ) {
        this.allSelected = true
        this.students.forEach(x => this.selectedStudents.push(x.id))
      } else {
        this.allSelected = false
      }
    },
    // transcript addition
    submitFiles() {
      if ( this.files.length != 0 ) {
        for(var i = 0; i < this.files.length; i ++ ) {
          let formData = new FormData();
          formData.append('file', this.files[i]);

          post(this, '/transcript', formData, () => {
            this.getStudents();
            this.$store.dispatch('setSnackbar', {
              text: "Transcript uploaded",
            })
          }, error =>{
            this.$store.dispatch('setSnackbar', {
              text: error,
              color: "error"
            })
          }, {
            'Content-Type': 'multipart/form-data'
          });
        }
        this.addFiles = false;
        this.files = [];
      } else {
        console.log("There are no files.");
      }
    },
    // mails
    showMails( ) {
      for( let i = 0; i < this.selectedStudents.length; i ++ ) {
        this.mails += this.students.find(s => s.id === this.selectedStudents[ i ] ).mail;
        this.mails += ', '
      }
    },
    copyMails() {
      navigator.clipboard.writeText(this.mails);
    },
    // remove
    removeStudents( ids ) {
      for(let i = 0; i < ids.length; i ++) {
        del(this, '/transcript/'+ids[i], '',  () => {
          this.getStudents()
          this.$store.dispatch('setSnackbar', {
            text: "Transcripts deleted",
          })
        }, error => {
          this.$store.dispatch('setSnackbar', {
            text: error,
            color: "error"
          })
        });
      }
      this.del = false;
    },
    // router
    goToAudit( id ){
      this.$router.push({ name: 'Student Audit', params: { id: id } })
    }
  },

  created() {
    this.getStudents();
    this.next = ((this.currentPage*this.pageSize) < this.students.length) ? true : false;
  },
}
</script>