<template>
    <div class="text-center">
        <v-dialog
        v-model="show"
        max-width="350">
            <v-card>
                <v-card-title class="headline red--text">
                    {{$t('warning')}}
                </v-card-title>
                <v-card-text>
                    {{$t('delete.warning')}} {{number}} {{$t('delete.items')}}?
                </v-card-text>
                <v-divider></v-divider>
                <v-card-actions class="flex justify-space-between">
                    <v-btn color="blue darken-1" @click="show = false">{{$t('no')}}</v-btn>
                    <v-btn color="blue darken-1 mr-0" @click="submit(true)">{{$t('yes')}}</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>

    export default {
      props: ['number'],
      name: 'DeleteModal',

      data(){
        return {
          show: false,
        }
      },

      methods: {
        submit (val) {
          this.$emit('submit', val);
          this.closeModal();
        },
        showModal () {
          this.show = true;
        },
        closeModal () {
          this.show = false;
        }
      }
    }
</script>