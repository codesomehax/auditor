<template>
    <v-app>
        <dashboard-core-app-bar />

        <dashboard-core-drawer />

        <dashboard-core-view />

        <v-snackbar v-for="(snackbar, index) in snackbars" :key="index" :color="snackbar.color" v-model="snackbar.showing" right bottom>{{snackbar.text}}</v-snackbar>

    </v-app>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    name: 'DashboardIndex',

    components: {
      DashboardCoreAppBar: () => import('./components/core/AppBar'),
      DashboardCoreDrawer: () => import('./components/core/Drawer'),
      DashboardCoreView: () => import('./components/core/View'),
    },

    data: () => ({
      expandOnHover: false,
    }),
    computed: {
      ...mapState(['snackbars'])
    }
  }
</script>
