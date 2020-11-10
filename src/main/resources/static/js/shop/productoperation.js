$(function() {
    var productId = getQueryString('productId');
    var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
    var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
    var productPostUrl = '/o2o/shopadmin/modifyproduct';
    var isEdit = false;

    if (productId) {
        getInfo(productId);
        isEdit = true;
    } else {
        getCategory();
        productPostUrl = '/o2o/shopadmin/addproduct';
    }

    //get product information by product id
    function getInfo(id) {
        $.getJSON(
                infoUrl,
                function(data) {
                    if (data.success) {
                        var product = data.product;
                        $('#product-name').val(product.productName);
                        $('#product-desc').val(product.productDesc);
                        $('#priority').val(product.priority);
                        $('#point').val(product.point);
                        $('#normal-price').val(product.normalPrice);
                        $('#promotion-price').val(
                            product.promotionPrice);

                        var optionHtml = '';
                        var optionArr = data.productCategoryList;
                        var optionSelected = product.productCategory.productCategoryId;
                        // 生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
                        optionArr
                            .map(function(item, index) {
                                var isSelect = optionSelected === item.productCategoryId ? 'selected'
                                    : '';
                                optionHtml += '<option data-value="'
                                    + item.productCategoryId
                                    + '"'
                                    + isSelect
                                    + '>'
                                    + item.productCategoryName
                                    + '</option>';
                            });
                        $('#category').html(optionHtml);
                    }
                });
    }

    // add product in the product category
    function getCategory() {
        $.getJSON(categoryUrl, function(data) {
            if (data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList.map(function(item, index) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId + '">'
                        + item.productCategoryName + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    // add images to the product if the number of images is less than 6
    $('.detail-img-div').on('change', '.detail-img:last-child', function() {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });

    $('#submit').click(
        function() {
            var product = {};
            product.productName = $('#product-name').val();
            product.productDesc = $('#product-desc').val();
            product.priority = $('#priority').val();
            product.point = $('#point').val();
            product.normalPrice = $('#normal-price').val();
            product.promotionPrice = $('#promotion-price').val();
            product.productCategory = {
                productCategoryId : $('#category').find('option').not(
                    function() {
                        return !this.selected;
                    }).data('value')
            };
            product.productId = productId;

            // get thumbnail
            var thumbnail = $('#small-img')[0].files[0];
            var formData = new FormData();
            formData.append('thumbnail', thumbnail);
            $('.detail-img').map(
                function(index, item) {
                    if ($('.detail-img')[index].files.length > 0) {
                        formData.append('productImg' + index,
                            $('.detail-img')[index].files[0]);
                    }
                });


            formData.append('productStr', JSON.stringify(product));
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                alert('please enter the verification code！');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            $.ajax({
                url : productPostUrl,
                type : 'POST',
                data : formData,
                contentType : false,
                processData : false,
                cache : false,
                success : function(data) {
                    if (data.success) {
                        alert('success！');
                        $('#captcha_img').click();
                    } else {
                        alert('fail！');
                        $('#captcha_img').click();
                    }
                }
            });
        });

});