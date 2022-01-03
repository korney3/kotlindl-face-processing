<html>
<head>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<h2>File Upload
    <br> Age+Gender Prediction
    <br> Landmarks Preview</h2>


<form method="POST" enctype="multipart/form-data" id="fileUploadForm" class="uploader">

    <input type="file" id="uploadfile"
           name="uploadfile" accept="image/*" placeholder="Upload File"/>
    <button type="submit" class="btn btn-default" id="btnSubmit">Upload</button>

</form>
<div class="row">
    <div class="column">
        <#if initialimageurl??>
            <figure id="left_image">
                <img alt="your-image" id="your-image" src=${initialimageurl} width="200">
                <figcaption><i>Original image</i>
                </figcaption>
            </figure>
        </#if>
    </div>
    <div class="column">
        <#if processedimageurl??>
            <figure id="right_image">
                <img alt="your-image" id="your-image" src=${processedimageurl} width="200">
                <figcaption><i>Image with landmarks
                        <#if ageprediction??>
                            <br> Age ${ageprediction}
                        </#if>
                        <#if genderprediction??>
                            <br> Gender ${genderprediction}
                        </#if>
                    </i>
                </figcaption>
            </figure>
        </#if>
    </div>
</div>
<a href="/files">Files</a>
</body>
</html>