// カレンダーの設定で利用するため、以下のコードでは変数maxDateに 3か月後 の日付を代入しています。
let maxDate = new Date();
maxDate = maxDate.setMonth(maxDate.getMonth() + 3);

// id属性に"fromCheckinDateToCheckoutDate"が設定されたHTML要素（入力フォーム）に対し、Flatpickrのインスタンスを生成しています。
flatpickr('#fromCheckinDateToCheckoutDate', {
 mode: "range",
 locale: 'ja',
 minDate: 'today',
 maxDate: maxDate,
 onClose: function(selectedDates, dateStr, instance) {
//onCloseはカレンダーを閉じたときの処理を設定するためのオプションで、値には関数を指定します。
//関数の引数は以下の3つで、本アプリではdateStrのみ使用しています。
//なお、Eclipseでは使用していない引数に波線が表示される場合がありますが、無視してOKです。
//引数：1.selectedDates＜選択した日付のオブジェクト＞、2.dateStr＜選択した日付の文字列＞、3.instance＜Flatpickrのインスタンス＞

   const dates = dateStr.split(" から ");
   //if文を使って処理を分けている理由は、チェックイン日とチェックアウト日が同日のケースも考えられる為（日帰り）
   if (dates.length === 2) {   //チェックイン日とチェックアウト日に分割できた場合はフォーム部品のvalue属性に配列の各要素を代入する
     document.querySelector("input[name='checkinDate']").value = dates[0];
     document.querySelector("input[name='checkoutDate']").value = dates[1];
   } else {   //分割できなかった場合はフォーム部品のvalue属性を空にする
     document.querySelector("input[name='checkinDate']").value = '';
     document.querySelector("input[name='checkoutDate']").value = '';
   }
 }
});