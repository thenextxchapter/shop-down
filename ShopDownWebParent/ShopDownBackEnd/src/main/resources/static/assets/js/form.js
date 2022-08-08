$(document).ready(function () {
    $("#notEmptyFileImage").addClass("no-display");

    $("#buttonCancel").on("click", function () {
        window.location = moduleURL;
    });

    $("#fileImage").change(function () {
        fileSize = this.files[0].size;

        if (fileSize > MAX_FILE_SIZE) {
            this.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + " bytes!");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

function showImageThumbnail(fileInput) {
    $("#emptyFileImage").addClass("no-display");
    $("#notEmptyFileImage").removeClass("no-display").addClass("do-display");
    const file = fileInput.files[0];
    const reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal("show");
}

function showModalError(message) {
    showModalDialog("Error", message)
}

function showModalWarning(message) {
    showModalDialog("Warning", message)
}
