$(function(){
	//이미지 리스트 불러오기
	$.ajax({
		type: "post",
    	url: "/springMavenNCP/user/getUploadList",
   	 dataType: "json",
   	 success: function (data) {
   	 	var result;
      
       $.each(data,function(index, item){
       	result = `<tr>` +
       			 `<td align="center" id="seq">` + item.seq + `</td>` +
       			 `<td align="center" id="imageFile">`+ 
       			 `<img src="https://kr.object.ncloudstorage.com/bitcamp-edu-bucket-95/storage/`+item.imageFileName+
       			 `"style='width:70px; height:70px; alt="` + item.imageName + `"'>` +
       			 `</td>` +
       			 `<td align="center">`+item.imageOriginalName+`</td>` +
       			 `<td align="center"><button id="updateBtn" onclick.href="/springMavenNCP/user/update?seq=` + item.seq + `">수정</td>` +
       			 `<td align="center"><button id="deleteBtn" onclick.href="/springMavenNCP/user/delete?seq=` + item.seq + `">삭제</td>` +
       			 `</tr>`;
       			 
       	 $('#imageListTable').append(result);
       });
   	 },
   	 error:function(e){
		console.log(e);
	 }
	});//ajax
	
	
	//이미지 수정폼으로 들어가기	
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
})