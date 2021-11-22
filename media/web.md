# webApp

Install dependencies \
(includes the shared module compilation & installation - see `scripts/preinstall.sh`)

```shell
yarn
```

Start webpack dev server

```shell
yarn start
```

Reinstall the module

```shell
yarn remove RssReader-shared && \
yarn add file:../shared/build/dist
```

> Accessing a feed without cross origin control allowed.
> **Safari**
> Develop > Disable cross-origin restrictions
> **Chrome** (extension)
> https://chrome.google.com/webstore/detail/allow-cors-access-control/lhobafahddgcelffkeicbaginigeejlf