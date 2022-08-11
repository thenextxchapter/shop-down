$(document).ready(function () {
    $("input[name='extraImage']").each(function (index) {
        $(this).change(function () {
            if (!checkFileSize(this)) {
                return;
            }
            showExtraImageThumbnail(this, index);
        });
    });
});

function showExtraImageThumbnail(fileInput, index) {
    $(".notEmptyExtraImage").removeClass("no-display").addClass("do-display");
    $(".showImage").removeClass("no-display").addClass("do-display");

    const file = fileInput.files[0];
    const reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);

    addNextExtraImageSection(index + 1);
}

function addNextExtraImageSection(index) {
    let htmlExtraImage = `
        <div class="grid-product-item no-display showImage" id="divExtraImage${index}">
            <div id="extraImageHeader${index}" class="extra-image-header"></div>
            <img id="extraThumbnail${index}" class="extra-thumbnail" alt="Extra image #${index + 1} preview" src="${defaultImageThumbnailSrc}">
        </div>
        <div id="divProductImages" class="grid-product-item gallery-uploader-wrap" style="border: none">
            <label class="uploader-img" style="width: 100%; height: 100%; margin: 0;">
                <input type="file" name="extraImage"
                onchange="showExtraImageThumbnail(this, ${index})"
                 accept="image/png, image/jpeg">
                <svg xmlns="http://www.w3.org/2000/svg" fill="#999" width="32" height="32" viewBox="0 0 24 24">
                    <circle cx="12" cy="12" r="3"/>
                    <path d="M16.83 4L15 2H9L7.17 4H2v16h20V4h-5.17zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5z"/>
                </svg>
                <p class="text-primary">Upload</p>
            </label>
        </div>
    `;

    let htmlRemoveLink = `
        <a class="btn fas fa-trash-alt" title="Remove this image" style="color:#842029;"
        href="javascript:removeExtraImage(${index - 1})"></a>
    `;
    $("#divProductImages").replaceWith(htmlExtraImage);

    $("#extraImageHeader" + (index - 1)).append(htmlRemoveLink);
}

function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}
