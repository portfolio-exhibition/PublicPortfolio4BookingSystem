
//大まかな処理の流れは以下のとおり
//1.idに"imageFile"が指定されているHTML要素（画像ファイルを選択するためのinput要素）の値が変更される度に、イベント処理が実行される
//2.選択された画像ファイルが正常に読み込まれたら、idに"imagePreview"が指定されているHTML要素の中に、読み込まれた画像ファイルを表示するための img要素を挿入する
//3.選択された画像ファイルがない場合、idに"imagePreview"が指定されているHTML要素の中身を空にする

const imageInput = document.getElementById('imageFile');
const imagePreview = document.getElementById('imagePreview');

imageInput.addEventListener('change', () => {
 if (imageInput.files[0]) {
   let fileReader = new FileReader();
   fileReader.onload = () => {
     imagePreview.innerHTML = `<img src="${fileReader.result}" class="mb-3">`;
   }
   fileReader.readAsDataURL(imageInput.files[0]);
 } else {
   imagePreview.innerHTML = '';
 }
})