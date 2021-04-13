# NU Student Audit System


<h3>Building the project</h3>
The project includes a wrapper for Apache Maven, so you can build it without having Maven installed on your machine. 

Run 
`./mvnw package ` on a Linux machine
or `./mvnw.cmd package` on a Windows machine

The output of this task is the `auditor.jar` file in the `target` folder of the **root** project. The jar contains the compiled Java classes as well as the compiled static frontend scripts, resources and assets

<h3>Running the project</h3>

After building the project, you can run the `auditor.jar` inside the `target` directory

`java -jar auditor.jar`

or you can specify the server port (defaults to 8080)

`java -jar -Dserver.port=7777 auditor.jar`
