$(function() {

    getlist();
    function getlist(e) {
        $.ajax({
            url : "/o2o/shopadmin/getshoplist",
            type : "get",
            dataType : "json",
            success : function(data) {
                if (data.success) {
                    handleList(data.shopList);
                    handleUser(data.user);
                }
            }
        });
    }

    function handleUser(data) {
        $('#user-name').text(data.name);
    }

    //concatenate the row information
    function handleList(data) {
        var html = '';
        data.map(function(item, index) {
            html += '<div class="row row-shop"><div class="four wide column">'
                + item.shopName + '</div><div class="four wide column">'
                + shopStatus(item.enableStatus)
                + '</div><div class="four wide column">'
                + goShop(item.enableStatus, item.shopId) + '</div></div>';

        });
        $('.shop-wrap').html(html);
    }

    function shopStatus(status) {
        if (status == 0) {
            return 'under review';
        } else if (status == -1) {
            return 'illegal shop';
        } else if (status == 1) {
            return 'pass';
        }
    }

    function goShop(status, id) {
        if (status == 1) {
            return '<a href="/o2o/shopmanagement?shopId=' + id
                + '">Enter</a>';
        } else {
            return '';
        }
    }

})