const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 7090
  },
  chainWebpack: config =>{
    config.plugin('html')
        .tap(args => {
          args[0].title = "光华教材征订系统";
          return args;
        })
  }
})
