--run maven install before compile project
mvn install:install-file -DgroupId=com.MeiYe -DartifactId=meiye-core-encode -Dversion=1.0 -Dpackaging=jar -Dfile=shiro-core-1.2.4.jar