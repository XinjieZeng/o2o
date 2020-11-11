$(function() {
    var listUrl = '/o2o/shopadmin/getproductlistbyshop';
    var statusUrl = '/o2o/shopadmin/modifyproduct';
    getList();
    /**
     * get product list of the shop
     *
     * @returns
     */
    function getList() {
        $.getJSON(listUrl, function(data) {
            if (data.success) {
                var productList = data.productList;
                var tempHtml =
                    '<table class="ui celled striped table">' +
                    '<thead>' +
                    '<tr><th>Product Name</th>' +
                    '<th>Priority</th>' +
                    '<th>Operation</th>' +
                    '</tr></thead>' +
                    '<tbody>';

                productList.map(function(item, index) {
                    var textOp = "off-shelf";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        textOp = "enabled";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    // concatenate
                    tempHtml += '' + '<tr>'
                        + '<td data-label="name" class="right aligned collapsing">'
                        + item.productName
                        + '</td>'
                        + '<td data-label="priority" class="right aligned">'
                        + item.priority
                        + '</td>'
                        + '<td data-label="operation" class="right aligned">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">Edit</a>'
                        + '<a href="#" class="status" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">Preview</a>'
                        + '</td>'
                        + '</tr>';
                });

                tempHtml += '</tbody>'
                $('.product-wrap').html(tempHtml);
            }
        });
    }

    $('.product-wrap')
        .on(
            'click',
            'a',
            function(e) {
                var target = $(e.currentTarget);
                if (target.hasClass('edit')) {
                    window.location.href = '#'
                        + e.currentTarget.dataset.id;
                } else if (target.hasClass('status')) {
                    changeItemStatus(e.currentTarget.dataset.id,
                        e.currentTarget.dataset.status);
                } else if (target.hasClass('preview')) {
                    window.location.href = '#'
                        + e.currentTarget.dataset.id;
                }
            });
    function changeItemStatus(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        var reply = confirm('confirm?');
        if (reply) {
            $.ajax({
                url : statusUrl,
                type : 'POST',
                data : {
                    productStr : JSON.stringify(product),
                    statusChange : true
                },
                dataType : 'json',
                success : function(data) {
                    if (data.success) {
                        alert('success！');
                        getList();
                    } else {
                        alert('fail！');
                    }
                }
            });
        }

    }
});