javac -classpath jars/javaview.jar:jars/jvx.jar:jars/Jama-1.0.3.jar:. workshop/*.java 
javac -classpath jars/javaview.jar:jars/jvx.jar:. menu/*.java
java -classpath jars/javaview.jar:jars/jvx.jar:jars/vgpapp.jar:jars/Jama-1.0.3.jar:. -Xmx1024m javaview model="models/simp_rabbit.obj" codebase=. archive.dev=show