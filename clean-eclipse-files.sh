 find -depth -name target -o -name .project -o -name .classpath -o -name .settings | xargs -I file  rm -rf file
