@echo off
set jv_jar=jars/javaview.jar;jars/jvx.jar;jars/vgpapp.jar;jars\Jama-1.0.3.jar;.
javaw -cp %jv_jar% -Djava.library.path="dll" -Xmx1024m javaview model="models/simp_rabbit.obj" codebase=. archive.dev=show %*