<template>
  <div>
    <v-dialog
        max-width="600px"
    >
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
  </div>
</template>
