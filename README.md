# Backend-parser

# WIP: Backend-parser Documentation


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


<h3>Curriculum Excel file rules</h3>
<h5>Requirement format</h5>

* Each group of courses must be started with `Category: `
* First column is the course requirement
* Second column is the number of required credits of the course
* Parser will try to get the requirement from the first column by splitting the text by a hyphen and looking for the first part
    * In the following example the parser will split by a hyphen that will result in (MATH 161) and (Calculus I) and searching for the MATH 161 in the student's transcript

      | Category: Math        |   |
          |-----------------------|---|
      | MATH 161 - Calculus I | 4 |

* If you want to specify the patterns for any requirement thus ignoring the first column text, then use third column by listing the course prefixes separating with comma
    * If you want to specify the course level (100, 200, 300 etc) just add the leading number of the level.
      For the following curriculum slot the parser will search for any PHIL 1** and HST 2** course with a minimal weight of 3 credits (for example, HST 202 or PHIL 150).

  | Category: Humanities |   |                 |
    |----------------------|---|-----------------|
  | SHSS/COM             | 3 | PHIL 1,  HST 2  |
    * Use wildcard for any course. For the following slot the parser will map any course with 3 credits

  | Category: Free       |   |                 |
     |----------------------|---|-----------------|
  | Free elective        | 3 | *               |

* The end of the grouped courses should be a first row entry with terminating text. Default is  `Total`

