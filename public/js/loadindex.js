$(document).ready(function() {   
    $("#loading").css("display","inherit");
    if($.cookie('user_name') && $.cookie('user_name') != ""){
        console.log($.cookie('user_name'));
        $("#login_join").css("display","none");        
        $("#welcomename").css("display","inherit");
        $("#welcomename").children().append("<p style=\"color:white;\">欢迎你，<a>"+$.cookie('user_name')+"</a></p>");
        var logoutbtn = "<li><input id=\"logoutclick\" type=\"button\" class=\"btn btn-inverse btn-large btn-block\" value=\"登出\" onclick=\"\"></li>";
        document.getElementById("welcomename").innerHTML = document.getElementById("welcomename").innerHTML + logoutbtn;
    }else{
        $("#login_join").css("display","inherit");        
        $("#welcomename").css("display","none");
    }
    $("#logoutclick").click(function(){
        console.log("lalal");
        $.cookie('user_name',"");        
        $("#login_join").css("display","inherit");        
        $("#welcomename").css("display","none");
    });
         
    var sheetnum = 1;
    var isloading = false;
     $.getJSON("/product/all",    
    //$.getJSON("./json/get_index.json",
   	{"productcatalog":getParameterByName("productcatalog"),"sheetnum":sheetnum},
    function(data) {
        $("#loading").css("display","none");
        for (k in data.product) {
            var insertProduct = "<div class='col-sm-6 col-md-4 col-lg-4' id='" + data.product[k].id + "'><div class='pin'><div class='thumbnail'><a href='product.html?productid=" + data.product[k].id + "' target='_blank' ><img src='" + data.product[k].img + "' alt='" + data.product[k].title + "'/></a></div><div class='caption'><a href='product.html?productid=" + data.product[k].id + "' target='_blank' ><p>" + data.product[k].title + "</p></a></div></div></div>";
            document.getElementById("productlist").innerHTML = document.getElementById("productlist").innerHTML + insertProduct;
        }
    });
    $.getJSON("/product/index",
    // $.getJSON("./json/get_menu.json",
    function(data) {
        $("#loading").css("display","none");
        var insertmenu = "";
        for (i in data.menu) {
            insertmenu += "<li class=\"dropdown-submenu\"><a href=\"" + "index.html?productcatalog=" + data.menu[i].name + "\">" + data.menu[i].name + "<b class=\"caretleft\"></b></a><ul class=\"dropdown-menu\" style=\"margin-top:"+(i*36)+"px\">";

            for (j in data.menu[i].submenu) {
                insertmenu += "<li class=\"dropdown-submenu\"><a href=\"" + "index.html?productcatalog=" + data.menu[i].submenu[j].name + "\">" + data.menu[i].submenu[j].name + "<b class=\"caretleft\"></b></a><ul class=\"dropdown-menu\" style=\"margin-top:"+(j*36)+"px\">";

                for (k in data.menu[i].submenu[j].submenu) {
                    insertmenu += "<li><a href=\"" + "index.html?productcatalog=" + data.menu[i].submenu[j].submenu[k].name + "\">" + data.menu[i].submenu[j].submenu[k].name + "</a></li>";
                }
                insertmenu += "</ul>";
            }
            insertmenu += "</ul></li>";
        }
        insertmenu += "</ul></li>";
        document.getElementById("menu").innerHTML = document.getElementById("menu").innerHTML + insertmenu;
    });



	function getParameterByName(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		results = regex.exec(location.search);
		return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}

    
    $(window).scroll(function() {
        if (  (document.documentElement.clientHeight + 
              $(document).scrollTop() >= document.body.offsetHeight) && (isloading == false) )
        { 
            $("#loading").css("display","inherit");
            isloading = true;
            // alert("You're at the bottom of the page.");
            sheetnum++;
            // 载入分页
            $.getJSON("/product/all",    
            // $.getJSON("./json/get_index.json",
            {"productcatalog":getParameterByName("productcatalog"),"sheetnum":sheetnum},
            function(data) {
                $("#loading").css("display","none");
                for (k in data.product) {
                    var insertProduct = "<div class='col-sm-6 col-md-4 col-lg-4' id='" + data.product[k].id + "'><div class='pin'><div class='thumbnail'><a href='product.html?productid=" + data.product[k].id + "' target='_blank' ><img src='" + data.product[k].img + "' alt='" + data.product[k].title + "'/></a></div><div class='caption'><a href='product.html?productid=" + data.product[k].id + "' target='_blank' ><p>" + data.product[k].title + "</p></a></div></div></div>";
                    document.getElementById("productlist").innerHTML = document.getElementById("productlist").innerHTML + insertProduct;
                }
            });
            // 载入分页结束
            isloading = false;
        }
    });
});