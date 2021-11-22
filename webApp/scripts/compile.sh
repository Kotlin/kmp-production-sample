#!/bin/sh

yarn remove RssReader-shared
yarn add file:../shared/build/dist
yarn start
