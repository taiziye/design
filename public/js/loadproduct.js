$(document).ready(function(){
	$("#loading").css("display","inherit");
	//./json/get_product.json
	$.getJSON("/product/detail",{"productid":getParameterByName("productid")},function (data){
		$("#loading").css("display","none");
		$("#producttitle").html(data.title);
		for(i in data.images){
			if(i == 0){
				var insertimg0 = "<div class=\"item active\"><img style=\"height:400px;\"  src="+data.images[i].url+" alt=\""+i+"\"</div>";
				document.getElementById("carl").innerHTML = document.getElementById("carl").innerHTML+insertimg0;
				var insertli0 = "<li data-target=\"#carousel-example-generic\" data-slide-to=\""+ i+"\" class=\"active\">";
				document.getElementById("ol").innerHTML = document.getElementById("ol").innerHTML+insertli0;
			
			}
			else{
				var insertimg = "<div class=\"item\"><img style=\"height:400px;\"  src="+data.images[i].url+" alt=\""+i+"\"</div>";
				document.getElementById("carl").innerHTML = document.getElementById("carl").innerHTML+insertimg;
				var insertli = "<li data-target=\"#carousel-example-generic\" data-slide-to=\""+ i+"\">";
				document.getElementById("ol").innerHTML = document.getElementById("ol").innerHTML+insertli;
			}
		}

		var insertdescription = "<p>"+  data.description +"</p>";
		document.getElementById("description").innerHTML = document.getElementById("description").innerHTML+insertdescription;
		
		$("#sourcebtn").click(function(){
			window.open(data.source);
		});

		for(j in data.guess){
			var insertguess = "<a href='product.html?productid="+ data.guess[j].id +"'><img src='"+ data.guess[j].img  +"' alt='"+  data.guess[j].title +"'/></a><p>"+ data.guess[j].title +"</p>";
			document.getElementById("guess").innerHTML = document.getElementById("guess").innerHTML+insertguess;
		}		

		for(k in data.comment){
			var insertcomment = "<div class='comment'><div class='comment-text'>"+ data.comment[k].content +"</div><div class='comment-info'><div class='username'><small>" +data.comment[k].name+ "</small></div><div class='time'><p><small>"+data.comment[k].time+"</small></p></div></div></div>";
			document.getElementById("comment").innerHTML = document.getElementById("comment").innerHTML+insertcomment;
		}
	});


	function getParameterByName(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		results = regex.exec(location.search);
		return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}


});