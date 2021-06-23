const path = require('path');
const _ = require('lodash');

const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin')
const CompressionPlugin = require('compression-webpack-plugin');
const scalaVersion = require('./scala-version')

const scalaOutputPath = path.resolve(__dirname, `./modules/frontend/target/scala-${scalaVersion}`);

const devServerHost = '127.0.0.1';
const devBackendPort = 30099;
const devServerPort = 30190;

const devBackend = `http://127.0.0.1:${devBackendPort}`;

const devServer = {
  hot: true,
  firewall: false,
  client: {
    host: devServerHost,
    port: devServerPort,
  },
  injectHot: true,
  injectClient: true,
  transportMode: 'ws',
  public: `http://localhost:${devServerPort}`,
  port: devServerPort,
  host: devServerHost,

  historyApiFallback: {
    index: ''
  },
  proxy: {
    '/api': {
      target: devBackend,
      changeOrigin: true,
      ws: true
    }
  },
};

const common = (mode) => ({
  mode,
  resolve: {
    alias: {
      'frontend-config': (mode === 'production') ?
        path.resolve(__dirname, './modules/frontend-config/prod') :
        path.resolve(__dirname, './modules/frontend-config/dev')
    }
  },
  output: {
    publicPath: '/',
    filename: '[name].[hash].js',
    library: 'app',
    libraryTarget: 'var'
  },
  entry: [
    path.resolve(__dirname, './modules/frontend/src/main/static/stylesheets/main.scss'),
    path.resolve(__dirname, './modules/frontend/src/main/static/stylesheets/main.css')
  ],
  module: {
    rules: [
      {
        test: /\.css$/i,
        use: [MiniCssExtractPlugin.loader, 'css-loader', 'postcss-loader'],
      },
      {
        test: /\.scss$/i,
        use: [MiniCssExtractPlugin.loader, 'css-loader', 'postcss-loader', 'sass-loader'],
      },
      {
        test: /\.(png|jpg|woff|woff2|ttf|eot|svg|txt)$/i,
        type: 'asset/resource'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      filename: 'index.html',
      template: './modules/frontend/src/main/static/html/index.html.ejs',
      minify: false,
      inject: 'head',
    }),
    new CopyWebpackPlugin({
      patterns: [
        {from: './modules/frontend/src/main/static/public', to: ''},
      ]
    })
  ]
})

const dev = {
  devtool: 'cheap-module-source-map',
  entry: [
    path.resolve(__dirname, `${scalaOutputPath}/frontend-fastopt/main.js`),
  ],
  plugins: [
    new MiniCssExtractPlugin()
  ],
};

const prod = {
  entry: [
    path.resolve(__dirname, `${scalaOutputPath}/frontend-opt/main.js`),
  ],
  // devtool: 'source-map',
  optimization: {
    minimize: true,
    minimizer: [new TerserPlugin()],
  },
  plugins: [
    new CleanWebpackPlugin(),
    new MiniCssExtractPlugin({
      filename: '[name].[contenthash].css',
    }),
    new CompressionPlugin({
      test: /\.(js|css|html|svg|json|woff|woff2)$/,
      deleteOriginalAssets: false,
    }),
    new CompressionPlugin({
      test: /\.(js|css|html|svg|woff|woff2)$/,
      filename: '[path][base].br',
      algorithm: 'brotliCompress',
      compressionOptions: {
        // zlib’s `level` option matches Brotli’s `BROTLI_PARAM_QUALITY` option.
        level: 11,
      },
      minRatio: 0.8,
      deleteOriginalAssets: false,
    })
  ]
};


function customizer(objValue, srcValue) {
  if (_.isArray(objValue)) {
    return objValue.concat(srcValue);
  }
}

function getConfig() {
  switch (process.env.npm_lifecycle_event) {
    case 'build':
    case 'build:prod':
      console.log('production build');
      return _.mergeWith({}, common('production'), prod, customizer);
    case 'build:dev':
      console.log('development build');
      return _.mergeWith({}, common('development'), dev, customizer);

    case 'start:prod':
      console.log('production dev server');
      return _.mergeWith({}, common('production'), prod, {
        devServer
      }, customizer);
    case 'start':
    case 'start:dev':
    default:
      console.log('development dev server');
      return _.mergeWith({}, common('development'), dev, {
        devServer
      }, customizer);
  }
}


const config = getConfig()
module.exports = config
