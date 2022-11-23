const dropdownBrands = $("#brand");
const dropdownCategory = $("#category");

$(document).ready(function () {
    dropdownBrands.change(function () {
        dropdownCategory.empty();
        /* getCategories() shows the categories of the selected brand */
        getCategories();
    });

    getCategoriesForNewForm();

});

function getCategoriesForNewForm() {
    let catIdField = $("#categoryId");
    let editMode = false;

    if (catIdField.length) {
        editMode = true
    }

    if (!editMode) getCategories();
}

function getCategories() {
    const brandId = dropdownBrands.val();
    const url = brandModuleURL + "/" + brandId + "/categories";

    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategory);
        });
    });
}

function checkUnique(form) {
    const productId = $("#id").val();
    const productName = $("#name").val();

    const csrfValue = $("input[name='_csrf']").val();

    const params = {
        id: productId,
        name: productName,
        _csrf: csrfValue
    };

    $.post(checkUniqueUrl, params, function (response) {
        if (response === "OK") {
            form.submit();
        } else if (response === "DuplicateName") {
            showModalWarning("There is another product with the same name " + productName);
        } else {
            showModalError("Unknown response from server");
        }
    }).fail(function () {
        showModalError("Could not connect to server");
    });

    return false;
}

tinymce.init({
    selector: 'textarea',
    menubar: false,
    plugins: [
        'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
        'anchor', 'searchreplace', 'visualblocks', 'fullscreen',
        'insertdatetime', 'media', 'table', 'code', 'help', 'wordcount'
    ],
    toolbar: 'blocks | bold italic backcolor | ' +
        'alignleft aligncenter alignright alignjustify | ' +
        'bullist numlist indent | removeformat | help',
    toolbar_mode: 'floating'
});
