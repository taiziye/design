$(document).ready(function() {
	var timer=5;
	var flag;
	$("#joinclick").click(function(){
		if($("#name").val().length >= 20 || $("#name").val() == "" || $("#psw").val().length <= 6 ||$("#psw").val().length <= 6 || $("#psw").val() != $("#checkpsw").val()){
			alert('请正确填写注册信息！');
		}
		else{
		    $.getJSON("/user/join", {
		        "email": $("#email").val(), 
		    	"name": $("#name").val(),
		        "password": $("#psw").val()
		    },
		    function(data) {
		    	if(data.status == "success"){
		    		console.log("register success!");
					// flag=setInterval(daoji,1000);
					// $.cookie('user_name',$("#name").val());
					$('#myModal').modal({
				      keyboard: true
				   });
				}					
		    	if(data.status == "fail"){
		    		console.log("register fail!");
		    		$('#myModal2').modal({
				      keyboard: true
				   });
		    	}	    	
		    });
	    }
	});

	$('#myModal2').on('hide.bs.modal', function () {
  	location.href="join.html";
})
    function daoji(){
		timer=timer-1;
		// myspan.innerHTML=timer;
		console.log(timer);
		if(timer==0){
		location.href="index.html";
		clearInterval(flag);
		}	
	}


	//有效性判定
	$("#email").blur(function(){	
		CheckMail($("#email").val());

	});
	$("#name").blur(function(){
		if ($("#name").val().length >= 20 || $("#name").val() == "") {
			alert('请将用户名设置在20个英文字符以内');
			remove("name");
			insert("name","cross");
		}
		else{
			remove("name");
			insert("name","check");
		}
	});
	$("#psw").blur(function(){
		if ($("#psw").val().length <= 6) {
			alert('请将密码设置在6字符以上');
			remove("psw");
			insert("psw","cross");
		}
		else{
			remove("psw");
			insert("psw","check");
		}
	});
	$("#checkpsw").blur(function(){
		if ($("#psw").val() != $("#checkpsw").val()) {
			alert('两次输入的密码不一致！请重新输入');
			remove("checkpsw");
			insert("checkpsw","cross");
		}
		else{
			remove("checkpsw");
			insert("checkpsw","check");
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