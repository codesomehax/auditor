# NU Student Audit System


<h3>Building the project</h3>
The project includes a wrapper for Apache Maven, so you can build it without having Maven installed on your machine. 

On a Linux machine run
`./mvnw package `<br/>

On a Windows machine run<br/>
`./mvnw.cmd package` in git bash<br/>
or `.\mvnw.cmd package` in cmd


The output of this task is the `auditor.jar` file in the `target` folder of the **root** project. The jar contains the compiled Java classes as well as the compiled static frontend scripts, resources and assets

<h3>Running the project</h3>

After building the project, you can run the `auditor.jar` inside the `target` directory

`java -jar auditor.jar`

or you can specify the server port (defaults to 8080)

`java -jar -Dserver.port=7777 auditor.jar`


<h3>Database</h3>

By default, the app creates the file of H2 database (`db.mv.db` file in the `target` directory). So the data is persistent after the shutdowns. 

If you want to use in-memory database, then specify the `DB_URL` flag which should be a valid h2 jdbc url.

` java -jar auditor.jar --DB_URL=jdbc:h2:mem:testdb`

--DB_URL=jdbc:h2:mem:testdb
