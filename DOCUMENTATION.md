# WIP: Backend-parser Documentation

<h3>Curriculum Excel file rules</h3>
<h5>Requirement format</h5>

* Every requirement should belong to a category of requirements. Keyword `Category: ` signifies the beginning of a category. All requirements listed under a category are supposed to belong to it.  
* First column contains the label for a requirement. 
* Second column specifies the number of required credits of the course.
* Parser will try to get the requirement from the first column by splitting the text by a hyphen and looking for the first part
  * In the following example the parser will split by a hyphen that will result in (MATH 161) and (Calculus I) and searching for the MATH 161 in the student's transcript
  
    | Category: Math        |   |
    |-----------------------|---|
    | MATH 161 - Calculus I | 4 |
    
* The third column specifies pattern for matching the code of a completed course against this requirement.
    * In its simplest form, the pattern is a course code. 
        * Example: MATH 161.
    * Specific characters in the pattern can be replaced by a wildcard character "?". This character matches any other single character.
        * Example: M?TH 161 matches MATH 161, MBTH 161, MCTH 161 and etc.
    * Symbol "*" is a wildcard pattern. It matches any course code. Note, however, that it is not equivalent to arbitrary number of "?".
        * Example: pattern "*" matches code "MATH 161", but pattern "M\*" does not.
    * It is also enough to type a prefix of a course code.
        * Example: The pattern "MAT" is equivalent to "MAT? ???".
    * Parser also supports ranges of course codes. In the numerical part of a pattern specify lower and upper bounds separated by the hyphen.
        * Example: MATH 161-163 matches three courses with codes in the set {MATH 161, MATH 162, MATH 163}.
    *  A set of separated by comma patterns can be provided for a single requirement. In this case, the requirement is satisfied by any course matching at least one of the patterns.
        * Example: MATH 161, MATH 162, MATH 163 is equivalent to MATH 161-163
    
* The fourth column specifies antipatterns of a requirement
    * The course cannot satisfy a requirement if it matches one of its antipatterns
        * Example:
        
        | Category: Major       |   |             |                  |   |
        |-----------------------|---|-------------|------------------|---|
        | Intermediate elective | 6 | ANT 200-499 | ANT 201, ANT 301 |   |
    
        This requirement can be satisfied by completion of any ANT course of the level in the range between 200 and 499 except ANT 201 and ANT 301 
   
* The end of the grouped courses should be a first row entry with terminating text. Default is  `Total`

