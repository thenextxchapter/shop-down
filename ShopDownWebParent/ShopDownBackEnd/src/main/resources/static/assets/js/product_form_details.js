function addNextDetailSection() {
    let allDivDetails = $("[id^='divDetail']");
    let divDetailsCount = allDivDetails.length;

    let htmlDetailSection = `
        <div class="form-inline row g-3 mt-2" id="divDetail${divDetailsCount}">
            <div class="col-md-6">
                <label>Name: </label>
                <input type="text" class="form-control" name="detailNames" maxlength="255">
            </div>
    
            <div class="col-md-5">
                <label>Value: </label>
                <input type="text" class="form-control" name="detailValues" maxlength="255">
            </div>
        </div>
    `;

    $("#divProductDetails").append(htmlDetailSection);

    let previousDivDetailSection = allDivDetails.last();
    let previousDivDetailID = previousDivDetailSection.attr("id");

    let htmlRemoveLink = `
        <a class="col-md-1 btn fas fa-1x fa-trash-alt" 
        title="Remove this detail" 
        href="javascript:removeDetailSectionById('${previousDivDetailID}')"
        style="color:#842029; font-size: 23px; align-self: end"></a>
    `;

    previousDivDetailSection.append(htmlRemoveLink);

    $("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
    $("#" + id).remove();
}
