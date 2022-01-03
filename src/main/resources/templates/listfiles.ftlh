<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Upload files</title>
    <link rel="stylesheet" href="styles-list.css">


</head>
<body>
<div class="container">
    <div class="row justify-content-center align-items-center">
        <div class="col-md-7 table-responsive">
            <h2>Uploaded Files</h2>
            <table id="customerTable" class="table">
                <thead>
                <tr>
                    <th>No</th>
                    <th>Filename</th>
                    <th>Download</th>
                </tr>
                </thead>
                <tbody>
                <#if files??>
                    <#list files as file>
                        <tr>
                            <td>${file?index}</td>
                            <td>${file.filename}</td>
                            <td><a href="${file.url}">Link</a></td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
            <div>
                <a href="/" class="btn btn-light btn-block" role="button">Back to Upload Form</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>