<template>
  <base-material-card
    class="v-card--material-chart"
    v-bind="$attrs"
    v-on="$listeners"
  >
    <template v-slot:heading>
      <chartist
        :data="data"
        :event-handlers="eventHandlers"
        :options="options"
        :ratio="ratio"
        :responsive-options="responsiveOptions"
        :type="type"
        style="max-height: 150px;"
      />
    </template>

    <slot
      slot="reveal-actions"
      name="reveal-actions"
    />

    <slot />

    <slot
      slot="actions"
      name="actions"
    />
  </base-material-card>
</template>

<script>
  export default {
    name: 'MaterialChartCard',

    inheritAttrs: false,

    props: {
      data: {
        type: Object,
        default: () => ({}),
      },
      eventHandlers: {
        type: Array,
        default: () => ([]),
      },
      options: {
        type: Object,
        default: () => ({}),
      },
      ratio: {
        type: String,
        default: undefined,
      },
      responsiveOptions: {
        type: Array,
        default: () => ([]),
      },
      type: {
        type: String,
        required: true,
        validator: v => ['Bar', 'Line', 'Pie'].includes(v),
      },
    },
  }
</script>

<style lang="sass">
  .v-card--material-chart
    p
      color: #999

    .v-card--material__heading
      max-height: 185px

      .ct-label
        color: inherit
        opacity: .7
        font-size: 0.975rem
        font-weight: 100

      .ct-grid
        stroke: rgba(255, 255, 255, 0.2)

      //.ct-series-a .ct-point,
      //.ct-series-a .ct-line,
      //.ct-series-a .ct-bar,
      //.ct-series-a .ct-slice-donut
      //    stroke: #3b12f5
      //
      //.ct-series-a .ct-slice-pie,
      //.ct-series-a .ct-area
      //    fill: #3b12f5
</style>

<style lang='scss'>

  .ct-legend {
    position: relative;
    z-index: 10;
    list-style: none;

    li {
      position: relative;
      margin-left: 10px;
      padding-left: 20px;
      margin-bottom: 3px;
      display: inline-block;
      cursor: pointer;
    }

    li:before {
      width: 12px;
      height: 12px;
      position: absolute;
      left: 5px;
      bottom: 5px;
      content: '';
      border-style: solid;
      border-width: 3px;
      border-radius: 2px;
    }

    li.inactive:before {
      background: transparent;
    }

    &.ct-legend-inside {
      position: absolute;
      top: 0;
      right: 0;
    }
  }
</style>
