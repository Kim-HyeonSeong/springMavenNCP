<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	table {
		border-collapse: collapse;
	}
	table > tr, th {
		padding:10px;
	}
	div{
		color:red;
		font-size: 0.8em;
	}
</style>
</head>
<body>
<form id="updateForm">
	<table border="1">
		<tr>
			<th>상품명</th>
			<td>
				<input type="text" name="imageName" id="imageName" size="35">
				<input type="text" name="seq" id="seq" value="${param.seq}"> 
				<div id="imageNameDiv"></div>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<textarea name="imageContent" id="imageContent" rows="10" cols="50"></textarea>
				<div id="imageContentDiv"></div>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<span id="showImgList"></span>
				
				<img id="camera" src="../image/camera.png" width="30" height="30" alt="카메라">
				<input type="file" name="img[]" id="img" multiple="multiple" style="visibility: hidden;">
			</td>
		</tr>
		
		<tr>
			<td colspan="2" align="center">
				<input type="button" value="수정" id="updateCBtn">
				<input type="reset" value="취소">
			</td>
		</tr>
		
	</table>
	<br>
	
	<div id="resultDiv"></div>
</form>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.7.0.min.js"></script>
<script type="text/javascript" src="../js/upload.js"></script>
<script type="text/javascript" src="../js/update.js"></script>
<script>
$('#camera').click(function(){
	//강제로 이벤트 발생시킴
	$('#img').trigger('click');
})

<!-- upload 버튼을 누르기 전에 선택한 이미지가 맞는지 확인하기위해서 이미지를 보여준다. -->
/* $('#img').change(function(){
	readURL(this);
}) */
$('#img').change(function(){
	$('#showImgList').empty();
	for(i=0; i<this.files.length; i++){
		readURL(this.files[i]);
	}
})

function readURL(file){
	var reader = new FileReader();
	
	reader.onload = function(e){	
		var img = document.createElement('img');
		var br = document.createElement('br');
		
		img.src = e.target.result;
		img.width = 70;
		img.height = 70;
		$('#showImgList').append(img).append('&nbsp;');
	}
	reader.readAsDataURL(file);
}
</script>
</body>
</html>