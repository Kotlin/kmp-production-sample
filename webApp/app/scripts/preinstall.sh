echo 'NPM preinstall hook'
echo $(pwd)

# run tasks for building lib and generating TS definitions

cd ../../
./gradlew webApp:compileProductionExecutableKotlinJs
./gradlew webApp:publicPackageJson

# make the distro directory

mkdir -p ./webApp/build/dist

# copy module content to distro dir so NPM can install from there
# then copy package.json to executable folder

cp -r ./webApp/build/compileSync/main/productionExecutable/kotlin/* ./webApp/build/dist/
cp ./webApp/build/tmp/publicPackageJson/package.json ./webApp/build/dist/package.json

# rename files to index for easier imports

mv ./webApp/build/dist/RssReader-webApp.js ./webApp/build/dist/index.js
mv ./webApp/build/dist/RssReader-webApp.js.map ./webApp/build/dist/index.js.map
mv ./webApp/build/dist/RssReader-webApp.d.ts ./webApp/build/dist/index.d.ts
