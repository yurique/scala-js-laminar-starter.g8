# Running

## Front-end

Install npm dependencies:

```
npm install
```

Build the front-end:

```
sbt frontend/fastLinkJS
```

Start the webpack dev server:

```
npm start
```

## Back-end

Start sbt:

```
sbt
```

Within sbt start the back-end app:

```
sbt> backend/reStart
```

## Open 

Open http://localhost:30190/ in the browser.

# Developing

To make sbt re-compile the front-end on code changes:

```
sbt ~frontend/fastLinkJS
```

To re-compile and restart the back-end on code changes:

```
sbt> ~backend/reStart
```

# Prod build

Build an optimized js for the front-end:

```
sbt frontend/fullLinkJS
```

Run the npm:

```
npm run build:prod
```

The front end assets will be generated into the `dist` folder.

## Testing

testing with jest is currently broken
