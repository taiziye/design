$(document).ready(function() {
	var timer=5;
	var flag;
	$("#loginclick").click(function(){
		if($("#name").val() == "" || $("#password").val() == ""){
			alert('请正确填写登录信息！');
		}else{			
		    // $.getJSON("./json/get_login.json", {
		    	$.getJSON("/user/checkLogin", {
		        "email": $("#email").val(), 
		        "password": $("#password").val()
		    },
		    function(data) {
		    	if(data.status == "success"){
		    		console.log("login success!");
					$.cookie('user_name',data.name);
					flag=setInterval(daoji,1000);
					$('#myModal').modal({
				      keyboard: true
				   });
				}					
		    	if(data.status == "fail"){
		    		console.log("login fail!");
		    		$('#myModal2').modal({
				      keyboard: true
				   });
		    	}	    	
		    });
		}
	});


	$('#myModal2').on('hide.bs.modal', function () {
  	location.href="login.html";
  });

    function daoji(){
		timer=timer-1;
		console.log(timer);
		if(timer==0){
		location.href="index.html";
		clearInterval(flag);
		}	
	}

	$("#email").blur(function(){	
		CheckMail($("#email").val());
	});

	$("#password").blur(function(){
		if ($("#password").val() == "") {
			alert('请输入密码');
			remove("password");
			insert("password","cross");
		}
		else{
			remove("password");
			insert("password","check");
		}
	});

	function CheckMail(mail) {
	    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	    if (filter.test(mail)){
	    	remove("email");
		    insert("email","check");
		    return true;
	    } 
	    else {
	        alert('您的电子邮件格式不正确');	        
	    	remove("email");
	        insert("email","cross");
	        return false;
	    }
	}

	function insert(obj,mark){
		var markhtml = "<img class=\"checkjoinpic\" src=\"images/"+ mark +".png\">";
		$("#"+obj).after(markhtml);
	}
	function remove(obj){
		$("#"+obj).next().remove();
	}
});