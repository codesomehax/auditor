module.exports = {
    "transpileDependencies": [
        "vuetify"
    ],
    pluginOptions: {
        i18n: {
            locale: 'en',
            fallbackLocale: 'en',
            localeDir: 'locales',
            enableInSFC: false,
        },
    },

    // https://cli.vuejs.org/config/#devserver-proxy
    /* devServer: {
        port: 3000,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                ws: true,
                changeOrigin: true
            }
        }
    } */
}