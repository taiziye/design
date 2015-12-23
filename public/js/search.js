$(document).ready(function() {
	$("#searchbtn").click(function(){
		console.log($("#navbarInput-01").val());
		$("#loading").css("display","inherit");
	$.getJSON("/product/search",
   	{"searchproduct":$("#navbarInput-01").val()},
    function(data) {
    	$("#productlist").empty(); 
        $("#loading").css("display","none");
        for (k in data.product) {
            var insertProduct = "<div class='col-sm-6 col-md-4 col-lg-4' id='" + data.product[k].id + "'><div class='pin'><div class='thumbnail'><a href='product.html?productid=" + data.product[k].id + "' target='_blank' ><img src='" + data.product[k].img + "' alt='" + data.product[k].title + "'/></a></div><div class='caption'><a href='product.html?productid=" + data.product[k].id + "' target='_blank' ><p>" + data.product[k].title + "</p></a></div></div></div>";
            document.getElementById("productlist").innerHTML = document.getElementById("productlist").innerHTML + insertProduct;
        }
    });
	});
})