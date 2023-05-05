set jv_jar=jars/javaview.jar;jars/jvx.jar;jars/vgpapp.jar;jars\Jama-1.0.3.jar;.
java -cp %jv_jar% -Djava.library.path="dll" -Xmx1024m test.TestMumps 
@pause
