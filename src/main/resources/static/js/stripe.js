//Stripe の公開可能キーをセットする
const stripe = Stripe('pk_test_');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});