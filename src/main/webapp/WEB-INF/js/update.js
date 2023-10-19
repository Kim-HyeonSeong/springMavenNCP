$(function(){
	var seq = $('#seq').val();
	
	//이미지 정보 불러오기
	$.ajax({
		type: "post",
    	url: "/springMavenNCP/user/getImage",
    	data: {seq:seq},
   		dataType: "json",
   	 	success: function (data) {
			$('#imageName').val(data.imageName);
			$('#imageContent').val(data.imageContent);
			var img = `<img id="imgView" src="" style="width:40px">`;
			$('#showImgList').append(img);
			$('#imgView').attr('src', 'https://kr.object.ncloudstorage.com/bitcamp-edu-bucket-95/storage/'+data.imageFileName);
   	 	},
   	 	error:function(e){
		console.log(e);
	 	}
	});//ajax
	
	/*
	$(document).on('click', '#updateBtn', function (){
			var seq = $(this).closest('tr').find('#seq').text(); 
			
      		location.href='/springMavenNCP/user/updateForm?seq='+seq;
	});
	
	 //삭제버튼
	$(document).on('click', '#deleteBtn', function (){
			alert("삭제 하시겠습니까?");
			var seq = $(this).closest('tr').find('#seq').text(); 
			console.log(seq);
			
			$.ajax({
			type:'post',
			url:'/springMavenNCP/user/delete',
			data:{ seq: seq},
			success: function(data){
				alert("회원정보 삭제완료");
				window.location.reload();
			},
			error: function(e){
				console.log(e);
			}
		});
	});
	*/
})