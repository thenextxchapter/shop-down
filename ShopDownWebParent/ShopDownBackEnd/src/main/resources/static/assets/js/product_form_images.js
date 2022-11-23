let extraImagesCount = 0;

$(document).ready(function () {
    $("input[name='extraImage']").each(function (index) {
        $(this).change(function () {
            extraImagesCount++;

            if (!checkFileSize(this)) {
                return;
            }
            showExtraImageThumbnail(this, index);
        });
    });
});

function showExtraImageThumbnail(fileInput, index) {
    const file = fileInput.files[0];
    const reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);

    if (index >= extraImagesCount - 1) {
        addNextExtraImageSection(index + 1);
    }
}

function addNextExtraImageSection(index) {
    let htmlExtraImage = `
        <div class="grid-product-item" id="divExtraImage${index}">
            <div id="extraImageHeader${index}" class="extra-image-header"></div>
            <div class="gallery-uploader-wrap" style="width: 100%;height: 100%;">
                <label class="uploader-img" style="width: inherit; height: inherit;">
                    <input type="file" name="extraImage" accept="image/png, image/jpeg"
                           class="form-control" style="padding: 0"
                           onchange="showExtraImageThumbnail(this, ${index})"
                    >
                    <img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" width="100%" src="${defaultImageThumbnailSrc}">
                    <input type="hidden" name="extraImage">
                </label>
            </div>
        </div>
    `;

    let htmlRemoveLink = `
        <a class="btn fas fa-trash-alt" title="Remove this image" style="color:#842029;"
        href="javascript:removeExtraImage(${index - 1})"></a>
    `;
    $("#divProductImages").append(htmlExtraImage);

    $("#extraImageHeader" + (index - 1)).append(htmlRemoveLink);

    extraImagesCount++;
}

function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}
