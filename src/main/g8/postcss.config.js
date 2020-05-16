module.exports = (ctx) => {
  return {
    plugins: [
      require('postcss-import')({}),
      ctx.webpack.mode === 'production' ?
        require('tailwindcss')('./tailwind.prod.config.js') :
        require('tailwindcss')('./tailwind.dev.config.js'),
      require('postcss-nested')({}),
      require('autoprefixer')({}),
      ctx.webpack.mode === 'production' ?
        require('cssnano')() :
        false
    ]
  };
}
