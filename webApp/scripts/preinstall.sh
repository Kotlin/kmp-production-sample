echo 'NPM preinstall hook'

pwd
cd ../

./gradlew shared:clean
./gradlew shared:compileProductionExecutableKotlinJs

# shellcheck disable=SC2181
if [ $? != 0 ]; then
  echo 'Exit with Gradle error'
  exit 1;
fi;

./gradlew shared:jsPublicPackageJson

# shellcheck disable=SC2181
if [ $? != 0 ]; then
  echo 'Exit with Gradle error'
  exit 1;
fi;

# make the distro directory (were we cook up our JS module)

mkdir -p ./shared/build/dist

# copy module content to distro dir so NPM can install from there
# then copy package.json to executable folder

cp -r ./shared/build/compileSync/main/productionExecutable/kotlin/* ./shared/build/dist/
cp ./shared/build/tmp/jsPublicPackageJson/package.json ./shared/build/dist/package.json

# rename files to index for easier imports

mv ./shared/build/dist/RssReader-shared.js ./shared/build/dist/index.js
mv ./shared/build/dist/RssReader-shared.js.map ./shared/build/dist/index.js.map
mv ./shared/build/dist/RssReader-shared.d.ts ./shared/build/dist/index.d.ts
