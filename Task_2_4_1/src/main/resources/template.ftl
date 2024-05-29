<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Task results</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 8px;
            text-align: center;
        }
        .student {
            border: 3px solid black;
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

        .warn {
            background-color: #ff9248;
        }
    </style>
</head>

<body>
<table border="1px solid black">
    <tr>
        <th rowspan="2">Name</th>
        <th rowspan="2">Total Mark</th>
        <th rowspan="2">Task tag</th>
        <th rowspan="2">Downloaded</th>
        <th rowspan="2">Compiled</th>
        <th rowspan="2">Javadoc</th>
        <th rowspan="2">Checkstyle</th>
        <th colspan="3">Tests</th>
        <th colspan="2">Deadlines</th>
        <th rowspan="2">Mark</th>
    </tr>
    <tr>
        <th>Passed</th>
        <th>Skipped</th>
        <th>Failed</th>
        <th>Soft</th>
        <th>Hard</th>
    </tr>
    <#list tasksResults as student, tasks>
        <tr class="student">
            <th rowspan="${tasks?size + 1}">${student.fullname}</th>
            <#assign key_list = studentsMarks?keys/>
            <#assign value_list = studentsMarks?values/>
            <#assign seq_index = key_list?seq_index_of(student) />
            <#assign key_value = value_list[seq_index]/>
            <th rowspan="${tasks?size + 1}"><b>${key_value}</b></th>
            <#list tasks as task, result>
                <tr>
                    <th>${task.tag}</th>
                    <td>${result.downloaded?string('&#10004;', '&#10060;')}</td>
                    <td>${result.compiled?string('&#10004;', '&#10060;')}</td>
                    <td>${result.javadoc?string('&#10004;', '&#10060;')}</td>
                    <td>
                        <#if result.checkstyleWarnings == 0>
                          <span class="green">${result.checkstyleWarnings}</span>
                        <#elseif result.checkstyleWarnings <= 10 && 1 <= result.checkstyleWarnings >
                          <span class="yellow">${result.checkstyleWarnings}</span>
                        <#else>
                          <span class="red">${result.checkstyleWarnings}</span>
                        </#if>
                    </td>
                    <td class="green">${result.testsPassed}</td>
                    <td class="yellow">${result.testsSkipped}</td>
                    <td class="red">${result.testsFailed}</td>

                    <td>${result.softDeadline?string('&#10004;', '&#10060;')}</td>
                    <td>${result.hardDeadline?string('&#10004;', '&#10060;')}</td>

                    <td><b>${result.mark}</b></td>
                </tr>
            </#list>
        </tr>
    </#list>
</table>

</body>
</html>
