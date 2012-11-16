s4t
===

Simple transcoder with FFmpeg4

mvn install:install-file -Dfile=src/dependentjar/jave-1.0.2.jar -DgroupId=it.sauronsoftware -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar
mvn install:install-file -Dfile=src/dependentjar/jave-1.0.2-src.zip -DgroupId=it.sauronsoftware -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar -Dclassifier=sources

Xuggler 소스 로컬 리포지토리에 등록하기

mvn install:install-file -Dfile=src/dependentjar/xuggler-src.jar -DgroupId=xuggle -DartifactId=xuggle-xuggler -Dversion=5.3 -Dpackaging=jar -Dclassifier=sources