<!DOCTYPE html>
<html lang="en">
<head>
    <title>Task results</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }

        p {
            margin: 2px;
        }
        .red {
            color: red;
            font-weight: 600;
            font-size: 20px;
        }
        .yellow {
            color: orange;
            font-weight: 600;
            font-size: 20px;
        }
        .green {
            color: green;
            font-weight: 600;
            font-size: 20px;
        }
    </style>
</head>

<body>
<table border="1">
    <tr>
        <th>Name</th>
         <#list tasks as task>
            <th>
                ${task.tag}
            </th>
        </#list>
    </tr>
    <#list tasksResults as student, tasks>
        <tr>
            <th>${student.fullname}</th>
            <#list tasks as task, result>
                <td>
                    <p>Downloaded successfully: <b>${result.downloaded?string}</b></p>
                    <p>Compiled successfully: <b>${result.compiled?string}</b></p>
                    <p>Javadoc generated: <b>${result.javadoc?string}</b></p>

                    <p>Checkstyle warning count:
                        <#if result.checkstyleWarnings == 0>
                          <span class="green">${result.checkstyleWarnings}</span>
                        <#elseif result.checkstyleWarnings <= 10 && 1 <= result.checkstyleWarnings >
                          <span class="yellow">${result.checkstyleWarnings}</span>
                        <#else>
                          <span class="red">${result.checkstyleWarnings}</span>
                        </#if>
                    </p>

                    <p>Tests Passed/Skipped/Failed: <span class="green">${result.testsPassed}</span>/
                                                    <span class="yellow">${result.testsSkipped}</span>/
                                                    <span class="red">${result.testsFailed}</span></p>
                    <p>Deadlines & Mark:
                        <#if result.softDeadline>
                           <span class="green"><b>soft</b></span>
                        <#else>
                            <span class="red"><b>soft</b></span>
                        </#if>
                        <#if result.hardDeadline>
                            <span class="green"><b>hard</b></span>
                        <#else>
                            <span class="red"><b>hard</b></span>
                        </#if>
                        <b>${result.mark}</b>
                    </p>
                </td>
            </#list>
        </tr>
    </#list>
</table>

</body>
</html>
