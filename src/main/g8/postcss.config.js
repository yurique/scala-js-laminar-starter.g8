module.exports = (api) => {
  const tailwindcss = require('./tailwind.config')(api)
  const plugins = {
    'postcss-import': {},
    'postcss-nested': {},
    tailwindcss,
    autoprefixer: {}
  }
  if (api.mode === 'production') {
    plugins.cssnano = {}
  }

  return {
    plugins
  }
}
