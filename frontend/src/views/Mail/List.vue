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
      <router-link :to="{ name: 'Mail', query: { action: 'create' } }">
        <v-btn
          color="success">
          Create new
        </v-btn>
      </router-link>
    </v-col>
  </v-row>

    <base-material-card
      icon="mdi-email-multiple-outline"
      title="Mail list"
      class="px-5 py-3"
    >
      <v-simple-table>
        <thead>
          <tr>
            <th class="primary--text display-1">No.</th>
            <th class="primary--text display-1" @click="sort('name')">Name</th>
            <th class="primary--text display-1" @click="sort('text')">Text</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="(mail, i) in sortedMails"
              :key="mail.id">
            <td>{{(i+1)*currentPage}}</td>
            <td>{{mail.name}}</td>
            <td>
              <div v-if="mail.text.length < 80"> {{mail.text}}</div>
              <div v-else> {{mail.text.substring(0, 80)+'...'}}</div>
            </td>
            <td class="text-right">
              <router-link tag="button" :to="{ name: 'Mail', query: { action: 'edit', id: mail.id } }">
                <v-tooltip open-delay="83" bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <v-icon
                      v-bind="attrs"
                      v-on="on"
                      class="mx-1">
                        mdi-email-edit-outline
                    </v-icon>
                  </template>
                  <span>Edit</span>
                </v-tooltip>
              </router-link>
              <v-tooltip open-delay="83" bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-icon
                    @click="deleteMail(mail.id)"
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
  </v-container>
</template>

<script>
export default {
  name: 'MailsList',

  data: () => ({
    currentSort: 'Name',
    currentSortDir: 1,
    pageSize: 5,
    pageSizeOptions: [5, 10, 15, 20, 25, 30],
    currentPage: 1,
    prev: false,
    next: true,
    mails: []
  }),

  computed: {
    sortedMails() {
      return this.mails.slice().sort((a,b) => {
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
      this.next = ((this.currentPage*this.pageSize) < this.mails.length) ? true : false;
      this.prev = (this.currentPage == 1) ? false : true;
    },
    currentPage() {
      this.next = ((this.currentPage*this.pageSize) < this.students.length) ? true : false;
      this.prev = (this.currentPage == 1) ? false : true;
    },
  },

  methods: {
    // get information
    getMails() {
      // TODO: get data from backend
      this.mails = [ 
        {
          id: 1254,
          name: "Keep going!",
          text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        },
        {
          id: 1243,
          name: "Lorem ipsum",
          text: "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?"
        },
      ]
    },
    // sorting
    sort ( col ) {
      if(this.currentSort === col){
        this.currentSortDir = this.currentSortDir === 1 ? -1 : 1;
      }else{
        this.currentSort = col;
      }
    },
    // navigation
    nextPage() {
      if((this.currentPage*this.pageSize) < this.mails.length)
        this.currentPage++;
    },
    prevPage() {
      if(this.currentPage > 1)
        this.currentPage--;
    },
    // remove
    deleteMail( id ) {
      // TODO: send id and command to backend
      var mail = this.mails.find(s => s.id === id )
      this.mails.splice( this.mails.indexOf( mail ), 1 )
    },
  },
  created() {
    this.getMails();
    this.next = ((this.currentPage*this.pageSize) < this.mails.length) ? true : false;
  },
}
</script>