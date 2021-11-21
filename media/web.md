# Web

Here are the instructions to follow to start working with the web version of the RSS feed reader.

```shell
# cd to react app
cd ./webApp/app

# install deps (npm i -g yarn if required)
yarn

# start WDS (Webpack Dev Server)
start
```

Reinstall the module

```shell
cd ./webApp/app

yarn remove RssReader-webApp && \
yarn add file:../build/dist && \
yarn start
```

> Note:
> When accessing a feed without cross origin control allowed you must handle it through a proxy or temporary disable CORS on the browser.
